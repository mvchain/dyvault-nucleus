package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.dto.BusinessSearchDTO;
import com.mvc.dyvault.common.bean.vo.BusinessDetailVO;
import com.mvc.dyvault.common.bean.vo.BusinessSimpleVO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.BusinessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/3/22 15:10
 */
@RestController
@RequestMapping("business")
public class BusinessController extends BaseController {

    @Autowired
    BusinessTransactionService businessService;

    @GetMapping("")
    public Result<List<BusinessSimpleVO>> getBusinessList(@RequestParam("userId") BigInteger userId, @ModelAttribute BusinessSearchDTO pageDTO) {
        List<BusinessSimpleVO> result = businessService.getBusinessList(pageDTO, userId);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<BusinessDetailVO> getBusiness(@PathVariable BigInteger id, @RequestParam("userId") BigInteger userId) {
        BusinessDetailVO result = businessService.getBusiness(id, userId);
        return new Result<>(result);
    }

    @PutMapping("{id}")
    public Result<Boolean> confirmOrder(@RequestParam BigInteger userId, @PathVariable BigInteger id) {
        Boolean result = businessService.confirmOrderComplete(userId, id);
        return new Result<>(result);
    }
}
