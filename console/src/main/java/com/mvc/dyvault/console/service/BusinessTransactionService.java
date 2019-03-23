package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.dyvault.common.bean.BusinessShop;
import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.bean.dto.BusinessSearchDTO;
import com.mvc.dyvault.common.bean.vo.BusinessDetailVO;
import com.mvc.dyvault.common.bean.vo.BusinessSimpleVO;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessTransactionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessTransactionService extends AbstractService<BusinessTransaction> implements BaseService<BusinessTransaction> {

    @Autowired
    BusinessTransactionMapper businessTransactionMapper;
    @Autowired
    AppUserService appUserService;
    @Autowired
    ShopService shopService;

    public List<BusinessSimpleVO> getBusinessList(BusinessSearchDTO pageDTO, BigInteger userId) {
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        if (null != pageDTO.getId() && !pageDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", pageDTO.getId());
        }
        if (pageDTO.getStatus() != 0) {
            ConditionUtil.andCondition(criteria, "status = ", pageDTO.getStatus());
        }
        ConditionUtil.andCondition(criteria, String.format("(buy_user_id = %s or sell_user_id = %s )", userId, userId));
        List<BusinessSimpleVO> result = findByCondition(condition).stream().map(obj -> {
            BusinessSimpleVO vo = new BusinessSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;

        }).collect(Collectors.toList());
        return null == result || result.size() == 0 ? new ArrayList<BusinessSimpleVO>() : result;
    }

    public BusinessDetailVO getBusiness(BigInteger id, BigInteger userId) {
        BusinessDetailVO result = new BusinessDetailVO();
        BusinessTransaction tx = findById(id);
        if (null == tx || !(tx.getBuyUserId().equals(userId) || tx.getSellUserId().equals(userId))) {
            return result;
        }
        BeanUtils.copyProperties(tx, result);
        return result;
    }

    public OrderDetailVO getDetail(BigInteger userId, BigInteger id) {
        BusinessTransaction tx = findById(id);
        if (!(tx.getSellUserId().equals(userId) || tx.getBuyUserId().equals(userId))) {
            return null;
        }
        OrderDetailVO result = new OrderDetailVO();
        BeanUtils.copyProperties(tx, result);
        return result;
    }

    public OrderDetailVO checkOrderExist(BigInteger userId) {
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        ConditionUtil.andCondition(criteria, String.format("(buy_user_id = %s or sell_user_id = %s )", userId, userId));
        List<BusinessTransaction> tx = findByCondition(condition);
        if (tx.size() > 0) {
            OrderDetailVO result = new OrderDetailVO();
            BeanUtils.copyProperties(tx.get(0), result);
            return result;
        }
        return null;
    }

    public BigInteger confirmOrder(BigInteger userId, ConfirmOrderDTO confirmOrderDTO) {
        Long time = System.currentTimeMillis();
        checkSign(confirmOrderDTO);
        BusinessShop shop = shopService.findById(confirmOrderDTO.getShopId());
        BusinessTransaction tx = new BusinessTransaction();
        BeanUtils.copyProperties(confirmOrderDTO, tx);
        tx.setOrderType(1);
        tx.setOrderStatus(0);
        tx.setStatus(1);
        tx.setCreatedAt(time);
        tx.setBuyUserId(userId);
        tx.setSellUserId(shop.getUserId());
        tx.setUserId(userId);
        tx.setUpdatedAt(time);
        tx.setPayAt(time);
        tx.setAutoSend(1);
        tx.setSelfOrderNumber(getOrderNumber());
        save(tx);
        sendPush();
        return tx.getId();
    }

    private void checkSign(ConfirmOrderDTO confirmOrderDTO) {

    }

    public Boolean updateStatus(BigInteger userId, BigInteger id, Integer status, String payAccount) {
        BusinessTransaction tx = findById(id);
        if (!tx.getStatus().equals(userId)) {
            return false;
        }
        if (status == 1) {
            tx.setPayAccount(payAccount);
            tx.setStatus(1);
            tx.setOrderStatus(1);
            tx.setPayAt(System.currentTimeMillis());
            tx.setUpdatedAt(System.currentTimeMillis());
            update(tx);
            updateCache(tx.getId());
        } else if (status == 4) {
            tx.setStatus(status);
            tx.setUpdatedAt(System.currentTimeMillis());
            update(tx);
            updateCache(tx.getId());
        }
        sendPush();
        return true;
    }

    private void sendPush() {
    }
}