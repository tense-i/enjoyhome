package com.enjoyhome.mapper;


import com.enjoyhome.entity.NursingProject;
import com.enjoyhome.vo.NursingProjectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-20 上午12:02
 */
@Mapper
public interface NursingProjectMapper {

    /**
     * 分页查询护理项目
     *
     * @param name
     * @param status
     * @param page
     * @param size
     * @return
     */
    List<NursingProjectVo> selectByPage(String name, Integer status, Integer page, Integer size);

    /**
     * 添加护理项目
     *
     * @param nursingProject
     * @return
     */
    int insertNursingProject(NursingProject nursingProject);


    /**
     * 根据id查询护理项目
     *
     * @param id
     */
    NursingProject selectById(Long id);


    /**
     * 更新护理项目
     *
     * @param nursingProject
     */
    int updateNursingProject(NursingProject nursingProject);

    /**
     * 更新护理项目状态
     *
     * @param nursingProject
     */
    int updateNursingProjectStatus(NursingProject nursingProject);


    /**
     * 删除护理项目
     *
     * @param id
     */
    int delete(Long id);
}

