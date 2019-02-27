package com.mvc.dyvault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.ExportOrders;
import com.mvc.dyvault.common.bean.dto.AdminTransactionDTO;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.SignSumVO;
import com.mvc.dyvault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.dyvault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.dyvault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.dyvault.console.common.BaseController;
import com.mvc.dyvault.console.service.BlockTransactionService;
import com.mvc.dyvault.console.service.CommonAddressService;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:41
 */
@RestController
@RequestMapping("dashboard/blockTransaction")
public class DBlockTransactionController extends BaseController {
    @Autowired
    BlockTransactionService blockTransactionService;
    @Autowired
    CommonAddressService commonAddressService;

    @PutMapping("status")
    public Result<Boolean> updateStatus(@RequestBody DBlockStatusDTO dBlockStatusDTO) {
        blockTransactionService.updateStatus(dBlockStatusDTO);
        return new Result<>(true);
    }

    @GetMapping("")
    public Result<PageInfo<DBlockeTransactionVO>> getTransactions(@ModelAttribute PageDTO pageDTO, @ModelAttribute DBlockeTransactionDTO dBlockeTransactionDTO) {
        PageInfo<DBlockeTransactionVO> result = blockTransactionService.getTransactions(pageDTO, dBlockeTransactionDTO);
        return new Result<>(result);
    }

    @PostMapping("")
    public Result<Boolean> buy(@RequestBody AdminTransactionDTO dto) {
        blockTransactionService.buy(dto);
        return new Result<>(true);
    }
    @GetMapping("collect")
    public Result<List<ExportOrders>> exportCollect() throws IOException, BitcoindException, CommunicationException {
        List<ExportOrders> result = commonAddressService.exportCollect();
        return new Result<>(result);
    }

    @GetMapping("sign")
    public Result<List<ExportOrders>> exportSign() throws IOException, BitcoindException, CommunicationException {
        List<ExportOrders> result = commonAddressService.exportSign();
        return new Result<>(result);
    }

    @GetMapping("signCount")
    public Result<List<SignSumVO>> exportSignCount() throws IOException, BitcoindException, CommunicationException {
        List<SignSumVO> result = commonAddressService.getExportSignSum();
        return new Result<>(result);
    }
}
