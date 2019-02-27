package com.mvc.dyvault.app.service;

import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.AppInfo;
import com.mvc.dyvault.common.bean.dto.AssertVisibleDTO;
import com.mvc.dyvault.common.bean.dto.DebitDTO;
import com.mvc.dyvault.common.bean.dto.TransactionDTO;
import com.mvc.dyvault.common.bean.dto.TransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.*;
import com.mvc.dyvault.common.util.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AppService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;

    public AppInfo getApp(String appType) {
        Result<AppInfo> appInfoResult = consoleRemoteService.getApp(appType);
        return appInfoResult.getData();
    }

}
