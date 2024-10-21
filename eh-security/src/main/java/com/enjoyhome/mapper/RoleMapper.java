package com.enjoyhome.mapper;

import com.github.pagehelper.Page;
import com.enjoyhome.dto.RoleDto;
import com.enjoyhome.entity.Role;
import com.enjoyhome.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    int insert(Role record);

    int insertSelective(Role record);

    int updateByPrimaryKeySelective(Role record);

    Page<List<Role>> selectPage(@Param("roleDto") RoleDto roleDto);

    List<Role> selectList(@Param("roleVo") RoleVo roleVo);

    List<RoleVo> findRoleVoListInUserId(@Param("userIds") List<Long> userIds);

    List<RoleVo> findRoleVoListByResourceNo(@Param("resourceNo") String resourceNo);

    List<RoleVo> findRoleVoListByUserId(@Param("userId") Long userId);

    int deleteRoleByIds(@Param("roleIds") List<Long> roleIds);
}
