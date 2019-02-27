package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.BlockUsdtWithdrawQueue;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.dao.BlockUsdtWithdrawQueueMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockUsdtWithdrawQueueService extends AbstractService<BlockUsdtWithdrawQueue> implements BaseService<BlockUsdtWithdrawQueue> {

    @Autowired
    BlockUsdtWithdrawQueueMapper blockUsdtWithdrawQueueMapper;

    public void start(String orderId, String fromAddress) {
        if (StringUtils.isNotBlank(orderId) && orderId.equalsIgnoreCase(BusinessConstant.WITHDRAW_USDT_QUEUE)) {
            blockUsdtWithdrawQueueMapper.start(fromAddress);
        }
    }

    public List<BlockUsdtWithdrawQueue> findStart() {
        return blockUsdtWithdrawQueueMapper.findStart(System.currentTimeMillis());
    }
}