package com.mvc.dyvault.console.controller;

import com.mvc.dyvault.common.bean.dto.TransactionDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.BlockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:42
 */
@RestController
@RequestMapping("blockTransaction")
public class BlockTransactionController extends BaseController {

    @Autowired
    BlockTransactionService blockTransactionService;

    @PostMapping("{userId}")
    public Result<Boolean> sendTransaction(@PathVariable("userId") BigInteger userId, @RequestBody TransactionDTO transactionDTO) {
        blockTransactionService.sendTransaction(userId, transactionDTO);
        return new Result<>(true);
    }

}
