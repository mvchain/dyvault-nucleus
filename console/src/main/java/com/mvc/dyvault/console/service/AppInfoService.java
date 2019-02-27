package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.AppInfo;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.AppInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppInfoService extends AbstractService<AppInfo> implements BaseService<AppInfo> {

    @Autowired
    private AppInfoMapper appInfoMapper;

}