package com.enjoyhome.service;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-20 上午12:09
 */

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.NursingProjectDto;
import com.enjoyhome.vo.NursingProjectVo;
import org.springframework.stereotype.Service;

/**
 * 护理项目服务Service
 */
@Service
public interface NursingProjectService {
    /**
     * 分页查询护理项目
     *
     * @param name
     * @param status
     * @param page
     * @param size
     * @return
     */
    PageResponse<NursingProjectVo> getByPage(String name, Integer status, Integer page, Integer size);


    /**
     * 添加护理项目
     *
     * @param nursingProjectDto
     */
    void add(NursingProjectDto nursingProjectDto);


    /**
     * 根据id查询护理项目
     *
     * @param id
     */
    NursingProjectVo getById(Long id);


    /**
     * 更新护理项目
     *
     * @param nursingProjectDto
     */
    void update(NursingProjectDto nursingProjectDto);

    /**
     * 更新护理项目状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除护理项目
     *
     * @param id
     */
    void deleteById(Long id);
}
