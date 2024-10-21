package com.enjoyhome.mapper;

import com.enjoyhome.entity.Device;
import com.enjoyhome.vo.DeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    int deleteByDeviceId(@Param("deviceId") String id);

    int insert(Device record);

    Device selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Device record);

    List<DeviceVo> selectByDeviceIds(List<String> list);

    List<DeviceVo> selectByLocation(List<String> ids, int type);
}