package com.enjoyhome.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 入住的另一个实体、只包含家属信息和照片
 *
 * @author tensei
 */
@Data
@ApiModel(description = "入住实体类")
public class CheckInOtherDto {

    /**
     * 家属信息
     */
    @ApiModelProperty(value = "家属信息")
    private List<MemberElderDto> memberElderDtos;

    /**
     * 一寸照片
     */
    @ApiModelProperty(value = "一寸照片")
    private String url1;

    /**
     * 身份证人像面
     */
    @ApiModelProperty(value = "身份证人像面")
    private String url2;

    /**
     * 身份证国徽面
     */
    @ApiModelProperty(value = "身份证国徽面")
    private String url3;


}
