package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.dto.TransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TransactionDetailVO;
import com.mvc.dyvault.common.bean.vo.TransactionSimpleVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.AppOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:39
 */
@RestController
@RequestMapping("appOrder")
public class AppOrderController extends BaseController {

    @Autowired
    AppOrderService appOrderService;

    /**
     * TODO 数据量量庞大,需要修改为缓存部分数据+搜索引擎（根据测试结果）
     *
     * @param userId
     * @param transactionSearchDTO
     * @return
     */
    @GetMapping("user")
    public Result<List<TransactionSimpleVO>> getTransactions(@RequestParam("userId") BigInteger userId, @ModelAttribute TransactionSearchDTO transactionSearchDTO) {
        List<TransactionSimpleVO> result = appOrderService.getTransactions(userId, transactionSearchDTO);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<TransactionDetailVO> getTransaction(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id) {
        TransactionDetailVO vo = appOrderService.getDetail(userId, id);
        return new Result<>(vo);
    }

}
