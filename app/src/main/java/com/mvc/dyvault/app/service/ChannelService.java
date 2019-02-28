package com.mvc.dyvault.app.service;

import com.mvc.dyvault.app.bean.dto.PageDTO;
import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ChannelService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;


    public List<AppChannel> getChannel(BigInteger id, PageDTO pageDTO) {
        Result<List<AppChannel>> result = consoleRemoteService.getChannel(id, pageDTO);
        return result.getData();
    }
}
