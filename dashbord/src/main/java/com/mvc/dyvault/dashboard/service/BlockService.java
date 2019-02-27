package com.mvc.dyvault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.BlockSign;
import com.mvc.dyvault.common.bean.CommonAddress;
import com.mvc.dyvault.common.bean.ExportOrders;
import com.mvc.dyvault.common.bean.dto.AdminTransactionDTO;
import com.mvc.dyvault.common.bean.vo.AdminWalletVO;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.SignSumVO;
import com.mvc.dyvault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.dyvault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.dyvault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.dyvault.common.dashboard.bean.vo.DHoldVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BlockService extends BaseService {

    public PageInfo<DBlockeTransactionVO> getTransactions(DBlockeTransactionDTO dBlockeTransactionDTO) {
        Result<PageInfo<DBlockeTransactionVO>> result = remoteService.getTransactions(dBlockeTransactionDTO);
        return result.getData();
    }

    public BigDecimal getBalance(BigInteger tokenId) {
        Result<BigDecimal> result = remoteService.getBalance(tokenId);
        return result.getData();
    }

    public Boolean updateStatus(DBlockStatusDTO dBlockStatusDTO) {
        Result<Boolean> result = remoteService.updateStatus(dBlockStatusDTO);
        return result.getData();
    }

    public Boolean importAddress(List<CommonAddress> list, String fileName) {
        remoteService.importAddress(list, fileName);
        return true;
    }

    public Boolean importSign(List<BlockSign> list, String fileName) {
        remoteService.importSign(list, fileName);
        return true;
    }

    public AdminWalletVO getAdminWallet(BigInteger tokenId) {
        Result<AdminWalletVO> result = remoteService.getAdminWallet(tokenId);
        return result.getData();
    }

    public Boolean buy(AdminTransactionDTO dto) {
        Result<Boolean> result = remoteService.buy(dto);
        return result.getData();
    }

    public List<ExportOrders> exportCollect() {
        Result<List<ExportOrders>> result = remoteService.exportCollect();
        return result.getData();
    }

    public List<SignSumVO> exportSignCount() {
        Result<List<SignSumVO>> result = remoteService.exportSignCount();
        return result.getData();
    }

    public List<ExportOrders> exportSign() {
        Result<List<ExportOrders>> result = remoteService.exportSign();
        return result.getData();
    }
}
