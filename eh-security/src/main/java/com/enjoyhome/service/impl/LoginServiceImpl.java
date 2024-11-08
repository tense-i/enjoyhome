package com.enjoyhome.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.enjoyhome.constant.UserCacheConstant;
import com.enjoyhome.properties.JwtTokenManagerProperties;
import com.enjoyhome.service.LoginService;
import com.enjoyhome.service.ResourceService;
import com.enjoyhome.service.RoleDeptService;
import com.enjoyhome.service.RoleService;
import com.enjoyhome.utils.BeanConv;
import com.enjoyhome.utils.JwtUtil;
import com.enjoyhome.vo.ResourceVo;
import com.enjoyhome.vo.RoleVo;
import com.enjoyhome.vo.UserAuth;
import com.enjoyhome.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录接口实现
 */
@Component
public class LoginServiceImpl implements LoginService {

    /**
     * 认证管理器
     */
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Autowired
    RoleDeptService roleDeptService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserVo login(UserVo userVo) {
        //用户认证
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userVo.getUsername(), userVo.getPassword());
        // 认证器
        Authentication authenticate = authenticationManager.authenticate(authentication);

        //认证Authentication对象获得内置对象UserAuth
        UserAuth userAuth = (UserAuth) authenticate.getPrincipal();
        // 脱敏？ 为什么要脱敏？
        userAuth.setPassword("");
        //转换为UserVo
        UserVo userVoResult = BeanConv.toBean(userAuth, UserVo.class);

        //查询当前用户拥有的资源列表，方便授权过滤器进行校验
        List<ResourceVo> resourceVoList = resourceService.findResourceVoListByUserId(userVoResult.getId());
        Set<String> requestPaths = resourceVoList.stream()
                .filter(x -> "r".equals(x.getResourceType()))// 过滤可读资源
                .map(ResourceVo::getRequestPath) // 获取所有可读资源请求路径URL
                .collect(Collectors.toSet());// 去重

        // 当前用户拥有的资源请求路径
        userVoResult.setResourceRequestPaths(requestPaths);

        //用户当前角色列表
        List<RoleVo> roleVoList = roleService.findRoleVoListByUserId(userVoResult.getId());
        Set<String> roleLabels = roleVoList
                .stream()
                .map(RoleVo::getLabel)
                .collect(Collectors.toSet());
        userVoResult.setRoleLabels(roleLabels);

        //userToken令牌颁布
        String userToken = UUID.randomUUID().toString();
        userVoResult.setUserToken(userToken);

        //构建载荷
        Map<String, Object> claims = new HashMap<>();
        String userVoJsonString = JSONObject.toJSONString(userVoResult);
        claims.put("currentUser", userVoJsonString);

        //jwtToken令牌颁布
        String jwtToken = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(),
                jwtTokenManagerProperties.getTtl(), claims);

        //剔除缓存：用户关联userToken
        String userTokenKey = UserCacheConstant.USER_TOKEN + userVo.getUsername();
        long ttl = Long.valueOf(jwtTokenManagerProperties.getTtl()) / 1000;// 毫秒转秒
        //key：username   value:uuid
        // 存储到redis中、请求到达时，通过userTokenKey获取userToken
        redisTemplate.opsForValue().set(userTokenKey, userToken, ttl, TimeUnit.SECONDS);

        //续期缓存：userToken关联jwtToken
        String jwtTokenKey = UserCacheConstant.JWT_TOKEN + userToken;
        //key：uuid   value:jwttoken
        redisTemplate.opsForValue().set(jwtTokenKey, jwtToken, ttl, TimeUnit.SECONDS);

        return userVoResult;
    }
}
