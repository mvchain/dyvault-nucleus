package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.dyvault.common.bean.*;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.TransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.ProjectPublishListVO;
import com.mvc.dyvault.common.bean.vo.TransactionDetailVO;
import com.mvc.dyvault.common.bean.vo.TransactionSimpleVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.common.util.MessageConstants;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.constant.BusinessConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppOrderService extends AbstractService<AppOrder> implements BaseService<AppOrder> {

    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonAddressService commonAddressService;
    @Autowired
    BlockTransactionService blockTransactionService;
    @Autowired
    AppMessageService appMessageService;
    @Autowired
    CommonTokenService tokenService;

    public TransactionDetailVO getDetail(BigInteger userId, BigInteger id) {
        var order = findById(id);
        if (null == order) {
            return null;
        }
        Assert.isTrue(userId.equals(order.getUserId()), MessageConstants.getMsg("PERMISSION_WRONG"));
        var token = commonTokenService.findById(order.getTokenId());
        TransactionDetailVO vo = new TransactionDetailVO();
        vo.setClassify(order.getClassify());
        vo.setCreatedAt(order.getCreatedAt());
        vo.setFee(order.getFee());
        vo.setFeeTokenType(token.getTokenName());
        vo.setHashLink(token.getLink() + order.getHash());
        vo.setOrderRemark(order.getOrderRemark());
        vo.setTokenName(token.getTokenName());
        vo.setFromAddress(order.getFromAddress());
        vo.setTransactionType(order.getOrderType());
        vo.setBlockHash(order.getHash());
        vo.setOrderNumber(order.getOrderNumber());
        vo.setStatus(order.getStatus());
        vo.setToAddress(order.getToAddress());
        vo.setUpdatedAt(order.getUpdatedAt());
        vo.setValue(null == order.getValue() ? null : order.getValue().abs());
        if (order.getClassify() == 0 && StringUtils.isNotBlank(vo.getBlockHash())) {
            List<BlockTransaction> blockTrans = blockTransactionService.findBy("hash", vo.getBlockHash());
            if (null != blockTrans && blockTrans.size() > 0) {
                vo.setHeight(blockTrans.get(0).getHeight());
            }
        }
        return vo;
    }

    public List<TransactionSimpleVO> getTransactions(BigInteger userId, TransactionSearchDTO transactionSearchDTO) {
        Condition condition = new Condition(AppOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        ConditionUtil.andCondition(criteria, "token_id = ", transactionSearchDTO.getTokenId());
        if(null != transactionSearchDTO.getTransactionType() && 0!= transactionSearchDTO.getTransactionType()){
            ConditionUtil.andCondition(criteria, "order_type = ", transactionSearchDTO.getTransactionType());
        }
        PageHelper.startPage(1, transactionSearchDTO.getPageSize());
        PageHelper.orderBy("id desc");
        if (null != transactionSearchDTO.getId() && !transactionSearchDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id <", transactionSearchDTO.getId());
        }
        List<AppOrder> list = findByCondition(condition);
        List<TransactionSimpleVO> result = new ArrayList<>(list.size());
        list.forEach(obj -> {
            CommonToken token = commonTokenService.findById(obj.getTokenId());
            CommonTokenPrice price = commonTokenPriceService.findById(token.getId());
            TransactionSimpleVO vo = new TransactionSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            vo.setValue(vo.getValue().abs());
            vo.setTokenName(token.getTokenName());
            vo.setRatio(null == price ? BigDecimal.ZERO : price.getTokenPrice());
            vo.setTransactionType(obj.getOrderType());
            result.add(vo);
        });
        return result;
    }

    public void saveOrderTarget(BlockTransaction blockTransaction) {
        //如果操作类型为提现并且地址为内部地址,需要给对应目标用户生成一条订单信息
        if (blockTransaction.getOprType() == 2) {
            CommonAddress address = commonAddressService.findOneBy("address", blockTransaction.getToAddress());
            if (null != address && !BigInteger.ZERO.equals(address.getUserId())) {
                //地址存在且有对应用户， 则生成记录
                blockTransaction.setUserId(address.getUserId());
                blockTransaction.setOprType(blockTransaction.getOprType() == 1 ? 2 : 1);
                saveOrder(blockTransaction);
            }
        }
    }

    public void saveOrder(BlockTransaction blockTransaction) {
        Long time = System.currentTimeMillis();
        //非管理员操作才需要添加到订单列表
        AppOrder order = new AppOrder();
        order.setStatus(1);
        order.setProjectId(BigInteger.ZERO);
        order.setOrderContentId(blockTransaction.getId());
        order.setOrderType(blockTransaction.getOprType());
        order.setUserId(blockTransaction.getUserId());
        order.setValue(blockTransaction.getValue());
        order.setOrderNumber(getOrderNumber());
        order.setOrderContentName(BusinessConstant.CONTENT_BLOCK);
        order.setHash(blockTransaction.getHash());
        order.setFromAddress(blockTransaction.getFromAddress());
        order.setUpdatedAt(time);
        order.setToAddress(blockTransaction.getToAddress());
        order.setFee(blockTransaction.getFee());
        order.setCreatedAt(time);
        order.setOrderRemark(tokenService.getTokenName(blockTransaction.getTokenId()));
        order.setTokenId(blockTransaction.getTokenId());
        order.setClassify(BusinessConstant.CLASSIFY_BLOCK);
        save(order);
    }

    public void updateOrder(AppOrder obj, BigDecimal fee) {
        //修改订单列表,并发送通知
        obj.setStatus(2);
        obj.setHash(obj.getHash());
        obj.setFee(fee);
        obj.setUpdatedAt(System.currentTimeMillis());
        update(obj);
        appMessageService.transferMsg(obj.getId(), obj.getUserId(), obj.getValue(), tokenService.getTokenName(obj.getTokenId()), obj.getOrderType(), obj.getStatus());
        updateCache(obj.getId());
    }


    public void saveReturnOrder(BlockTransaction blockTransaction) {
        AppOrder order = new AppOrder();
        order.setClassify(0);
        order.setOrderContentId(blockTransaction.getId());
        List<AppOrder> list = findByEntity(order);
        list.forEach(obj -> {
            obj.setStatus(9);
            obj.setUpdatedAt(System.currentTimeMillis());
            update(obj);
            updateCache(obj.getId());
        });
    }

    public void updateOrderWithBlockTransaction(BlockTransaction oldTrans) {
        AppOrder order = new AppOrder();
        order.setClassify(0);
        order.setOrderContentId(oldTrans.getId());
        List<AppOrder> list = findByEntity(order);
        if (list.size() == 0) {
            //newOrder
            saveOrder(oldTrans);
        } else {
            list.forEach(obj -> {
                obj.setStatus(oldTrans.getStatus());
                obj.setUpdatedAt(System.currentTimeMillis());
                obj.setHash(oldTrans.getHash());
                update(obj);
                updateCache(obj.getId());
            });
        }
    }


    //“项目名称”理财成功参与： 100 USDT
    //“项目名称”理财提成发放： 10 ETH
    //“项目名称”理财取出本金：1000 USDT
    //“项目名称”理财取出利息： 10 USDT
    public void saveOrder(AppUserFinancialPartake partake, AppFinancial financial) {
        String msg = financial.getName() + " 理财取出本金: " + partake.getValue().setScale(4, RoundingMode.DOWN) + " " + commonTokenService.getTokenName(partake.getBaseTokenId());
        String incomeMsg = financial.getName() + " 理财取出利息: " + partake.getIncome().setScale(4, RoundingMode.DOWN) + " " + commonTokenService.getTokenName(partake.getTokenId());
        saveOrder(msg, partake, partake.getValue(), financial.getBaseTokenId(), financial.getName(), financial.getName(), 4);
        saveOrder(4, partake.getId(), BusinessConstant.CONTENT_FINANCIAL, getOrderNumber(), partake.getIncome(), partake.getUserId(), partake.getTokenId(), 6, 1, financial.getName(), incomeMsg, false);
    }

    private void saveOrder(String msg, AppUserFinancialPartake partake, BigDecimal value, BigInteger tokenId, String remark, String name, Integer status) {
        Long time = System.currentTimeMillis();
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(4);
        appOrder.setCreatedAt(time);
        appOrder.setUpdatedAt(time);
        appOrder.setFromAddress("");
        appOrder.setHash("");
        appOrder.setOrderContentId(partake.getId());
        appOrder.setOrderContentName(BusinessConstant.CONTENT_FINANCIAL);
        appOrder.setOrderNumber(getOrderNumber());
        appOrder.setValue(value);
        appOrder.setUserId(partake.getUserId());
        appOrder.setTokenId(tokenId);
        appOrder.setStatus(status);
        appOrder.setOrderType(1);
        appOrder.setFee(BigDecimal.ZERO);
        appOrder.setToAddress("");
        appOrder.setOrderRemark(remark);
        save(appOrder);
        appMessageService.transferFinancialMsg(name, appOrder.getId(), appOrder.getUserId(), value, tokenService.getTokenName(tokenId), msg);
    }


    /**
     * @param classify         0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账 4理財
     * @param orderContentId   关联数据id
     * @param orderContentName 关联类型
     * @param orderNumber      订单号
     * @param value            数量
     * @param userId
     * @param tokenId
     * @param status           状态0打包中 1确认中 2确认
     * @param orderType        1转入 2转出
     * @param remark
     * @param message
     */
    public void saveOrder(Integer classify, BigInteger orderContentId, String orderContentName, String orderNumber, BigDecimal value, BigInteger userId, BigInteger tokenId, Integer status, Integer orderType, String remark, String message, Boolean sendMsg) {
        Long time = System.currentTimeMillis();
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(4);
        appOrder.setCreatedAt(time);
        appOrder.setUpdatedAt(time);
        appOrder.setFromAddress("");
        appOrder.setHash("");
        appOrder.setOrderContentId(orderContentId);
        appOrder.setOrderContentName(orderContentName);
        appOrder.setOrderNumber(orderNumber);
        appOrder.setValue(value);
        appOrder.setUserId(userId);
        appOrder.setTokenId(tokenId);
        appOrder.setFee(BigDecimal.ZERO);
        appOrder.setStatus(status);
        appOrder.setOrderType(orderType);
        appOrder.setOrderRemark(remark);
        appOrder.setToAddress("");
        save(appOrder);
        appMessageService.transferMsg(appOrder.getId(), appOrder.getUserId(), message, sendMsg);
    }

    public void saveOrder(Integer classify,  String from, String to, BigInteger tokenId, BigDecimal value, BigInteger userId, String tokenName, Integer orderType) {
        CommonToken token = tokenService.findById(tokenId);
        Long time = System.currentTimeMillis();
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(classify);
        appOrder.setCreatedAt(time);
        appOrder.setUpdatedAt(time);
        appOrder.setFromAddress(from);
        appOrder.setFee(BigDecimal.valueOf(token.getFee()));
        appOrder.setHash("");
        appOrder.setOrderContentId(BigInteger.ZERO);
        appOrder.setOrderContentName(BusinessConstant.INNER_BLOCK);
        appOrder.setOrderNumber(getOrderNumber());
        appOrder.setValue(value);
        appOrder.setToAddress(to);
        appOrder.setUserId(userId);
        appOrder.setTokenId(tokenId);
        appOrder.setStatus(2);
        appOrder.setOrderType(orderType);
        appOrder.setOrderRemark(tokenName);
        save(appOrder);
        appMessageService.transferMsg(appOrder.getId(), userId, value, tokenName, orderType, 2);
    }

    public List<ProjectPublishListVO> getPublishList(BigInteger userId, BigInteger projectId, BigInteger id, PageDTO pageDTO) {
        if (null == id || id.equals(BigInteger.ZERO)) {
            id = BigInteger.valueOf(Integer.MAX_VALUE);
        }
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(AppOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        ConditionUtil.andCondition(criteria, "status = ", 2);
        ConditionUtil.andCondition(criteria, "order_content_name = ", BusinessConstant.CONTENT_PROJECT);
        ConditionUtil.andCondition(criteria, "order_content_id = ", projectId);
        ConditionUtil.andCondition(criteria, "id < ", id);
        List<AppOrder> list = findByCondition(condition);
        return list.stream().map(obj -> {
            ProjectPublishListVO vo = new ProjectPublishListVO();
            vo.setCreatedAt(obj.getCreatedAt());
            vo.setId(obj.getId());
            vo.setTokenId(obj.getTokenId());
            vo.setTokenName(commonTokenService.getTokenName(obj.getTokenId()));
            vo.setValue(obj.getValue());
            return vo;
        }).collect(Collectors.toList());
    }

}