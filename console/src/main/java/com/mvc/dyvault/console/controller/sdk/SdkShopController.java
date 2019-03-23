package com.mvc.dyvault.console.controller.sdk;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/12 14:35
 */
@RequestMapping("sdk/shop")
@RestController
public class SdkShopController extends BaseController {

    @Autowired
    ShopService shopService;

    @GetMapping("{id}")
    public Result<List<PaymentVO>> getPaymentDetail(@PathVariable("id") BigInteger id) {
        List<PaymentVO> result = shopService.getPaymentDetail(id);
        return new Result<>(result);
    }
}