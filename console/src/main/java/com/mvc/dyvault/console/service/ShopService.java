package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BusinessShop;
import com.mvc.dyvault.common.bean.BusinessShopPayment;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessShopMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService extends AbstractService<BusinessShop> implements BaseService<BusinessShop> {

    @Autowired
    BusinessShopPaymentService businessShopPaymentService;
    @Autowired
    BusinessShopMapper businessShopMapper;

    public List<PaymentVO> getPaymentDetail(BigInteger id) {
        List<BusinessShopPayment> list = businessShopPaymentService.findBy("shopId", id);
        return list.stream().filter(obj -> obj.getStatus() == 1).map(obj -> {
            PaymentVO vo = new PaymentVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;
        }).collect(Collectors.toList());
    }

}