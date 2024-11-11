package com.enjoyhome.vo;

import lombok.Data;

/**
 * @author tensei
 */
@Data
public class UserRoleVo {

    /**
     * 用户真名
     */
    private String userName;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户id
     */
    private Long id;
}
