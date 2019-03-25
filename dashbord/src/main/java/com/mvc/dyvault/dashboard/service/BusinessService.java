package com.mvc.dyvault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessOrderVO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.DevVO;
import com.mvc.dyvault.common.sdk.vo.ShopVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BusinessService extends BaseService {

    public PageInfo<ShopVO> getShop() {
        Result<PageInfo<ShopVO>> result = remoteService.getShop();
        return result.getData();
    }

    public DevVO getDevSetting(BigInteger id) {
        Result<DevVO> result = remoteService.getDevSetting(id);
        return result.getData();
    }

    public Boolean setDevSetting(BigInteger id, DevDTO devDTO) {
        Result<Boolean> result = remoteService.setDevSetting(id, devDTO);
        return result.getData();
    }

    public PageInfo<BusinessOrderVO> getList(BigInteger id, BusinessTxSearchDTO businessTxSearchDTO) {
        Result<PageInfo<BusinessOrderVO>> result = remoteService.getBusinessList(id, businessTxSearchDTO);
        return result.getData();
    }

    public List<BusinessTxCountVO> getTxCount(BigInteger id, Long startedAt, Long stopAt) {
        Result<List<BusinessTxCountVO>> result = remoteService.getBusinessCount(id, startedAt, stopAt);
        return result.getData();
    }

}
