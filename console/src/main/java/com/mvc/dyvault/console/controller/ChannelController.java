package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("channel")
public class ChannelController extends BaseController {

    @Autowired
    AppChannelService appChannelService;

    @GetMapping()
    Result<List<AppChannel>> getChannel(@RequestParam(required = false) BigInteger id, @ModelAttribute PageDTO pageDTO) {
        List<AppChannel> result = appChannelService.getChannel(id, pageDTO);
        return new Result<>(result);
    }

}
