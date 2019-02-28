package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.TimeSearchDTO;
import com.mvc.dyvault.common.bean.vo.MessageVO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * 消息相关
 *
 * @author qiyichen
 * @create 2018/11/8 14:23
 */
@RestController
@Api(tags = "消息相关")
@RequestMapping("message")
public class MessageController extends BaseController {

    @ApiOperation("传入时间戳获取通知信息,本地需要保存已读状态,服务器已读状态变更优先度很低")
    @GetMapping
    @SwaggerMock("${message.list}")
    public Result<List<MessageVO>> getlist(@ModelAttribute TimeSearchDTO timeSearchDTO, @ModelAttribute PageDTO pageDTO) {
        BigInteger userId = getUserId();
        List<MessageVO> list = messageService.getlist(userId, timeSearchDTO.getTimestamp(), pageDTO.getPageSize());
        return new Result<>(list);
    }

}
