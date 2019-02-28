package com.mvc.dyvault.app.service;

import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.dto.AssertVisibleDTO;
import com.mvc.dyvault.common.bean.dto.TransactionDTO;
import com.mvc.dyvault.common.bean.dto.TransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.*;
import com.mvc.dyvault.common.util.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AssetService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;
    private final static String USDT_REG_ARR = "^[123mn][a-zA-Z1-9]{24,34}$";
    private final static String ETH_REG = "^(0x)?[0-9a-fA-F]{40}$";

    public List<TokenBalanceVO> getAsset(BigInteger userId) {
        Result<List<TokenBalanceVO>> result = consoleRemoteService.getAsset(userId);
        return result.getData();
    }

    public TokenBalanceVO getAsset(BigInteger userId, BigInteger tokenId) {
        Result<TokenBalanceVO> result = consoleRemoteService.getAssetByTokenId(userId, tokenId);
        return result.getData();
    }

    public BigDecimal getBalance(BigInteger userId) {
        Result<BigDecimal> result = consoleRemoteService.getBalance(userId);
        return result.getData();
    }

    public List<TransactionSimpleVO> getTransactions(BigInteger userId, TransactionSearchDTO transactionSearchDTO) {
        Result<List<TransactionSimpleVO>> result = consoleRemoteService.getTransactions(userId, transactionSearchDTO.getTransactionType(), transactionSearchDTO.getId(), transactionSearchDTO.getPageSize(), transactionSearchDTO.getTokenId());
        return result.getData();
    }

    public TransactionDetailVO getTransaction(BigInteger userId, BigInteger id) {
        Result<TransactionDetailVO> result = consoleRemoteService.getTransaction(userId, id);
        return result.getData();
    }

    public String getAddress(BigInteger userId, BigInteger tokenId) {
        Result<String> result = consoleRemoteService.getAddress(userId, tokenId);
        return result.getData();
    }

    public TransactionTokenVO getTransactionInfo(BigInteger userId, BigInteger tokenId) {
        Result<TransactionTokenVO> result = consoleRemoteService.getTransactionInfo(userId, tokenId);
        return result.getData();
    }

    public Boolean sendTransaction(BigInteger userId, TransactionDTO transactionDTO) {
        Boolean regResult = false;
        if (transactionDTO.getAddress().indexOf("@") > 0) {
            regResult = true;
        } else if (transactionDTO.getTokenId().equals(BigInteger.valueOf(4)) || transactionDTO.getTokenId().equals(BigInteger.valueOf(2))) {
            //usdt
            regResult = transactionDTO.getAddress().matches(USDT_REG_ARR);
        } else {
            //ETH OR ERC20
            regResult = transactionDTO.getAddress().matches(ETH_REG);
        }
        Assert.isTrue(regResult, MessageConstants.getMsg("ADDRESS_FAIL"));
        Result<Boolean> result = consoleRemoteService.sendTransaction(userId, transactionDTO);
        return result.getData();
    }

    public Boolean updateVisible(BigInteger userId, AssertVisibleDTO assertVisibleDTO) {
        Result<Boolean> result = consoleRemoteService.updateVisible(userId, assertVisibleDTO);
        return result.getData();
    }

}
