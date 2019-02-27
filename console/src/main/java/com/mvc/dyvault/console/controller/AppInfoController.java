package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.AppInfo;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("appInfo")
public class AppInfoController extends BaseController {

    @Autowired
    AppInfoService appInfoService;

    @PostMapping
    public Result<Boolean> saveAppInfo(@RequestBody AppInfo appInfo) {
        AppInfo app = appInfoService.findOneBy("appType", appInfo.getAppType());
        if (null == app) {
            appInfoService.save(appInfo);
        } else {
            appInfoService.update(appInfo);
        }
        appInfoService.updateCache(appInfo.getAppType());
        return new Result<>(true);
    }

    @GetMapping
    public Result<List<AppInfo>> list() {
        return new Result<>(appInfoService.findAll());
    }

    @GetMapping("{appType}")
    public Result<AppInfo> getApp(@PathVariable("appType") String appType) {
        AppInfo app = appInfoService.findById(appType);
        if (null == app) {
            app = new AppInfo();
        }
        return new Result<>(app);
    }

}
