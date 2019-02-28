package com.mvc.dyvault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppMessage;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.TimeSearchDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 16:02
 */
@RestController
@RequestMapping("appMessage")
public class AppMessageController extends BaseController {

    @Autowired
    AppMessageService appMessageService;

    /**
     * TODO 根据性能结果缓存或走搜索引擎
     *
     * @param userId
     * @param timeSearchDTO
     * @param pageDTO
     * @return
     */
    @GetMapping()
    public Result<PageInfo<AppMessage>> list(@RequestParam BigInteger userId, @ModelAttribute TimeSearchDTO timeSearchDTO, @ModelAttribute PageDTO pageDTO) {
        List<AppMessage> message = appMessageService.list(userId, timeSearchDTO, pageDTO);
        return new Result<>(new PageInfo<>(message));
    }

}
