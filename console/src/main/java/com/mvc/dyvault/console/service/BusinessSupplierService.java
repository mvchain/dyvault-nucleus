package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BusinessSupplier;
import com.mvc.dyvault.common.bean.vo.SupplierVO;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.BusinessSupplierMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class BusinessSupplierService extends AbstractService<BusinessSupplier> implements BaseService<BusinessSupplier> {

    @Autowired
    BusinessSupplierMapper businessSupplierMapper;


    private BusinessSupplier findOneById(BigInteger id) {
        //now only one
        List<BusinessSupplier> list = findAll();
        if (null == list || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public SupplierVO getSupplier(BigInteger id) {
        BusinessSupplier supplier = findOneById(id);
        if (null == supplier) {
            return new SupplierVO();
        }
        SupplierVO result = new SupplierVO();
        BeanUtils.copyProperties(supplier, result);
        return result;
    }

    public Boolean updateSupplier(BigInteger id, SupplierVO supplierVO) {
        BusinessSupplier supplier = findOneById(id);
        if (null != supplier) {
            supplier.setAliPaySwitch(supplierVO.getAliPaySwitch());
            supplier.setBankSwitch(supplierVO.getBankSwitch());
            supplier.setWeChatSwitch(supplierVO.getWeChatSwitch());
            supplier.setPriceDifferences(supplierVO.getPriceDifferences());
            update(supplier);
            updateCache(supplier.getId());
            return true;
        }
        supplier = new BusinessSupplier();
        supplier.setAliPaySwitch(0);
        supplier.setBankSwitch(0);
        supplier.setWeChatSwitch(0);
        supplier.setHasWeChat(0);
        supplier.setHasAliPay(0);
        supplier.setHasBank(0);
        supplier.setUserId(id);
        supplier.setPriceDifferences(supplierVO.getPriceDifferences());
        save(supplier);
        updateCache(supplier.getId());
        return true;
    }

    public Boolean updateHasStatus(BigInteger userId, Integer paymentType) {
        BusinessSupplier supplier = findOneById(userId);
        if (null != supplier) {
            return true;
        }
        //1 credit card; 2 Alipay 3 WeChat
        supplier = new BusinessSupplier();
        supplier.setAliPaySwitch(0);
        supplier.setBankSwitch(0);
        supplier.setWeChatSwitch(0);
        supplier.setHasWeChat(paymentType == 3 ? 1 : 0);
        supplier.setHasAliPay(paymentType == 2 ? 1 : 0);
        supplier.setHasBank(paymentType == 1 ? 1 : 0);
        supplier.setUserId(userId);
        supplier.setPriceDifferences(0F);
        save(supplier);
        updateCache(supplier.getId());
        return true;
    }

}
