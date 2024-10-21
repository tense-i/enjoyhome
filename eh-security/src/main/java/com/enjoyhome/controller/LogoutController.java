package com.enjoyhome.controller;

import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.service.LogoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  退出接口
 */
@RestController
@Api(tags = "用户退出")
@RequestMapping("security")
public class LogoutController {

    @Autowired
    LogoutService logoutService;


    @PostMapping("logout")
    @ApiOperation(value = "用户退出",notes = "用户退出")
    public ResponseResult<Boolean> login(){
        return ResponseResult.success(logoutService.logout());
    }
}
