package com.mvc.dyvault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.dyvault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DTokenVO;
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
public class TokenService extends BaseService {


    public List<DTokenVO> findTokens(String tokenName, Integer blockType) {
        Result<List<DTokenVO>> result = remoteService.findTokens(tokenName, blockType);
        return result.getData();
    }

    public Boolean newToken(DTokenDTO dTokenDTO) {
        Result<Boolean> result = remoteService.newToken(dTokenDTO);
        return result.getData();
    }

    public Boolean updateToken(DTokenDTO dTokenDTO) {
        Result<Boolean> result = remoteService.updateToken(dTokenDTO);
        return result.getData();
    }

    public Boolean tokenSetting(DTokenSettingVO dto) {
        Result<Boolean> result = remoteService.tokenSetting(dto);
        return result.getData();
    }

    public PageInfo<DTokenSettingVO> getTokenSettings(String tokenName, PageDTO pageDTO) {
        Result<PageInfo<DTokenSettingVO>> result = remoteService.getTokenSettings(pageDTO, tokenName);
        return result.getData();
    }

    public DTokenSettingVO getTokenSetting(BigInteger id) {
        Result<DTokenSettingVO> result = remoteService.getTokenSetting(id);
        return result.getData();
    }


    public DTokenDTO getToken(BigInteger id) {
        Result<DTokenDTO> result = remoteService.getToken(id);
        return result.getData();
    }

}
