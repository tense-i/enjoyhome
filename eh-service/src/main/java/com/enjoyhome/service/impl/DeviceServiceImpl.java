package com.enjoyhome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.DeviceDto;
import com.enjoyhome.entity.Device;
import com.enjoyhome.exception.BaseException;
import com.enjoyhome.mapper.DeviceMapper;
import com.enjoyhome.service.DeviceService;
import com.enjoyhome.utils.ObjectUtil;
import com.enjoyhome.vo.DeviceVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    Client client;

    @Resource
    private DeviceMapper deviceMapper;

    @Value("${enjoyhome.aliyun.iotInstanceId}")
    private String iotInstanceId;

    @Override
    public void registerDevice(DeviceDto deviceDto) throws Exception {
        RegisterDeviceRequest request = deviceDto.getRegisterDeviceRequest();
        request.setIotInstanceId(iotInstanceId);
        // 云端注册请求
        RegisterDeviceResponse response = client.registerDevice(request);

        if (Boolean.TRUE.equals(response.getBody().getSuccess())) {
            // 保存位置
            Device device = BeanUtil.toBean(deviceDto, Device.class);
            device.setDeviceId(response.getBody().getData().getIotId());
            device.setProductId(request.getProductKey());
            device.setNoteName(request.getNickname());


            QueryProductRequest productRequest = new QueryProductRequest();
            productRequest.setIotInstanceId(iotInstanceId);
            productRequest.setProductKey(request.getProductKey());
            QueryProductResponse queryProductResponse = client.queryProduct(productRequest);
            // 产品名称
            String productName = queryProductResponse.getBody().getData().getProductName();
            device.setProductName(productName);
            // 保存位置为老人
            if (device.getLocationType().equals(0)) {
                // 物理位置为空
                device.setPhysicalLocationType(-1);
            }
            try {
                // 插入到数据库中
                deviceMapper.insert(device);
            } catch (Exception e) {
                DeleteDeviceRequest deleteDeviceRequest = new DeleteDeviceRequest();
                deleteDeviceRequest.setIotInstanceId(iotInstanceId);
                deleteDeviceRequest.setDeviceName(device.getDeviceName());
                deleteDeviceRequest.setIotId(device.getDeviceId());
                deleteDeviceRequest.setProductKey(device.getProductId());
                client.deleteDevice(deleteDeviceRequest);
                throw new BaseException("该老人/位置已绑定该产品，请重新选择");
            }
            return;
        }
        throw new BaseException("设备名称已存在，请重新输入");
    }

    @Override
    public void updateDevice(DeviceDto deviceDto) throws Exception {
        BatchUpdateDeviceNicknameRequest request = new BatchUpdateDeviceNicknameRequest();
        request.setIotInstanceId(iotInstanceId);
        BatchUpdateDeviceNicknameRequest.BatchUpdateDeviceNicknameRequestDeviceNicknameInfo nicknameInfo =
                BeanUtil.toBean(deviceDto,
                        BatchUpdateDeviceNicknameRequest.BatchUpdateDeviceNicknameRequestDeviceNicknameInfo.class);
        request.setDeviceNicknameInfo(Lists.newArrayList(nicknameInfo));
        BatchUpdateDeviceNicknameResponse response = client.batchUpdateDeviceNickname(request);
        // 保存位置
        if (Boolean.TRUE.equals(response.getBody().getSuccess())) {
            // 保存位置
            Device device = BeanUtil.toBean(deviceDto, Device.class);
            device.setProductId(deviceDto.getProductKey());
            device.setNoteName(deviceDto.getNickname());
            Device device1 = deviceMapper.selectByPrimaryKey(device.getId());
            if (ObjectUtil.isEmpty(device1)) {
                device.setDeviceId(deviceDto.getIotId());
                device.setNoteName(deviceDto.getNickname());
                device.setProductId(deviceDto.getProductKey());
                device.setDeviceId(deviceDto.getIotId());
                deviceMapper.insert(device);
                return;
            }
            try {
                deviceMapper.updateByPrimaryKeySelective(device);
            } catch (Exception e) {
                throw new BaseException("该老人/位置已绑定该产品，请重新选择");
            }
            return;
        }
        throw new BaseException(response.getBody().getErrorMessage());
    }

    @Override
    public PageResponse<DeviceVo> queryDevice(QueryDeviceRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryDeviceResponse queryDeviceResponse = client.queryDevice(request);
        QueryDeviceResponseBody body = queryDeviceResponse.getBody();
        if (Boolean.TRUE.equals(body.getSuccess()) && body.getData() != null) {
            List<QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo> deviceInfo =
                    body.getData().getDeviceInfo();
            if (CollUtil.isEmpty(deviceInfo)) {
                return null;
            }
            // 所有设备的物联网id
            List<String> list =
                    deviceInfo.stream().map(QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo::getIotId).collect(Collectors.toList());

            // 根据物联网id在本地查询设备信息
            List<DeviceVo> devices = deviceMapper.selectByDeviceIds(list);

            // 转化为Map\<String(id), DeviceVo\>
            Map<String, DeviceVo> deviceMap = devices.stream().collect(Collectors.toMap(DeviceVo::getDeviceId, v -> v));


            // 整合本地数据与阿里云远程数据
            List<DeviceVo> vos = deviceInfo
                    .stream()
                    .map(v -> {
                        DeviceVo deviceVo = BeanUtil.toBean(v, DeviceVo.class);
                        deviceVo.setDeviceId(v.getIotId());
                        // 如果本地数据库中有该设备信息，则覆盖
                        if (ObjectUtil.isNotEmpty(deviceMap) && ObjectUtil.isNotEmpty(deviceMap.get(v.getIotId()))) {
                            DeviceVo device = deviceMap.get(v.getIotId());
                            BeanUtil.copyProperties(device, deviceVo);
                            deviceVo.setIotId(deviceVo.getDeviceId());
                        }
                        return deviceVo;
                    }).collect(Collectors.toList());

            return PageResponse.of(vos, body.getPage(), body.getPageSize(), (long) body.getPageCount(),
                    (long) body.getTotal());
        }
        return null;
    }

    @Override
    public DeviceVo queryDeviceDetail(QueryDeviceDetailRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        QueryDeviceDetailResponse queryDeviceDetailResponse = client.queryDeviceDetail(request);
        if (Boolean.TRUE.equals(queryDeviceDetailResponse.getBody().getSuccess())) {

            QueryDeviceDetailResponseBody.QueryDeviceDetailResponseBodyData data =
                    queryDeviceDetailResponse.getBody().getData();
            DeviceVo deviceVo = BeanUtil.toBean(data, DeviceVo.class);
            List<DeviceVo> devices = deviceMapper.selectByDeviceIds(Lists.newArrayList(data.getIotId()));
            if (CollUtil.isNotEmpty(devices)) {
                BeanUtil.copyProperties(devices.get(0), deviceVo, CopyOptions.create().ignoreNullValue());
                deviceVo.setIotId(deviceVo.getDeviceId());
            }
            return deviceVo;
        }
        throw new BaseException(queryDeviceDetailResponse.getBody().getErrorMessage());
    }

    @Override
    public void deleteDevice(DeleteDeviceRequest request) throws Exception {
        request.setIotInstanceId(iotInstanceId);
        DeleteDeviceResponse response = client.deleteDevice(request);
        if (Boolean.TRUE.equals(response.getBody().getSuccess())) {
            deviceMapper.deleteByDeviceId(request.getIotId());
            return;
        }
        throw new BaseException(response.getBody().getErrorMessage());
    }
}