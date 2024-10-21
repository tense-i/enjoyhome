package com.enjoyhome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.ApplicationsDto;
import com.enjoyhome.dto.PendingTasksDto;
import com.enjoyhome.entity.PendingTasks;
import com.enjoyhome.service.ApplicationsService;
import com.enjoyhome.vo.ApplicationsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author itheima
 */
@Service
public class ApplicationsServiceImpl implements ApplicationsService {

    @Autowired
    private ActFlowCommServiceImpl actFlowCommService;

    /**
     * 分页查询我的申请
     * @param applicationsDto
     * @return
     */
    @Override
    public ResponseResult<ApplicationsVo> selectByPage(ApplicationsDto applicationsDto) {
        PendingTasksDto pendingTasksDto = BeanUtil.toBean(applicationsDto, PendingTasksDto.class);
        PageResponse<PendingTasks> pendingTasksPageResponse = actFlowCommService.myTaskInfoList(pendingTasksDto);
        return ResponseResult.success(pendingTasksPageResponse);
    }

}
