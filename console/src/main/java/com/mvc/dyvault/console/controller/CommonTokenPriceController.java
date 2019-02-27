package com.mvc.dyvault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.CommonTokenPrice;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.CommonTokenPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiyichen
 * @create 2018/11/13 15:34
 */
@RestController
@RequestMapping("commonTokenPrice")
public class CommonTokenPriceController extends BaseController {

    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @GetMapping
    public Result<PageInfo<CommonTokenPrice>> getAll(PageDTO pageDTO) {
        return new Result<>(new PageInfo<>(commonTokenPriceService.findAll()));
    }

}
