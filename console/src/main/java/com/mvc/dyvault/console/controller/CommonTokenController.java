package com.mvc.dyvault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TransactionTokenVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.service.AppUserBalanceService;
import com.mvc.dyvault.console.service.CommonTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiyichen
 * @create 2018/11/13 11:44
 */
@RestController
@RequestMapping("commonToken")
public class CommonTokenController extends BaseController {

    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    AppUserBalanceService appUserBalanceService;

    @GetMapping()
    public Result<PageInfo<CommonToken>> getTokenList(@ModelAttribute CommonToken commonToken, @ModelAttribute PageDTO pageDTO) {
        List<CommonToken> result = commonTokenService.findAll();
        Stream<CommonToken> stream = result.stream();
        if (null != commonToken.getVisible()) {
            stream = stream.filter(obj -> obj.getVisible().equals(commonToken.getVisible()));
        }
        if (StringUtils.isNotBlank(commonToken.getTokenName())) {
            stream = stream.filter(obj -> obj.getTokenName().equalsIgnoreCase(commonToken.getTokenName()));
        }
        if (null != pageDTO.getUpdatedStartAt()) {
            stream = stream.filter(obj -> obj.getUpdatedAt().compareTo(pageDTO.getUpdatedStartAt()) > 0);
        }
        result = stream.collect(Collectors.toList());
        return new Result<>(new PageInfo<>(result));
    }

    @GetMapping("transactionInfo")
    public Result<TransactionTokenVO> getTransactionInfo(@RequestParam BigInteger userId, @RequestParam("tokenId") BigInteger tokenId) {
        CommonToken token = commonTokenService.findById(tokenId);
        TransactionTokenVO vo = new TransactionTokenVO();
        vo.setBalance(appUserBalanceService.getBalanceByTokenId(userId, tokenId));
        vo.setFee(token.getFee());
        vo.setFeeTokenName(token.getTokenName());
        return new Result<>(vo);
    }

}
