package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.BusinessShop;
import com.mvc.dyvault.common.bean.BusinessShopPayment;
import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessOrderVO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.DevVO;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessShopMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopService extends AbstractService<BusinessShop> implements BaseService<BusinessShop> {

    @Autowired
    BusinessShopPaymentService businessShopPaymentService;
    @Autowired
    BusinessTransactionService transactionService;
    @Autowired
    BusinessShopMapper businessShopMapper;
    @Autowired
    AppUserService appUserService;

    public List<PaymentVO> getPaymentDetail(BigInteger id) {
        List<BusinessShopPayment> list = businessShopPaymentService.findBy("shopId", id);
        return list.stream().filter(obj -> obj.getStatus() == 1).map(obj -> {
            PaymentVO vo = new PaymentVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public void deleteShop(BigInteger id) {
        BusinessShop shop = new BusinessShop();
        shop.setUserId(id);
        businessShopMapper.delete(shop);
        updateCache(id);
    }


    public void addShop(BigInteger id, String name) {
        BusinessShop shop = new BusinessShop();
        shop.setUserId(id);
        shop.setCreatedAt(System.currentTimeMillis());
        shop.setShopName(name);
        shop.setAppKey(UUID.randomUUID().toString().replace("-", ""));
        shop.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
        shop.setUpdateAt(System.currentTimeMillis());
        save(shop);
        updateCache(shop.getId());
    }

    public DevVO getDevSetting(BigInteger id) {
        BusinessShop shop = findOneBy("userId", id);
        if (null == shop) {
            return null;
        }
        DevVO result = new DevVO();
        result.setAppKey(shop.getAppKey());
        result.setAppSecret(shop.getAppSecret());
        result.setCallbackUrl(shop.getCallbackUrl());
        return result;
    }

    public Boolean setDevSetting(BigInteger id, DevDTO devDTO) {
        BusinessShop shop = findOneBy("userId", id);
        if (null == shop) {
            return false;
        }
        shop.setCallbackUrl(devDTO.getCallbackUrl());
        save(shop);
        updateCache(shop.getId());
        return true;
    }

    public PageInfo<BusinessOrderVO> getBusinessList(BigInteger id, BusinessTxSearchDTO businessTxSearchDTO) {
        PageHelper.startPage(businessTxSearchDTO.getPageNum(), businessTxSearchDTO.getPageSize(), businessTxSearchDTO.getOrderBy());
        BigInteger userId = null;
        if (StringUtils.isNotBlank(businessTxSearchDTO.getCellphone())) {
            AppUser user = appUserService.findOneBy("cellphone", businessTxSearchDTO.getCellphone());
            if (null == user) {
                return null;
            } else {
                userId = user.getId();
            }
        }
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "remitUserId = ", businessTxSearchDTO.getUserId());
        ConditionUtil.andCondition(criteria, "buyUserId = ", userId);
        ConditionUtil.andCondition(criteria, "orderNumber = ", businessTxSearchDTO.getOrderNumber());
        ConditionUtil.andCondition(criteria, "status = ", businessTxSearchDTO.getStatus());
        ConditionUtil.andCondition(criteria, "createdAt >= ", businessTxSearchDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "createdAt <=", businessTxSearchDTO.getCreatedStopAt());
        List<BusinessTransaction> list = transactionService.findByCondition(condition);
        PageInfo result = new PageInfo<>(list);
        List<BusinessOrderVO> voList = list.stream().map(obj -> {
            BusinessOrderVO vo = new BusinessOrderVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;
        }).collect(Collectors.toList());

        result.setList(voList);
        return result;
    }

    public List<BusinessTxCountVO> getBusinessCount(BigInteger id, Long startedAt, Long stopAt) {
        List<BusinessTxCountVO> result = transactionService.getBusinessCount(id, startedAt, stopAt);
        return result;
    }
}