package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.bean.dto.BusinessTransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.SupplierVO;
import com.mvc.dyvault.common.sdk.dto.PaymentDTO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.BusinessShopPaymentService;
import com.mvc.dyvault.console.service.BusinessSupplierService;
import com.mvc.dyvault.console.service.BusinessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/21 16:37
 */
@RestController
@RequestMapping("dashboard/supplier")
public class DSupplierController extends BaseController {

    @Autowired
    BusinessSupplierService businessSupplierService;
    @Autowired
    BusinessShopPaymentService paymentService;
    @Autowired
    BusinessTransactionService transactionService;

    @GetMapping("{id}/tx")
    public Result<PageInfo<BusinessTransaction>> getSupplierTxList(@PathVariable("id") BigInteger id, @RequestBody BusinessTransactionSearchDTO businessTransactionSearchDTO) {
        PageInfo<BusinessTransaction> result = transactionService.getSupplierTxList(id, businessTransactionSearchDTO);
        return new Result<>(result);
    }

    @GetMapping("{id}/cancel")
    public Result<Boolean> cancelBusinessTx(@PathVariable("id") BigInteger id, @PathVariable("txId") BigInteger txId) {
        Boolean result = transactionService.cancel(id, txId);
        return new Result<>(result);
    }

    @GetMapping("{id}/setting")
    public Result<SupplierVO> getSupplier(@PathVariable("id") BigInteger id) {
        SupplierVO result = businessSupplierService.getSupplier(id);
        return new Result<>(result);
    }

    @PostMapping("{id}/setting")
    public Result<Boolean> updateSupplier(@PathVariable("id") BigInteger id, @RequestBody SupplierVO supplierVO) {
        Boolean result = businessSupplierService.updateSupplier(id, supplierVO);
        return new Result<>(result);
    }

    @PostMapping("{id}")
    public Result<Boolean> updatePayment(@PathVariable("id") BigInteger id, @RequestBody PaymentDTO paymentDTO) {
        Boolean result = paymentService.updatePayment(id, paymentDTO);
        return new Result<>(result);
    }

}
