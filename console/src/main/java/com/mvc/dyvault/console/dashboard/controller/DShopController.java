package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.BusinessShop;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessOrderVO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.DevVO;
import com.mvc.dyvault.common.sdk.vo.ShopVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/21 16:37
 */
@RestController
@RequestMapping("dashboard/shop")
public class DShopController extends BaseController {

    @Autowired
    ShopService shopService;

    @GetMapping()
    public Result<PageInfo<ShopVO>> getShop() {
        PageHelper.startPage(1, 999, "id desc");
        List<BusinessShop> list = shopService.findAll();
        List<ShopVO> result = list.stream().map(obj -> {
            ShopVO vo = new ShopVO();
            vo.setCreatedAt(obj.getCreatedAt());
            vo.setShopId(obj.getId());
            vo.setId(obj.getUserId());
            vo.setShopName(obj.getShopName());
            return vo;
        }).collect(Collectors.toList());
        return new Result<>(new PageInfo<>(result));
    }

    @GetMapping("{id}/develop")
    public Result<DevVO> getDevSetting(@PathVariable("id") BigInteger id) {
        DevVO result = shopService.getDevSetting(id);
        return new Result<>(result);
    }

    @PostMapping("{id}/develop")
    public Result<Boolean> setDevSetting(@PathVariable("id") BigInteger id, @RequestBody DevDTO devDTO) {
        Boolean result = shopService.setDevSetting(id, devDTO);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<PageInfo<BusinessOrderVO>> getBusinessList(@PathVariable("id") BigInteger id, @ModelAttribute BusinessTxSearchDTO businessTxSearchDTO) {
        PageInfo<BusinessOrderVO> result = shopService.getBusinessList(id, businessTxSearchDTO);
        return new Result<>(result);
    }

    @GetMapping("{id}/count")
    public Result<List<BusinessTxCountVO>> getBusinessCount(@PathVariable("id") BigInteger id, @RequestParam("startedAt") Long startedAt, @RequestParam("stopAt") Long stopAt) {
        List<BusinessTxCountVO> result = shopService.getBusinessCount(id, startedAt, stopAt);
        return new Result<>(result);
    }

}
