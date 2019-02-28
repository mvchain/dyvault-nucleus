package com.mvc.dyvault.app.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.AppMessage;
import com.mvc.dyvault.common.bean.vo.MessageVO;
import com.mvc.dyvault.common.bean.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    ConsoleRemoteService messageRemoteService;

    public List<MessageVO> getlist(BigInteger userId, BigInteger timestamp, Integer pageSize) {
        Result<PageInfo<AppMessage>> listData = messageRemoteService.getlist(userId, timestamp, pageSize);
        List<MessageVO> result = new ArrayList<>(listData.getData().getList().size());
        for (AppMessage message : listData.getData().getList()) {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(message, vo);
            vo.setMessageType(message.getContentType());
            vo.setMessageId(message.getContentId());
            vo.setRead(message.getIsRead());
            result.add(vo);
        }
        return result;
    }

}
