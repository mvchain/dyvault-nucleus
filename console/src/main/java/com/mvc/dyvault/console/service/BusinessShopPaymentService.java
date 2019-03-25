package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BusinessShopPayment;
import com.mvc.dyvault.common.sdk.dto.PaymentDTO;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessShopPaymentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class BusinessShopPaymentService extends AbstractService<BusinessShopPayment> implements BaseService<BusinessShopPayment> {

    @Autowired
    BusinessShopPaymentMapper businessShopPaymentMapper;
    @Autowired
    BusinessSupplierService supplierService;

    public Boolean updatePayment(BigInteger id, PaymentDTO paymentDTO) {
        BusinessShopPayment payment = findByUserIdAndType(id, paymentDTO.getPaymentType());
        if (null != payment) {
            BeanUtils.copyProperties(paymentDTO, payment);
            update(payment);
            updateCache(payment.getId());
            return true;
        }
        BusinessShopPayment newPayment = new BusinessShopPayment();
        BeanUtils.copyProperties(paymentDTO, newPayment);
        newPayment.setUserId(id);
        newPayment.setStatus(0);
        save(newPayment);
        updateCache(newPayment.getId());
        supplierService.updateHasStatus(id, payment.getPaymentType());
        return true;
    }

    private BusinessShopPayment findByUserIdAndType(BigInteger userId, Integer type) {
        BusinessShopPayment payment = new BusinessShopPayment();
        payment.setUserId(userId);
        payment.setPaymentType(type);
        return findOneByEntity(payment);
    }
}