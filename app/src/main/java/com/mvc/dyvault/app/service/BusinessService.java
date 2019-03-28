package com.mvc.dyvault.app.service;

import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.dto.BusinessSearchDTO;
import com.mvc.dyvault.common.bean.vo.BusinessDetailVO;
import com.mvc.dyvault.common.bean.vo.BusinessSimpleVO;
import com.mvc.dyvault.common.bean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;

    public List<BusinessSimpleVO> getBusinessList(BusinessSearchDTO pageDTO, BigInteger userId) {
        Result<List<BusinessSimpleVO>> result = consoleRemoteService.getBusinessList(pageDTO, userId);
        return result.getData();
    }


    public BusinessDetailVO getBusiness(BigInteger id, BigInteger userId) {
        Result<BusinessDetailVO> result = consoleRemoteService.getBusiness(id, userId);
        return result.getData();
    }

    public Boolean confirmOrder(BigInteger userId, BigInteger id) {
        Result<Boolean> result = consoleRemoteService.confirmOrder(userId, id);
        return result.getData();
    }

}
