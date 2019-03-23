package com.mvc.dyvault.sdk.service;

import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("sdk/business/exist")
    Result<OrderDetailVO> checkOrderExist(@RequestParam("userId") BigInteger userId);

    @PostMapping("sdk/business")
    Result<BigInteger> confirmOrder(@RequestParam("userId") BigInteger userId, @RequestBody ConfirmOrderDTO confirmOrderDTO);

    @PostMapping("sdk/business/{id}/status")
    Result<Boolean> updateStatus(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id, @RequestParam("status") Integer status, @RequestParam(value = "payAccount", required = false) String payAccount);

    @GetMapping("sdk/business/{id}")
    Result<OrderDetailVO> getDetail(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("sdk/shop/{id}")
    Result<List<PaymentVO>> getPaymentDetail(@PathVariable("id") BigInteger id);

    @GetMapping("sdk/user/cellphone")
    Result<AppUser> getUserByCellphone(@RequestParam("cellphone") String cellphone);

    @PostMapping("sdk/user")
    Result<AppUser> regUser(@RequestParam("cellphone") String cellphone);
}
