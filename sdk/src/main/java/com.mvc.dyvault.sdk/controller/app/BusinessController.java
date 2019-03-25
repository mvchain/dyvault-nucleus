package com.mvc.dyvault.sdk.controller.app;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import com.mvc.dyvault.sdk.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "order controller")
@RestController
@RequestMapping("order")
public class BusinessController extends BaseController {

    @ApiOperation("check the order complete or not")
    @GetMapping("exist")
    public Result<OrderDetailVO> checkOrderExist() {
        OrderDetailVO result = businessService.checkOrderExist(getUserId());
        return new Result<>(result);
    }

    @ApiOperation("confirm order")
    @PostMapping()
    public Result<BigInteger> confirmOrder(@RequestBody ConfirmOrderDTO confirmOrderDTO) {
        BigInteger result = businessService.confirmOrder(getUserId(), confirmOrderDTO);
        return new Result<>(result);
    }


    @ApiOperation("update status order(complete, pay, cancel)")
    @PostMapping("{id}/status")
    public Result<Boolean> updateStatus(@PathVariable BigInteger id, @RequestParam @ApiParam("1.pay 2.complete 4.cancel") Integer status ,  @RequestParam(value = "payAccount", required = false) String payAccount) {
        Boolean result = businessService.updateStatus(getUserId(), id, status, payAccount);
        return new Result<>(result);
    }

    @ApiOperation("order detail")
    @GetMapping("{id}")
    public Result<OrderDetailVO> getDetail(@PathVariable BigInteger id) {
        OrderDetailVO result = businessService.getDetail(getUserId(), id);
        return new Result<>(result);
    }

}
