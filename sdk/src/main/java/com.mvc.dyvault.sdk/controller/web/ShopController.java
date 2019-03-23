package com.mvc.dyvault.sdk.controller.web;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import com.mvc.dyvault.sdk.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/3/23 15:38
 */
@Api(tags = "shop controller")
@RestController
@RequestMapping("shop")
public class ShopController extends BaseController {

    @ApiOperation("get payment method")
    @GetMapping("shop/{shopId}/payment")
    public Result<List<PaymentVO>> getPaymentDetail(@PathVariable BigInteger shopId){
        List<PaymentVO> result = shopService.getPaymentDetail(shopId);
        return new Result<>(result);
    }

}
