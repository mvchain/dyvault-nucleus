package com.mvc.dyvault.sdk.util;

import com.mvc.dyvault.common.constant.RedisConstant;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class SmsSender {

    public static Queue<String> queue = new ConcurrentLinkedQueue<>();
    @Autowired
    StringRedisTemplate redisTemplate;
    private final static Long EXPIRE = 10L;
    @Value("${white.account}")
    String[] whiteAccount;

    static {
    }

    @Async
    public void send(String phone) {
        queue.add(phone);
    }

    public Boolean sendSms(String phone) {
        try {
            String code = getVerifyCode(6);
            String messageBody = "【牛视科技】您的验证码是" + code + "!";
            Message message = Message.creator(new PhoneNumber("+86" + phone), new PhoneNumber("+12018907929"), messageBody).create();
            String key = RedisConstant.SMS_VALI_PRE + phone;
            redisTemplate.opsForHash().put(key, "CODE", String.valueOf(code));
            redisTemplate.expire(key, EXPIRE, TimeUnit.MINUTES);
            log.info("send sms:" + phone + ",result:" + message.getStatus());
            return true;
        } catch (Exception e) {
            log.warn("send sms error", e.getMessage());
        }
        return false;
    }

    public Boolean checkSmsValidCode(String phone, String code) {
        String key = RedisConstant.SMS_VALI_PRE + phone;
        String valiCode = "" + redisTemplate.opsForHash().get(key, "CODE");
        if (Arrays.asList(whiteAccount).contains(phone) && "555666".equalsIgnoreCase(code)) {
            return true;
        }
        if (ObjectUtils.equals(valiCode, code) && !"null".equalsIgnoreCase(valiCode)) {
            return true;
        }
        return false;
    }

    public static String getVerifyCode(int digit) {
        Random random = new Random();
        String code = "";
        while (digit > 0) {
            code += random.nextInt(10);
            digit--;
        }
        return code;
    }

}