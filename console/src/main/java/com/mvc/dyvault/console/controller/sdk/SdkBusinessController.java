package com.mvc.dyvault.console.controller.sdk;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import com.mvc.dyvault.console.bean.ToPayEntity;
import com.mvc.dyvault.console.bean.ToPayResponse;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.BusinessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/12 14:35
 */
@RequestMapping("sdk/business")
@RestController
public class SdkBusinessController extends BaseController {

    @Autowired
    BusinessTransactionService businessTransactionService;

    @GetMapping("exist")
    public Result<OrderDetailVO> checkOrderExist(@RequestParam("userId") BigInteger userId) {
        OrderDetailVO vo = businessTransactionService.checkOrderExist(userId);
        return new Result<>(vo);
    }

    @PostMapping("")
    public Result<BigInteger> confirmOrder(@RequestParam("userId") BigInteger userId, @RequestBody ConfirmOrderDTO confirmOrderDTO) {
        BigInteger result = businessTransactionService.confirmOrder(userId, confirmOrderDTO);
        return new Result<>(result);
    }

    @PostMapping("{id}/status")
    public Result<Boolean> updateStatus(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id, @RequestParam("status") Integer status, @RequestParam(required = false) String payAccount) {
        Boolean result = businessTransactionService.updateStatus(userId, id, status, payAccount);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<OrderDetailVO> getDetail(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id) {
        OrderDetailVO result = businessTransactionService.getDetail(userId, id);
        return new Result<>(result);
    }

    @PostMapping("order")
    public ToPayResponse createOrder(@RequestBody ToPayEntity toPayEntity){
        ToPayResponse response = businessTransactionService.createOrder(toPayEntity);
        return response;
    }

}