package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BusinessShopPayment;
import com.mvc.dyvault.common.bean.vo.SupplierVO;
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
        newPayment.setShopId(paymentDTO.getShopId());
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

    public void updateStatus(BigInteger userId, SupplierVO supplierVO) {
        updateStatusByType(userId, 1, supplierVO.getBankSwitch());
        updateStatusByType(userId, 2, supplierVO.getAliPaySwitch());
        updateStatusByType(userId, 3, supplierVO.getWeChatSwitch());
    }

    private void updateStatusByType(BigInteger userId, Integer paymentType, Integer status) {
        BusinessShopPayment payment = new BusinessShopPayment();
        payment.setUserId(userId);
        payment.setPaymentType(paymentType);
        payment = findOneByEntity(payment);
        if (null != payment) {
            payment.setStatus(status);
            update(payment);
            updateCache(payment.getId());
        }
    }

    public PaymentDTO getPayment(BigInteger id, Integer paymentType) {
        BusinessShopPayment payment = new BusinessShopPayment();
        payment.setPaymentType(paymentType);
        payment = findOneByEntity(payment);
        if (null != payment) {
            PaymentDTO result = new PaymentDTO();
            BeanUtils.copyProperties(payment, result);
            return result;
        }
        return null;
    }
}