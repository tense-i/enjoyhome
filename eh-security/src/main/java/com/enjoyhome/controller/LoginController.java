package com.enjoyhome.controller;

import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.service.LoginService;
import com.enjoyhome.vo.UserVo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 */
@RestController
@Api(tags = "用户登录")
@RequestMapping("security")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParam(name = "userVo", value = "登录对象", required = true, dataType = "UserVo")
    @ApiOperationSupport(includeParameters = {"userVo.username", "userVo.password"})
    /**
     * @bug 这个接收的参数是UserVo，但是实际上应该是UserDto、但UserDto设计没有password字段，所以这里使用UserVo
     */
    public ResponseResult<UserVo> login(@RequestBody UserVo userVo) {
        return ResponseResult.success(loginService.login(userVo));
    }
}
