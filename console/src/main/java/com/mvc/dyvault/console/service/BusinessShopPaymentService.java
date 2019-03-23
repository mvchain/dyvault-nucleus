package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BusinessShopPayment;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessShopPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessShopPaymentService extends AbstractService<BusinessShopPayment> implements BaseService<BusinessShopPayment> {

    @Autowired
    BusinessShopPaymentMapper businessShopPaymentMapper;
}