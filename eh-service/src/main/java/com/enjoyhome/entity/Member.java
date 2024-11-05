package com.enjoyhome.entity;

import com.enjoyhome.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-11-05 上午10:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * c端-小程序用户
 */
public class Member extends BaseEntity {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * openId
     *
     * @attention 统一用户不同小程序openId不同
     */
    private String openId;

    /**
     * 性别
     * 0-未知
     * 1-男
     * 2-女
     */
    private int gender;
}
