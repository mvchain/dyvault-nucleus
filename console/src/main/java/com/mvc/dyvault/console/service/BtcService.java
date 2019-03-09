package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.dyvault.common.bean.AdminWallet;
import com.mvc.dyvault.common.bean.BlockTransaction;
import com.mvc.dyvault.common.bean.CommonAddress;
import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.constant.RedisConstant;
import com.mvc.dyvault.common.util.BaseContextHandler;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.util.btc.BtcAction;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.*;
import com.neemre.btcdcli4j.core.http.HttpLayerException;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/29 14:03
 */
@Service("BtcService")
@Transactional(rollbackFor = RuntimeException.class)
@Log4j
public class BtcService extends BlockService {

    @Autowired
    BtcdClient btcdClient;
    private static String nowHash = "";
    private AdminWallet hotWallet = null;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    BlockUsdtWithdrawQueueService blockUsdtWithdrawQueueService;
    //发送usdt时必然会带上这笔金额,因此发送手续费时需要额外发送这笔数量,否则会从预设手续费中扣除从而导致和期望结果不一致
    private final BigDecimal USDT_LIMIT_FEE = new BigDecimal("0.00000546");

    @Override
    public BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException {
        return null;
    }

    @Override
    public BigInteger getEthEstimateTransferFrom(String contractAddress, String from, String to) throws IOException {
        return null;
    }

    @Override
    public BigInteger getEthEstimateApprove(String contractAddress, String from, String to) throws IOException {
        return null;
    }

    @Override
    public void send(AdminWallet hot, String address, BigDecimal fromWei) throws IOException {

    }

    @Override
    public BigDecimal getBalance(String tokenName) {
        return null;
    }

    @Override
    public BigInteger getEthEstimateTransfer(String tokenContractAddress, String toAddress, String address, BigDecimal value) throws IOException {
        return null;
    }

