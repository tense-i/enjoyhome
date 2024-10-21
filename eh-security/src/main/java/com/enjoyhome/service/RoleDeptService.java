package com.enjoyhome.service;

import com.enjoyhome.entity.RoleDept;

import java.util.List;

/**
 *  角色部门关联表
 */
public interface RoleDeptService {

    /***
     *  批量保存
     * @param roleDeptList
     * @return
     */
    int batchInsert(List<RoleDept> roleDeptList);

    /***
     *  删除角色对应数据权限
     * @param roleId
     * @return
     */
    Boolean deleteRoleDeptByRoleId(Long roleId);

    /***
     *  批量删除
     * @param roleIds
     * @return
     */
    Boolean deleteRoleDeptInRoleId(List<Long> roleIds);

}
