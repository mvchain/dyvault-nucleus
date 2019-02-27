package com.mvc.dyvault.app.config;

import com.mvc.dyvault.common.permission.NotLogin;
import com.mvc.dyvault.common.util.BaseContextHandler;
import com.mvc.dyvault.common.util.JwtHelper;
import com.mvc.dyvault.common.util.MessageConstants;
import com.mvc.dyvault.common.util.TokenErrorException;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author qyc
 */
@Log4j2
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = getAbsUri(request.getRequestURI());
        if (uri.startsWith("null")) {
            response.sendRedirect(uri.replaceFirst("null", "/"));
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader("Authorization");
        Claims claim = JwtHelper.parseJWT(token);
        NotLogin loginAnn = handlerMethod.getMethodAnnotation(NotLogin.class);
        checkAnnotation(claim, loginAnn, request.getRequestURI());
        setUserInfo(claim);
        return super.preHandle(request, response, handler);
    }

    private String getAbsUri(String requestURI) {
        while (requestURI.startsWith("/")) {
            requestURI = requestURI.replaceFirst("/", "");
        }
        return requestURI;
    }

    //校验权限
    private void checkAnnotation(Claims claim, NotLogin loginAnn, String uri) throws LoginException {
        if (null == claim && null == loginAnn) {
            if (uri.indexOf("/refresh") > 0) {
                throw new LoginException(MessageConstants.getMsg("TOKEN_EXPIRE"));
            } else {
                throw new TokenErrorException(MessageConstants.getMsg("TOKEN_EXPIRE"), MessageConstants.TOKEN_EXPIRE_CODE);
            }
        }
        if (null != claim) {
            JwtHelper.check(claim, uri);
        }
    }

    //设置用户信息
    public void setUserInfo(Claims userInfo) {
        if (null != userInfo) {
            String username = userInfo.get("username", String.class);
            Integer userId = userInfo.get("userId", Integer.class);
            BaseContextHandler.set("username", username);
            BaseContextHandler.set("userId", BigInteger.valueOf(userId.longValue()));
        }
    }

}
