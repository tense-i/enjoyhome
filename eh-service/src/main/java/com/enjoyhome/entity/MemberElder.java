package com.enjoyhome.entity;

import com.enjoyhome.base.BaseEntity;
import com.enjoyhome.vo.BedVo;
import com.enjoyhome.vo.DeviceVo;
import com.enjoyhome.vo.RoomVo;
import com.enjoyhome.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客户老人关联实体类
 */
@Data
public class MemberElder extends BaseEntity {

    /**
     * 客户id
     */
    private Long memberId;

    /**
     * 老人id
     */
    private Long elderId;

    private ElderVo elderVo;

    private BedVo bedVo;

    private RoomVo roomVo;

    @ApiModelProperty(value = "绑定的智能设备id列表")
    private List<DeviceVo> deviceVos;


}

