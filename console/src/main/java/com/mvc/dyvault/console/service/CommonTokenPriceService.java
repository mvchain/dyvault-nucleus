package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.bean.CommonTokenControl;
import com.mvc.dyvault.common.bean.CommonTokenHistory;
import com.mvc.dyvault.common.bean.CommonTokenPrice;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.dao.CommonTokenHistoryMapper;
import com.mvc.dyvault.console.dao.CommonTokenPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class CommonTokenPriceService extends AbstractService<CommonTokenPrice> implements BaseService<CommonTokenPrice> {

    @Autowired
    CommonTokenPriceMapper commonTokenPriceMapper;
    @Autowired
    CommonTokenHistoryMapper historyMapper;
    @Autowired
    CommonTokenService commonTokenService;

    public void saveHistory(BigInteger tokenId, BigDecimal usdtPrice) {
        if (usdtPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        CommonTokenHistory history = new CommonTokenHistory();
        history.setCreatedAt(System.currentTimeMillis());
        history.setPrice(usdtPrice);
        history.setTokenId(tokenId);
        historyMapper.insert(history);
        updatePrice(tokenId, usdtPrice);
    }

    public void updatePrice(BigInteger tokenId, BigDecimal nextPrice) {
        Long time = System.currentTimeMillis();
        CommonTokenPrice price = findById(tokenId);
        if (null == price) {
            price = new CommonTokenPrice();
            price.setTokenPrice(nextPrice);
            price.setTokenId(tokenId);
            price.setTokenName(commonTokenService.getTokenName(tokenId));
            save(price);
        }
        price.setTokenPrice(nextPrice);
        update(price);
        CommonTokenHistory commonTokenHistory = new CommonTokenHistory();
        commonTokenHistory.setCreatedAt(time);
        commonTokenHistory.setTokenId(tokenId);
        commonTokenHistory.setPrice(nextPrice);
        historyMapper.insert(commonTokenHistory);
        updateAllCache();
        updateCache(tokenId);
    }
}