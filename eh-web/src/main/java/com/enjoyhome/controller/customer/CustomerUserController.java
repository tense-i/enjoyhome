package com.enjoyhome.controller.customer;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.controller.BaseController;
import com.enjoyhome.dto.UserLoginRequestDto;
import com.enjoyhome.service.DeviceDataService;
import com.enjoyhome.service.MemberService;
import com.enjoyhome.utils.ObjectUtil;
import com.enjoyhome.vo.DeviceDataVo;
import com.enjoyhome.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * 用户管理
 */
@Slf4j
@Api(tags = "客户管理")
@RestController
@RequestMapping("/customer/user")
public class CustomerUserController extends BaseController {

    @Autowired
    private MemberService memberService;

    @Resource
    private DeviceDataService deviceDataService;

    @Resource
    private Client client;

    @Value("${enjoyhome.aliyun.iotInstanceId}")
    private String iotInstanceId;

    /**
     * C端用户登录--微信登录
     *
     * @param userLoginRequestDto 用户登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public ResponseResult<LoginVo> login(@RequestBody UserLoginRequestDto userLoginRequestDto) throws IOException {
        LoginVo login = memberService.login(userLoginRequestDto);
        return success(login);
    }

    @GetMapping("/get-page")
    @ApiOperation(value = "获取设备数据分页结果", notes = "接收包含分页信息的请求参数，返回一个包含分页数据的Page<DeviceDataDto>对象")
    public ResponseResult<PageResponse<DeviceDataVo>> getDeviceDataPage(
            @ApiParam(value = "页码", required = true) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(value = "每页大小", required = true) @RequestParam("pageSize") Integer pageSize,
            @ApiParam(value = "设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
            @ApiParam(value = "接入位置") @RequestParam(value = "accessLocation", required = false) String accessLocation,
            @ApiParam(value = "位置类型") @RequestParam(value = "accessLocation", required = false) Integer locationType,
            @ApiParam(value = "功能ID") @RequestParam(value = "functionId", required = false) String functionId,
            @ApiParam(value = "开始时间") @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) Long endTime,
            @ApiParam(value = "状态 0 正常 1 异常 2待处理 3已处理") @RequestParam(required = false) Integer status) {
        return ResponseResult.success(deviceDataService.getDeviceDataPage(pageNum, pageSize, status, deviceName,
                accessLocation, locationType, functionId, ObjectUtil.isEmpty(startTime) ? null :
                        LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime) ? null :
                        LocalDateTimeUtil.of(endTime)));
    }

    /*@PostMapping("/QueryThingModelPublished")
    @ApiOperation(value = "查看指定产品的已发布物模型中的功能定义详情", notes = "查看指定产品的已发布物模型中的功能定义详情")
    public ResponseResult queryThingModelPublished(@RequestBody QueryThingModelPublishedRequest request) throws
    Exception {
        request.setIotInstanceId("iot-06z00frq8umvkx2");
        QueryThingModelPublishedResponse response = client.queryThingModelPublished(request);
        return ResponseResult.success(response.getBody().getData());
    }*/

    @PostMapping("/QueryDevicePropertyStatus")
    @ApiOperation(value = "查询指定设备的状态", notes = "查询指定设备的状态")
    public ResponseResult<QueryDevicePropertyStatusResponseBody.QueryDevicePropertyStatusResponseBodyData> QueryDevicePropertyStatus(@RequestBody QueryDevicePropertyStatusRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryDevicePropertyStatusResponse deviceStatus = client.queryDevicePropertyStatus(request);
        return ResponseResult.success(deviceStatus.getBody().getData());
    }


    @GetMapping("/get-week-page")
    @ApiOperation(value = "按周获取设备数据分页结果", notes = "接收包含分页信息的请求参数，返回一个包含分页数据的Page<DeviceDataDto>对象")
    public ResponseResult<PageResponse<DeviceDataVo>> getDeviceWeekDataPage(
            @ApiParam(value = "页码", required = true) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(value = "每页大小", required = true) @RequestParam("pageSize") Integer pageSize,
            @ApiParam(value = "设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
            @ApiParam(value = "接入位置") @RequestParam(value = "accessLocation", required = false) String accessLocation,
            @ApiParam(value = "位置类型") @RequestParam(value = "accessLocation", required = false) Integer locationType,
            @ApiParam(value = "功能ID") @RequestParam(value = "functionId", required = false) String functionId,
            @ApiParam(value = "开始时间") @RequestParam(required = false) Long startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) Long endTime,
            @ApiParam(value = "状态 0 正常 1 异常 2待处理 3已处理") @RequestParam(required = false) Integer status) {
        return ResponseResult.success(deviceDataService.getDeviceWeekDataPage(pageNum, pageSize, status, deviceName,
                accessLocation, locationType, functionId, ObjectUtil.isEmpty(startTime) ? null :
                        LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime) ? null :
                        LocalDateTimeUtil.of(endTime)));
    }

}
