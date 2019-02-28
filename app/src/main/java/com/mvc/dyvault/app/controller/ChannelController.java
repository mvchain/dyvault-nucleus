package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.bean.dto.PageDTO;
import com.mvc.dyvault.app.service.ChannelService;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/2/13 15:24
 */
@Api(tags = "Channel Api")
@RequestMapping("channel")
@RestController
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @ApiOperation("get channel list")
    @GetMapping()
    public @ResponseBody
    Result<List<AppChannel>> getChannel(@ApiParam("最后一条记录id,不传或0时从头拉取") @RequestParam(required = false) BigInteger id, @ModelAttribute PageDTO pageDTO) {
        return new Result<>(channelService.getChannel(id, pageDTO));
    }

}
