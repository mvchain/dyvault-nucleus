package com.mvc.dyvault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.dyvault.common.bean.AppMessage;
import com.mvc.dyvault.common.bean.dto.PageDTO;
import com.mvc.dyvault.common.bean.dto.TimeSearchDTO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.console.dao.AppMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AppMessageService {

    private static final String MODEL_TRANSFER = "%s %s %s%s";
    private static final String MODEL_PROJECT = "%s%s %s %s %s";
    private static final String MODEL_PUBLISH = "%s %s %s金额已释放";
    private static final String MODEL_TRADE = "%s 成功%s %s %s";

    @Autowired
    JPushService jPushService;
    @Autowired
    AppMessageMapper appMessageMapper;

    public List<AppMessage> list(BigInteger userId, TimeSearchDTO timeSearchDTO, PageDTO pageDTO) {
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        PageHelper.startPage(1, pageDTO.getPageSize());
        PageHelper.orderBy("created_at desc");
        if (null != timeSearchDTO.getTimestamp() && !BigInteger.ZERO.equals(timeSearchDTO.getTimestamp())) {
            ConditionUtil.andCondition(criteria, "created_at < ", timeSearchDTO.getTimestamp());
        }
        return appMessageMapper.selectByCondition(condition);
    }

    @Async
    public void transferFinancialMsg(String name, BigInteger id, BigInteger userId, BigDecimal value, String tokenName, String message) {
        Boolean result = jPushService.send(message, id, String.valueOf(userId));
        saveMsg(userId, result, id, message);
    }

    @Async
    public void transferMsg(BigInteger orderId, BigInteger userId, BigDecimal value, String tokenName, Integer transferType, Integer status) {
        String transferTypeStr = transferType == 1 ? "收款" : transferType == 2 ? "提现" : "划账";
        String statusStr = status == 2 ? "成功" : "失败";
        String msg = String.format(MODEL_TRANSFER, value.stripTrailingZeros().toPlainString(), tokenName, transferTypeStr, statusStr);
        Boolean result = jPushService.send(msg, orderId, String.valueOf(userId));
        saveMsg(userId, result, orderId, msg);
    }

    private AppMessage saveMsg(BigInteger userId, Boolean result, BigInteger orderId, String msg) {
        Long time = System.currentTimeMillis();
        AppMessage appMessage = new AppMessage();
        appMessage.setIsRead(0);
        appMessage.setUserId(userId);
        appMessage.setUpdatedAt(time);
        appMessage.setStatus(result ? 1 : 0);
        appMessage.setSendFlag(1);
        appMessage.setPushTime(time);
        appMessage.setMessageType(1);
        appMessage.setCreatedAt(time);
        appMessage.setContentType("ORDER");
        appMessage.setContentId(orderId);
        appMessage.setMessage(msg);
        appMessageMapper.insert(appMessage);
        return appMessage;
    }

    public Integer update(AppMessage appMessage) {
        return appMessageMapper.updateByPrimaryKey(appMessage);
    }

    public List<AppMessage> findByCondition(Condition condition) {
        return appMessageMapper.selectByCondition(condition);
    }

    public void transferMsg(BigInteger id, BigInteger userId, String message, Boolean send) {
        if (send) {
//            Boolean result = jPushService.send(message, id, String.valueOf(userId));
            saveMsg(userId, true, id, message);
        } else {
            saveMsg(userId, false, id, message);
        }
    }

}