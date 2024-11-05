package com.enjoyhome.controller;

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.NursingProjectDto;
import com.enjoyhome.service.NursingProjectService;
import com.enjoyhome.vo.NursingProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tensei
 * @version 1.0
 * @date 2024-10-19 下午11:56
 */
@RestController
@RequestMapping("/nursing_project")
@Api(tags = "护理项目相关接口")
public class NursingProjectController {
    @Autowired
    private NursingProjectService nursingProjectService;

    @GetMapping
    @ApiOperation("分页查询护理项目")
    public ResponseResult<PageResponse<NursingProjectVo>> getByPage(
            @ApiParam(value = "护理项目名称", required = false)
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "状态", required = false)
            @RequestParam(value = "status", required = false) Integer status,
            @ApiParam(value = "页码", required = false)
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", required = false)
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseResult.success(nursingProjectService.getByPage(name, status, page, size));
    }


    @PostMapping
    @ApiOperation("添加护理项目")
    public ResponseResult addNursingProject(
            @ApiParam(value = "护理项目数据", required = true)
            @RequestBody NursingProjectDto nursingProjectDto) {
        nursingProjectService.add(nursingProjectDto);
        return ResponseResult.success();
    }


    @GetMapping("{id}")
    public ResponseResult<NursingProjectVo> getNursingProjectById(
            @PathVariable Long id) {
        return ResponseResult.success(nursingProjectService.getById(id));
    }

    @PutMapping
    public ResponseResult updateNursingProject(
            @RequestBody NursingProjectDto nursingProjectDto) {
        nursingProjectService.update(nursingProjectDto);
        return ResponseResult.success();
    }


    @PutMapping("/{id}/status/{status}")
    public ResponseResult updateNursingProjectStatus(
            @PathVariable Long id,
            @PathVariable Integer status) {
        nursingProjectService.updateStatus(id, status);
        return ResponseResult.success();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteNursingProject(
            @PathVariable Long id) {
        nursingProjectService.deleteById(id);
        return ResponseResult.success();
    }

}
