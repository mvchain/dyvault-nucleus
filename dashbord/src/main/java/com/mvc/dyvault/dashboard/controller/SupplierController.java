package com.mvc.dyvault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.bean.dto.BusinessTransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.SupplierVO;
import com.mvc.dyvault.common.sdk.dto.PaymentDTO;
import com.mvc.dyvault.dashboard.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "supplier api")
@RestController
@RequestMapping("supplier")
public class SupplierController extends BaseController {

    @Autowired
    private SupplierService supplierService;

    @ApiOperation("payment setting")
    @PutMapping("{id}")
    public Result<Boolean> updatePayment(@PathVariable BigInteger id, @RequestBody PaymentDTO paymentDTO) {
        Boolean result = supplierService.updatePayment(id, paymentDTO);
        return new Result<>(result);
    }

    @ApiOperation("update price differences")
    @PutMapping("{id}/setting")
    public Result<Boolean> updateSupplier(@PathVariable BigInteger id, @RequestBody SupplierVO supplierVO) {
        Boolean result = supplierService.updateSupplier(id, supplierVO);
        return new Result<>(result);
    }

    @ApiOperation("get supplier setting")
    @GetMapping("{id}/setting")
    public Result<SupplierVO> getSupplier(@PathVariable BigInteger id) {
        SupplierVO result = supplierService.getSupplier(id);
        return new Result<>(result);
    }

    @ApiOperation("search Supplier tx")
    @GetMapping("{id}/tx")
    public Result<PageInfo<BusinessTransaction>> getTxList(@PathVariable BigInteger id, @ModelAttribute BusinessTransactionSearchDTO businessTransactionSearchDTO) {
        PageInfo<BusinessTransaction> result = supplierService.getTxList(id, businessTransactionSearchDTO);
        return new Result<>(result);
    }

    @ApiOperation("cancel Supplier tx")
    @PutMapping("{id}/tx/{txId}")
    public Result<Boolean> cancelTx(@PathVariable BigInteger id, @PathVariable BigInteger txId) {
        Boolean result = supplierService.cancelTx(id, txId);
        return new Result<>(result);
    }

}
