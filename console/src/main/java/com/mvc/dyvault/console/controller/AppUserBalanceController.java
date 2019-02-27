package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.dto.AssertVisibleDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenBalanceVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppUserBalanceService;
import com.mvc.dyvault.console.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:38
 */
@RestController
@RequestMapping("appUserBalance")
public class AppUserBalanceController extends BaseController {

    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppUserService appUserService;

    @GetMapping("{userId}")
    public Result<List<TokenBalanceVO>> getAsset(@PathVariable("userId") BigInteger userId) {
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(userId, false);
        return new Result<>(list);
    }

    @GetMapping("{userId}/{tokenId}")
    public Result<TokenBalanceVO> getAsset(@PathVariable("userId") BigInteger userId, @PathVariable("tokenId") BigInteger tokenId) {
        TokenBalanceVO list = appUserBalanceService.getAsset(userId, tokenId, false);
        return new Result<>(list);
    }

    @PutMapping("{userId}")
    public Result<Boolean> setAssetVisible(@RequestBody @Valid AssertVisibleDTO visibleDTO, @PathVariable("userId") BigInteger userId) {
        appUserBalanceService.setAssetVisible(visibleDTO, userId);
        return new Result<>(true);
    }

    @GetMapping("sum/{userId}")
    public Result<BigDecimal> getBalance(@PathVariable("userId") BigInteger userId) {
        //TODO 修改统计方式为缓存方式
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(userId, true);
        BigDecimal sum = list.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Result<>(sum);
    }

}
