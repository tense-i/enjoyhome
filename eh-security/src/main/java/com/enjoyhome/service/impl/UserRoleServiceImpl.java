package com.enjoyhome.service.impl;

import com.enjoyhome.entity.UserRole;
import com.enjoyhome.mapper.UserRoleMapper;
import com.enjoyhome.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关联表服务实现类
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public boolean deleteUserRoleByUserId(Long userId) {
        int flag = userRoleMapper.deleteUserRoleByUserId(userId);
        if (flag>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserRoleInUserId(List<Long> userIds) {
        int flag = userRoleMapper.deleteUserRoleInUserId(userIds);
        if (flag>0){
            return true;
        }
        return false;
    }

    @Override
    public int batchInsert(List<UserRole> userRoles) {
        return userRoleMapper.batchInsert(userRoles);
    }
}
