package com.enjoyhome.controller;

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.PendingTasksDto;
import com.enjoyhome.entity.PendingTasks;
import com.enjoyhome.service.ActFlowCommService;
import com.enjoyhome.utils.UserThreadLocal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tensei
 */
@RestController
@RequestMapping("/pending_tasks")
@Api(tags = "待办")
public class PendingTasksController extends BaseController {

    @Autowired
    private ActFlowCommService actFlowCommService;

    @PostMapping("/selectByPage")
    @ApiOperation(value = "查询待办", notes = "传入退住对象")
    public ResponseResult<PendingTasks> selectByPage(@RequestBody PendingTasksDto pendingTasksDto) {
        //只查询有当前登录人的任务
        Long userId = UserThreadLocal.getMgtUserId();

        if (pendingTasksDto.getReqType() == 0) {
            // 待办：设置代理人id
            pendingTasksDto.setAssigneeId(userId);
        } else {
            // 我的任务：设置申请人id
            pendingTasksDto.setApplicatId(userId);
        }

        PageResponse<PendingTasks> pendingTasksPageResponse = actFlowCommService.myTaskInfoList(pendingTasksDto);
        return success(pendingTasksPageResponse);
    }
}
