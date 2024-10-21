package com.enjoyhome.dto;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-20 下午8:34
 */

import com.enjoyhome.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("护理项目参数接收实体")
public class NursingProjectDto extends BaseDto {


    @ApiModelProperty("名称")
    private String name;


    @ApiModelProperty("排序号")
    private Integer orderNo;

    @ApiModelProperty("单位")
    private String unit;


    @ApiModelProperty("价格")
    private BigDecimal price;


    @ApiModelProperty("图片")
    private String image;


    @ApiModelProperty("护理要求")
    private String nursingRequirement;

    @ApiModelProperty("状态（0：禁用，1：启用）")
    private Integer status;
}