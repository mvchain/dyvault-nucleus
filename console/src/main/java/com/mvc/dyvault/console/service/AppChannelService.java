package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.dyvault.common.bean.AppChannel;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.AppChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppChannelService extends AbstractService<AppChannel> implements BaseService<AppChannel> {

    @Autowired
    AppChannelMapper appChannelMapper;

    public List<AppChannel> getChannel(BigInteger id, PageDTO pageDTO) {
        Condition condition = new Condition(AppChannel.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        if (null != id && !BigInteger.ZERO.equals(id)) {
            ConditionUtil.andCondition(criteria, "id < ", id);
        }
        return appChannelMapper.selectByCondition(condition);
    }

}