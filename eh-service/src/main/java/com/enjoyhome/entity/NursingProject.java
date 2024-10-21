package com.enjoyhome.entity;

import com.enjoyhome.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-19 下午11:52
 */
@Data
@ApiModel("护理项目实体")
public class NursingProject extends BaseEntity {

    /**
     * 护理名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty("排序号")
    private Integer orderNo;

    @ApiModelProperty("单位")
    private String unit;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 图片路径
     */
    @ApiModelProperty("图片")
    private String image;

    /**
     * 护理要求
     */
    @ApiModelProperty("护理要求")
    private String nursingRequirement;

    /**
     * 状态 （0：禁用，1：启用）
     */
    @ApiModelProperty("状态（0：禁用，1：启用）")
    private Integer status;
}