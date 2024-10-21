package com.enjoyhome.vo;

import com.enjoyhome.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NursingElderVo extends BaseVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "护理员id")
    private Long nursingId;

    @ApiModelProperty(value = "老人id")
    private Long elderId;
}