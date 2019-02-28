package com.mvc.dyvault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ChannelService extends BaseService {

    public PageInfo<AppChannel> getChannels(PageDTO pageDTO) {
        Result<PageInfo<AppChannel>> result = remoteService.getChannels(pageDTO);
        return result.getData();
    }

    public Boolean save(AppChannel appChannel) {
        Result<Boolean> result = remoteService.saveChannel(appChannel);
        return result.getData();
    }

    public Boolean delete(BigInteger id) {
        Result<Boolean> result = remoteService.delete(id);
        return result.getData();
    }
}
