package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.CommonPair;
import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.dyvault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DTokenVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.service.BlockHeightService;
import com.mvc.dyvault.console.service.CommonTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/21 16:39
 */
@RestController
@RequestMapping("dashboard/commonToken")
public class DCommonTokenController extends BaseController {
    @Autowired
    BlockHeightService blockHeightService;
    @Autowired
    CommonTokenService commonTokenService;

    @GetMapping("")
    public Result<List<DTokenVO>> findTokens(@RequestParam(value = "tokenName", required = false) String tokenName, @RequestParam(value = "isBlock", required = false) Integer blockType) {
        List<CommonToken> list = null;
        if (StringUtils.isNotBlank(tokenName)) {
            list = commonTokenService.findBy("tokenName", tokenName);
        } else {
            list = commonTokenService.findAll();
        }
        List<DTokenVO> result = new ArrayList<>();
        for (CommonToken token : list) {
            if (null != blockType && BusinessConstant.CLASSIFY_BLOCK.equals(0) && StringUtils.isBlank(token.getTokenType())) {
                //只筛选区块链类型
                continue;
            }
            DTokenVO vo = new DTokenVO();
            vo.setTokenId(token.getId());
            BeanUtils.copyProperties(token, vo);
            vo.setContractAddress(token.getTokenContractAddress());
            result.add(vo);
        }
        return new Result<>(result);
    }

    @PostMapping("")
    public Result<Boolean> newToken(@RequestBody DTokenDTO dTokenDTO) {
        CommonToken token = commonTokenService.findOneBy("tokenName", dTokenDTO.getTokenName());
        Assert.isNull(token, "令牌已存在");
        token = new CommonToken();
        BeanUtils.copyProperties(dTokenDTO, token);
        token.setTokenType(null == dTokenDTO.getBlockType() ? "" : dTokenDTO.getBlockType());
        token.setVisible(0);
        token.setWithdraw(0);
        token.setRecharge(0);
        commonTokenService.save(token);
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(token.getId());
        return new Result<>(true);
    }

    @GetMapping("{id}")
    public Result<DTokenDTO> getToken(@PathVariable BigInteger id) {
        CommonToken token = commonTokenService.findById(id);
        DTokenDTO vo = new DTokenDTO();
        BeanUtils.copyProperties(token, vo);
        vo.setContractAddress(token.getTokenContractAddress());
        vo.setDecimals(token.getTokenDecimal());
        vo.setTokenId(token.getId());
        return new Result<>(vo);
    }

    @PutMapping("")
    public Result<Boolean> updateToken(@RequestBody DTokenDTO dTokenDTO) {
        CommonToken token = new CommonToken();
        BeanUtils.copyProperties(dTokenDTO, token);
        if (dTokenDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
            token.setTokenType("ETH");
        } else if (dTokenDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_USDT) || dTokenDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_BTC)) {
            token.setTokenType("BTC");
        } else {
            token.setTokenType(null == dTokenDTO.getBlockType() ? "" : dTokenDTO.getBlockType());
        }
        token.setId(dTokenDTO.getTokenId());
        token.setTokenContractAddress(dTokenDTO.getContractAddress());
        token.setTokenDecimal(dTokenDTO.getDecimals());
        commonTokenService.update(token);
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(token.getId());
        return new Result<>(true);
    }

    @PutMapping("setting")
    public Result<Boolean> tokenSetting(@RequestBody DTokenSettingVO dto) {
        commonTokenService.tokenSetting(dto);
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(dto.getId());
        return new Result<>(true);
    }

    @GetMapping("setting")
    public Result<PageInfo<DTokenSettingVO>> getTokenSettings(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(value = "tokenName", required = false) String tokenName) {
        PageInfo<DTokenSettingVO> result = commonTokenService.getTokenSettings(pageDTO, tokenName);
        return new Result<>(result);
    }

    @GetMapping("setting/{id}")
    public Result<DTokenSettingVO> getTokenSetting(@PathVariable("id") BigInteger id) {
        DTokenSettingVO result = commonTokenService.getTokenSetting(id);
        return new Result<>(result);
    }
}
