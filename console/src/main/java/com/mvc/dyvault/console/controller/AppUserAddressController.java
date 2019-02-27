package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppUserAddressService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:40
 */
@RestController
@RequestMapping("appUserAddress")
public class AppUserAddressController extends BaseController {

    @Autowired
    AppUserAddressService appUserAddressService;

    @GetMapping("{userId}")
    public Result<String> getAddress(@PathVariable("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId) {
        String address = appUserAddressService.getAddress(userId, tokenId);
        return new Result<>(address);
    }

}
