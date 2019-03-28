package com.mvc.dyvault.sdk.config;

import com.mvc.dyvault.common.util.JwtHelper;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiyichen
 * @create 2018/11/15 18:16
 */
@Configuration
public class BeanConfig {
    @Value("${service.name}")
    private String serviceName;
    @Value("${service.expire}")
    private Long expire;
    @Value("${service.refresh}")
    private Long refresh;
    @Value("${service.base64Secret}")
    private String base64Secret;
    @Value("${geetest.captchaId}")
    private String captchaId;
    @Value("${geetest.privateKey}")
    private String privateKey;
    @Value("${sms.token}")
    private String token;
    @Value("${sms.sid}")
    private String sid;

    @Bean
    JwtHelper jwtHelper2() {
        JwtHelper.serviceName = serviceName;
        JwtHelper.expire = expire;
        JwtHelper.refresh = refresh;
        JwtHelper.base64Secret = base64Secret;
        Twilio.init(sid, token);
        return new JwtHelper();
    }


}
