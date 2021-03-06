package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.service.TokenService;
import com.mvc.dyvault.common.bean.vo.ExchangeRateVO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenDetailVO;
import com.mvc.dyvault.common.bean.vo.TokenRatioVO;
import com.mvc.dyvault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 令牌相关
 *
 * @author qiyichen
 * @create 2018/11/7 11:02
 */

@Api(tags = "令牌相关")
@RequestMapping("token")
@RestController
public class TokenController extends BaseController {

    @Autowired
    TokenService tokenService;

    @ApiOperation("获取币种列表")
    @GetMapping
    @SwaggerMock("${token.all}")
    public Result<List<TokenDetailVO>> getTokens() throws Exception {
        return new Result<>(tokenService.getTokens(null));
    }

    @ApiOperation("获取币种比值,用于计算资产总值.以CNY为基础货币.建议缓存")
    @GetMapping("base")
    @SwaggerMock("${token.base}")
    public Result<List<TokenRatioVO>> getBase() {
        return new Result<>(tokenService.getBase());
    }

    @ApiOperation("获取汇率，每12小时刷新.客户端控制调用频率")
    @GetMapping("exchange/rate")
    public Result<List<ExchangeRateVO>> getExchangeRate() {
        return new Result<>(tokenService.getExchangeRate());
    }

}
