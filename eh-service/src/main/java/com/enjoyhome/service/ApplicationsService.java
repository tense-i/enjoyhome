package com.enjoyhome.service;


import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.ApplicationsDto;
import com.enjoyhome.vo.ApplicationsVo;

/**
* <p>
* applications Service 接口
* </p>
*
* @author itheima
*/
public interface ApplicationsService {

    /**
     * 分页查询我的申请
     * @param applicationsDto
     * @return
     */
    ResponseResult<ApplicationsVo> selectByPage(ApplicationsDto applicationsDto);

}