    @Override
    public void run(String... args) throws Exception {
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "btcOldListener");
            try {
                oldListener();
            } catch (Exception e) {
                oldListener();
            }
        });

    }

    protected String getOrderNumber() {
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        return "P" + String.format("%09d", id);
    }

    private List<BlockTransaction> blockTransaction(Transaction tx, Integer height) throws IOException {
        List<BlockTransaction> list = new ArrayList<>(tx.getDetails().size());
        tx.getDetails().stream().forEach(obj -> {
            CommonAddress address = commonAddressService.findOneBy("address", obj.getAddress());
            if (null == address || null == address.getUserId()) {
                return;
            }
            List<BlockTransaction> result = blockTransactionService.findBy("hash", tx.getTxId());
            if (result.size() > 0) {
                for (BlockTransaction transaction : result) {
                    transaction.setHeight(BigInteger.valueOf(height));
                    transaction.setFee(null == obj.getFee() ? BigDecimal.ZERO : obj.getFee());
                    transaction.setFromAddress(address.getAddress());
                    transaction.setUpdatedAt(System.currentTimeMillis());
                    blockTransactionService.update(transaction);
                }
                return;
            }
            BlockTransaction transaction = new BlockTransaction();
            Long time = System.currentTimeMillis();
            transaction.setCreatedAt(time);
            transaction.setUpdatedAt(time);
            transaction.setHash(tx.getTxId());
            transaction.setValue(obj.getAmount().abs());
            transaction.setOprType(BigInteger.ZERO.equals(address.getUserId()) ? 9 : 1);
            transaction.setErrorData("");
            transaction.setErrorMsg("");
            transaction.setTransactionStatus(4);
            transaction.setStatus(1);
            transaction.setFee(null == obj.getFee() ? BigDecimal.ZERO : obj.getFee());
            transaction.setFromAddress(address.getAddress());
            transaction.setToAddress(address.getAddress());
            transaction.setUserId(address.getUserId());
            transaction.setTokenId(BusinessConstant.BASE_TOKEN_ID_BTC);
            transaction.setTokenType("BTC");
            transaction.setOrderNumber(getOrderNumber());
            transaction.setHeight(BigInteger.valueOf(height));
            list.add(transaction);
        });
        return list;
    }

    private void oldListener() {
        String lastNumber = getHeight();
        Block block = null;
        while (true) {
            try {
                Thread.sleep(100);
                String height = btcdClient.getBlockChainInfo().getBestBlockHash();
                if (StringUtils.isBlank(lastNumber)) {
                    lastNumber = btcdClient.getBlockChainInfo().getBestBlockHash();
                    redisTemplate.opsForValue().set(RedisConstant.BTC_LAST_HEIGHT, lastNumber);
                }
                block = btcdClient.getBlock(lastNumber);
                Boolean ignore = isIgnore(lastNumber, block, height);
                if (ignore) {
                    continue;
                }
                List<String> txList = block.getTx();
                readTxList(txList, block.getHeight());
                nowHash = null == block ? nowHash : block.getHash();
                lastNumber = block.getNextBlockHash();
                if (null != block && null != block.getNextBlockHash()) {
                    redisTemplate.opsForValue().set(RedisConstant.BTC_LAST_HEIGHT, lastNumber);
                }
                updateStatus(block.getHeight().toString());
            } catch (HttpLayerException e1) {
                log.warn(e1.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readTxList(List<String> txList, Integer height) {
        for (String txId : txList) {
            try {
                Object txStr = null;
                try {
                    txStr = btcdClient.remoteCall("omni_gettransaction", Arrays.asList(txId));
                } catch (BitcoindException e) {
                    //notBtc transaction
                }
                if (null != txStr) {
                    continue;
                }
                Transaction tx = btcdClient.getTransaction(txId, true);
                if (null == tx) {
                    continue;
                }
                List<BlockTransaction> transs = blockTransaction(tx, height);
                transs.forEach(trans -> {
                    if (null != trans) {
                        saveOrUpdate(trans, "BTC");
                    }
                });
            } catch (Exception e) {
                // not mine transaction
            }
        }
    }

    @NotNull
    private Boolean isIgnore(String lastNumber, Block block, String height) {
        Boolean ignore = false;
        if (lastNumber.equals(height)) {
            ignore = true;
        }
        if (block.getConfirmations() == -1) {
            redisTemplate.opsForValue().set(RedisConstant.BTC_LAST_HEIGHT, block.getPreviousBlockHash());
            ignore = true;
        }
        if (nowHash.equalsIgnoreCase(block.getHash())) {
            ignore = true;
        }
        return ignore;
    }

    private String getHeight() {
        String lastNumber = redisTemplate.opsForValue().get(RedisConstant.USDT_LAST_HEIGHT);
        if (StringUtils.isBlank(lastNumber)) {
            String height = null;
            try {
                height = btcdClient.getBlockChainInfo().getBestBlockHash();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastNumber = String.valueOf(height);
            redisTemplate.opsForValue().set(RedisConstant.USDT_LAST_HEIGHT, lastNumber);
        }
        return lastNumber;
    }

    private void updateStatus(String lastNumber) {
        BigInteger height = NumberUtils.createBigInteger(lastNumber).subtract(BigInteger.valueOf(4));
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        ConditionUtil.andCondition(criteria, "height <= ", height);
        ConditionUtil.andCondition(criteria, "token_type = ", "BTC");
        PageHelper.startPage(1, 10);
        List<BlockTransaction> blockTransaction = blockTransactionService.findByCondition(condition);
        blockTransaction.forEach(obj -> {
            if (!obj.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_BTC)) {
                return;
            }
            blockTransactionService.updateSuccess(obj);
            try {
                updateAddressBalance(obj.getTokenId(), obj.getFromAddress(), BtcAction.getBtcBalance(obj.getFromAddress(), 0));
                updateAddressBalance(obj.getTokenId(), obj.getToAddress(), BtcAction.getBtcBalance(obj.getToAddress(), 0));
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        });
    }

    /**
     * 批量发送时手续费默认为单次发送的一半(按笔数算)
     *
     * @param wallet
     * @param token
     * @param addresses
     */
    private void sendBtc(AdminWallet wallet, CommonToken token, List<String> addresses) throws
            BitcoindException, CommunicationException {
        Map<String, BigDecimal> output = new HashMap<>();
        List<OutputOverview> input = new ArrayList<>(addresses.size());
        List<Output> listUnspent = btcdClient.listUnspent();
        //热钱包中的总余额
        BigDecimal total = btcdClient.getBalance();
        BigDecimal use = BigDecimal.ZERO;
        listUnspent = listUnspent.stream().filter(obj -> obj.getSpendable() == true).collect(Collectors.toList());
        BigDecimal fee = BigDecimal.ZERO.add(BigDecimal.valueOf(new Float(String.valueOf(token.getTransaferFee())) * addresses.size() / 2));
        for (Output obj : listUnspent) {
            //使用后余额也还原到该地址
            input.add(obj);
        }
        for (String address : addresses) {
            BigDecimal value = new BigDecimal(String.valueOf(token.getTransaferFee())).add(USDT_LIMIT_FEE);
            use = use.add(value);
            output.put(address, value);
        }
        //找零 = 总余额 - 发送余额 - 预设手续费
        output.put(wallet.getAddress(), total.subtract(use).subtract(fee));
        String row = btcdClient.createRawTransaction(input, output);
        SignatureResult res = btcdClient.signRawTransaction(row);
        if (res.getComplete()) {
            btcdClient.sendRawTransaction(res.getHex());
        }
    }

    @NotNull
    private Boolean getIgnore(AdminWallet wallet) throws BitcoindException, CommunicationException {
        Boolean ignore = false;
        if (null == wallet) {
            ignore = true;
        }
        BigDecimal balance = btcdClient.getBalance(wallet.getAddress());
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            ignore = true;
        }
        return ignore;
    }
}
