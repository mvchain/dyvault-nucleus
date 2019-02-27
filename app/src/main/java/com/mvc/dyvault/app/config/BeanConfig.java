package com.mvc.dyvault.app.config;

import com.mvc.dyvault.app.util.GeetestLib;
import com.mvc.dyvault.common.util.JwtHelper;
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

    @Bean
    JwtHelper jwtHelper2() {
        JwtHelper.serviceName = serviceName;
        JwtHelper.expire = expire;
        JwtHelper.refresh = refresh;
        JwtHelper.base64Secret = base64Secret;
        return new JwtHelper();
    }

    @Bean
    GeetestLib geetestLib() {
        GeetestLib gtSdk = new GeetestLib(captchaId, privateKey, true);
        return gtSdk;
    }

}
