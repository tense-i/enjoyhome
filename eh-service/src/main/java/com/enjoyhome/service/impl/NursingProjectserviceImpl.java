package com.enjoyhome.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.NursingProjectDto;
import com.enjoyhome.entity.NursingProject;
import com.enjoyhome.mapper.NursingProjectMapper;
import com.enjoyhome.service.NursingProjectService;
import com.enjoyhome.vo.NursingProjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-20 上午12:11
 */
@Service
public class NursingProjectserviceImpl implements NursingProjectService {
    @Autowired
    private NursingProjectMapper nursingProjectMapper;

    @Override
    public PageResponse<NursingProjectVo> getByPage(String name, Integer status, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<NursingProjectVo> nursingProjectVos = nursingProjectMapper.selectByPage(name, status, page, size);
        Page<NursingProjectVo> nursingProjects = (Page<NursingProjectVo>) nursingProjectVos;
        return PageResponse.of(nursingProjects, NursingProjectVo.class);
    }

    @Override
    public void add(NursingProjectDto nursingProjectDto) {
        NursingProject nursingProject = new NursingProject();
        BeanUtils.copyProperties(nursingProjectDto, nursingProject);
        nursingProjectMapper.insertNursingProject(nursingProject);
    }

    @Override
    public NursingProjectVo getById(Long id) {
        NursingProject nursingProject = nursingProjectMapper.selectById(id);
        NursingProjectVo nursingProjectVo = new NursingProjectVo();
        BeanUtils.copyProperties(nursingProject, nursingProjectVo);
        return nursingProjectVo;
    }

    @Override
    public void update(NursingProjectDto nursingProjectDto) {
        NursingProject nursingProject = new NursingProject();
        BeanUtils.copyProperties(nursingProjectDto, nursingProject);
        nursingProjectMapper.updateNursingProject(nursingProject);
    }


    @Override
    public void updateStatus(Long id, Integer status) {
        NursingProject nursingProject = new NursingProject();
        nursingProject.setId(id);
        nursingProject.setStatus(status);
        nursingProjectMapper.updateNursingProject(nursingProject);
    }

    @Override
    public void deleteById(Long id) {
        nursingProjectMapper.delete(id);
    }


}
