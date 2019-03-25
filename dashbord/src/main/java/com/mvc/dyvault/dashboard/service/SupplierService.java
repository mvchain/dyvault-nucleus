package com.mvc.dyvault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.bean.dto.BusinessTransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.SupplierVO;
import com.mvc.dyvault.common.sdk.dto.PaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SupplierService extends BaseService {

    public Boolean updatePayment(BigInteger id, PaymentDTO paymentDTO) {
        Result<Boolean> result = remoteService.updatePayment(id, paymentDTO);
        return result.getData();
    }

    public Boolean updateSupplier(BigInteger id, SupplierVO supplierVO) {
        Result<Boolean> result = remoteService.updateSupplier(id, supplierVO);
        return result.getData();
    }

    public SupplierVO getSupplier(BigInteger id) {
        Result<SupplierVO> result = remoteService.getSupplier(id);
        return result.getData();
    }

    public Boolean cancelTx(BigInteger id, BigInteger txId) {
        Result<Boolean> result = remoteService.cancelBusinessTx(id, txId);
        return result.getData();
    }

    public PageInfo<BusinessTransaction> getTxList(BigInteger id, BusinessTransactionSearchDTO businessTransactionSearchDTO) {
        Result<PageInfo<BusinessTransaction>> result = remoteService.getSupplierTxList(id, businessTransactionSearchDTO);
        return result.getData();
    }

}
