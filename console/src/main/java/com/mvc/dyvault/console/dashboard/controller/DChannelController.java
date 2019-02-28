package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("dashboard/channel")
public class DChannelController extends BaseController {

    @Autowired
    AppChannelService appChannelService;

    @GetMapping()
    Result<PageInfo<AppChannel>> getChannel(@ModelAttribute PageDTO pageDTO) {
        Condition condition = new Condition(AppChannel.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        List<AppChannel> result = appChannelService.findByCondition(condition);
        return new Result<>(new PageInfo<>(result));
    }

    @PostMapping()
    Result<Boolean> saveChannel(@RequestBody AppChannel appChannel) {
        appChannel.setUpdatedAt(System.currentTimeMillis());
        if (null == appChannel.getId() || BigInteger.ZERO.equals(appChannel.getId())) {
            appChannel.setCreatedAt(System.currentTimeMillis());
            appChannelService.save(appChannel);
        } else {
            appChannelService.update(appChannel);
            appChannelService.updateAllCache("id desc");
        }
        return new Result<>(true);
    }

    @DeleteMapping("{id}")
    Result<Boolean> delChannel(@PathVariable BigInteger id) {
        appChannelService.deleteById(id);
        return new Result<>(true);
    }
}
