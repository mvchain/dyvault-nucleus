package com.mvc.dyvault.console.controller.sdk;

import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiyichen
 * @create 2018/11/12 14:35
 */
@RequestMapping("sdk/user")
@RestController
public class SdkUserController extends BaseController {

    @Autowired
    AppUserService appUserService;

    @GetMapping("cellphone")
    public Result<AppUser> getUserByCellphone(@RequestParam("cellphone") String cellphone) {
        AppUser user = new AppUser();
        user.setCellphone(cellphone);
        AppUser result = appUserService.findOneByEntity(user);
        return new Result<>(result);
    }

    @PostMapping("")
    public Result<AppUser> regUser(@RequestParam("cellphone") String cellphone) {
        AppUser user = appUserService.regUser(cellphone);
        return new Result<>(user);
    }
}