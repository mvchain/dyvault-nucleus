package com.mvc.dyvault.app.service;

import com.mvc.dyvault.common.constant.RedisConstant;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * mail service
 *
 * @author qiyichen
 * @create 2018/3/14 17:00
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    public static Queue<String> queue = new ConcurrentLinkedQueue<>();
    private final static Long EXPIRE = 10L;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Value("${spring.mail.username}")
    String mail;
    @Value("${spring.mail.sendfrom}")
    String sendfrom;

    @Async
    public void send(String email) {
        queue.add(email);
    }

    public Boolean sendSms(String email) {
        //buytoken@163.com
        SimpleMailMessage message = new SimpleMailMessage();
        String key = RedisConstant.MAIL_VALI_PRE + email;
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        message.setTo(email);
        message.setFrom(sendfrom);
        message.setSubject(String.format("Identifying code"));
        message.setText(String.format("Your Code:%s", code));
        try {
            mailSender.send(message);
            redisTemplate.opsForHash().put(key, "CODE", String.valueOf(code));
            redisTemplate.expire(key, EXPIRE, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean checkSmsValiCode(String email, String code) {
        String key = RedisConstant.MAIL_VALI_PRE + email;
        String valiCode = "" + redisTemplate.opsForHash().get(key, "CODE");
        if (ObjectUtils.equals(valiCode, code)) {
            return true;
        }
        return false;
    }

    public String getMail(String email) {
        String[] arr = email.split("@");
        return arr[0].substring(0, 3) + "****@" + arr[1];
    }

}
