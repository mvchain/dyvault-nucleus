package com.mvc.dyvault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.dashboard.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@RestController
@RequestMapping("channel")
@Api(tags = "channel api")
public class ChannelController extends BaseController {

    @Autowired
    ChannelService channelService;

    @ApiOperation("search app channel list")
    @GetMapping()
    public Result<PageInfo<AppChannel>> getChannels(@ModelAttribute @Valid PageDTO pageDTO) {
        PageInfo<AppChannel> result = channelService.getChannels(pageDTO);
        return new Result<>(result);
    }

    @ApiOperation("save app channel")
    @PostMapping()
    public Result<Boolean> save(@RequestBody AppChannel appChannel) {
        Boolean result = channelService.save(appChannel);
        return new Result<>(result);
    }

}
