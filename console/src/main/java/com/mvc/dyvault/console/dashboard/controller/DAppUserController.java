package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.UserTypeDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenBalanceVO;
import com.mvc.dyvault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppUserAddressService;
import com.mvc.dyvault.console.service.AppUserBalanceService;
import com.mvc.dyvault.console.service.AppUserService;
import com.mvc.dyvault.console.service.BlockSignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:43
 */
@RestController
@RequestMapping("dashboard/appUser")
public class DAppUserController extends BaseController {
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    BlockSignService blockSignService;
    @Autowired
    AppUserAddressService appUserAddressService;

    @GetMapping("")
    public Result<PageInfo<DUSerVO>> findUser(@ModelAttribute PageDTO pageDTO, @RequestParam(value = "cellphone", required = false) String cellphone, @RequestParam(value = "status", required = false) Integer status) {
        PageInfo<DUSerVO> result = appUserService.findUser(pageDTO, cellphone, status);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<DUSerDetailVO> getUserDetail(@PathVariable("id") BigInteger id) {
        AppUser user = appUserService.findById(id);
        DUSerDetailVO result = new DUSerDetailVO();
        BeanUtils.copyProperties(user, result);
        return new Result<>(result);
    }

    @GetMapping("{id}/balance")
    public Result<List<DUserBalanceVO>> getUserBalance(@PathVariable("id") BigInteger id) {
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(id, true);
        List<DUserBalanceVO> result = new ArrayList<>(list.size());
        for (TokenBalanceVO vo : list) {
            DUserBalanceVO dUserBalanceVO = new DUserBalanceVO();
            BeanUtils.copyProperties(vo, dUserBalanceVO);
            dUserBalanceVO.setBalance(vo.getValue().multiply(vo.getRatio()));
            dUserBalanceVO.setAddress(appUserAddressService.getAddress(id, vo.getTokenId()));
            result.add(dUserBalanceVO);
        }
        return new Result<>(result);
    }

    @GetMapping("{id}/log")
    public Result<PageInfo<DUserLogVO>> getUserLog(@PathVariable("id") BigInteger id, @ModelAttribute @Valid PageDTO pageDTO) {
        PageInfo<DUserLogVO> result = appUserService.getUserLog(id, pageDTO);
        return new Result<>(result);
    }

    @PutMapping("{id}/status")
    public Result<Boolean> updateStatus(@PathVariable BigInteger id, @RequestParam Integer status) {
        Assert.isTrue(status == 1 || status == 0, "状态错误");
        AppUser appUser = new AppUser();
        appUser.setId(id);
        appUser.setUpdatedAt(System.currentTimeMillis());
        appUser.setStatus(status);
        appUserService.update(appUser);
        appUserService.updateCache(id);
        return new Result<>(true);
    }

    @PostMapping("userType")
    public Result<Boolean> updateUserType(@RequestBody UserTypeDTO userTypeDTO) {
        Boolean result = appUserService.updateUserType(userTypeDTO);
        return new Result<>(result);
    }
}
