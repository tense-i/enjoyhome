package com.enjoyhome.intercept;

import com.enjoyhome.constant.SecurityConstant;
import com.enjoyhome.constant.UserCacheConstant;
import com.enjoyhome.properties.JwtTokenManagerProperties;
import com.enjoyhome.utils.EmptyUtil;
import com.enjoyhome.utils.JwtUtil;
import com.enjoyhome.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 多租户放到SubjectContent上下文中
 */
@Component
public class UserTokenIntercept implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从头部中拿到当前userToken（UUID颁发）
        String userToken = request.getHeader(SecurityConstant.USER_TOKEN);
        if (!EmptyUtil.isNullOrEmpty(userToken)) {
            String jwtTokenKey = UserCacheConstant.JWT_TOKEN + userToken;
            String jwtToken = redisTemplate.opsForValue().get(jwtTokenKey);
            if (!EmptyUtil.isNullOrEmpty(jwtToken)) {
                Object userObj =
                        JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtToken).get(
                                "currentUser");
                String currentUser = String.valueOf(userObj);
                //放入当前线程中：用户当前的web直接获得user使用
                UserThreadLocal.setSubject(currentUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        //移除当前线程中的参数
        UserThreadLocal.removeSubject();
    }
}
