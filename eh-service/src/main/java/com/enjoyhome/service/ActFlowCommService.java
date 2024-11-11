package com.enjoyhome.service;

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.PendingTasksDto;
import com.enjoyhome.entity.*;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * @author tensei
 */
public interface ActFlowCommService {


    /**
     * 获取当前办理人id
     *
     * @param formKey      流程定义key
     * @param bussinessKey 业务key
     * @return
     */
    Long getNextAssignee(String formKey, String bussinessKey);


    /**
     * 部署流程定义
     */
    void saveNewDeploy(FlowInfo flowInfo);

    /**
     * 启动流程实例
     */
    ProcessInstance startProcess(String formKey, Map<String, Object> variables, String bussinessKey, Long id);

    /**
     * 查看个人任务列表
     */
    List<Map<String, Object>> myTaskList(String userid);

    /**
     * 查看个人任务信息
     *
     * @param pendingTasksDto
     */
    PageResponse<PendingTasks> myTaskInfoList(PendingTasksDto pendingTasksDto);


    /**
     * 完成任务
     *
     * @param title
     * @param taskId
     * @param userId
     * @param code   网关的条件（ops的值、ops=1审核通过，ops>1审核拒绝）
     * @param status
     */
    void completeProcess(String title, String taskId, String userId, Integer code, Integer status);

    void start(Long id, String formKey, User user, Map<String, Object> variables, boolean autoFinished);

    /**
     * 关闭 思路：改变流程当前节点的下一个节点为空 并完成这个节点的任务，并删除痕迹
     *
     * @param taskId 任务id
     * @param status 状态 1申请中 2已完成 3已关闭
     */
    void closeProcess(String taskId, Integer status);

    /**
     * 是否查看当前审核用户的任务
     *
     * @param taskId
     * @param status
     * @param checkIn
     * @return
     */
    Integer isCurrentUserAndStep(String taskId, Integer status, CheckIn checkIn);


    /**
     * 是否查看当前审核用户的任务
     *
     * @param taskId
     * @param status
     * @param retreat
     * @return
     */
    Integer isCurrentUserAndStep(String taskId, Integer status, Retreat retreat);


    /**
     * 驳回任务
     *
     * @param taskId
     * @param first  是否默认退回流程第一个节点，true 是,false默认是上一个节点，
     */
    void rollBackTask(String taskId, boolean first);

    /**
     * 撤回任务
     *
     * @param taskId
     * @param first  是否默认退回流程第一个节点，true 是,false默认是上一个节点，
     */
    void withdrawTask(String taskId, boolean first);

    /**
     * 跳转任务
     *
     * @param taskId
     * @param first  是否默认跳转流程第一个节点，true 是,false默认是上一个节点，
     */
    void jumpTask(String taskId, boolean first);

    /**
     * 跳转任意节点
     *
     * @param taskId 当前操作节点
     * @param first  是否默认第一 是否驳回
     */
    void anyJump(String taskId, boolean first);

}
