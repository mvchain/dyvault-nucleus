package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.service.BusinessService;
import com.mvc.dyvault.common.bean.dto.BusinessSearchDTO;
import com.mvc.dyvault.common.bean.vo.BusinessDetailVO;
import com.mvc.dyvault.common.bean.vo.BusinessSimpleVO;
import com.mvc.dyvault.common.bean.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/2/13 15:24
 */
@Api(tags = "Token business")
@RequestMapping("business")
@RestController
public class BusinessController extends BaseController {

    @Autowired
    BusinessService businessService;

    @ApiOperation("token business list")
    @GetMapping()
    public @ResponseBody
    Result<List<BusinessSimpleVO>> getBusinessList(@ModelAttribute BusinessSearchDTO pageDTO) {
        return new Result<>(businessService.getBusinessList(pageDTO, getUserId()));
    }

    @ApiOperation("token business detail")
    @GetMapping("{id}")
    public @ResponseBody
    Result<BusinessDetailVO> getBusiness(@PathVariable BigInteger id) {
        return new Result<>(businessService.getBusiness(id, getUserId()));
    }

    @ApiOperation("confirm order(balances must be enough)")
    @PutMapping("{id}")
    public Result<Boolean> confirmOrder(@PathVariable BigInteger id) {
        return new Result<>(businessService.confirmOrder(getUserId(), id));
    }


}
