package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.CommonTokenMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonTokenService extends AbstractService<CommonToken> implements BaseService<CommonToken> {

    @Autowired
    CommonTokenMapper commonTokenMapper;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonTokenControlService commonTokenControlService;
    @Autowired
    AppUserBalanceService appUserBalanceService;

    public DTokenSettingVO getTokenSetting(BigInteger id) {
        DTokenSettingVO result = new DTokenSettingVO();
        CommonToken token = findById(id);
        BeanUtils.copyProperties(token, result);
        result.setInner(StringUtils.isBlank(token.getTokenContractAddress()) && (null == token.getTokenDecimal() || 0 == token.getTokenDecimal()) ? 1 : 0);
        return result;
    }

    public PageInfo<DTokenSettingVO> getTokenSettings(PageDTO pageDTO, String tokenName) {
        Condition condition = new Condition(CommonToken.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(pageDTO.getPageSize(), pageDTO.getPageNum(), pageDTO.getOrderBy());
        ConditionUtil.andCondition(criteria, "token_name = ", tokenName);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<CommonToken> list = findByCondition(condition);
        List<DTokenSettingVO> vos = new ArrayList<>(list.size());
        for (CommonToken commonToken : list) {
            DTokenSettingVO vo = new DTokenSettingVO();
            BeanUtils.copyProperties(commonToken, vo);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public void tokenSetting(DTokenSettingVO dto) {
        CommonToken token = new CommonToken();
        BeanUtils.copyProperties(dto, token);
        update(token);
    }

    public String getTokenName(BigInteger tokenId) {
        CommonToken token = findById(tokenId);
        return token.getTokenName();
    }

}