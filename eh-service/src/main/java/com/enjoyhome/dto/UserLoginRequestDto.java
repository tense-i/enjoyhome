package com.enjoyhome.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * C端用户登录
 */
@Data
public class UserLoginRequestDto {

    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 小程序wx.login()返回的code
     */
    @ApiModelProperty("登录临时凭证")
    private String code;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号临时凭证")
    private String phoneCode;
}
