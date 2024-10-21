package com.enjoyhome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.enjoyhome.dto.BedDto;
import com.enjoyhome.entity.Bed;
import com.enjoyhome.mapper.BedMapper;
import com.enjoyhome.mapper.DeviceMapper;
import com.enjoyhome.service.BedService;
import com.enjoyhome.vo.BedVo;
import com.enjoyhome.vo.DeviceVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl implements BedService {

    @Resource
    BedMapper bedMapper;

    @Override
    public void addBed(BedDto bed) {
        Bed bed1 = BeanUtil.toBean(bed, Bed.class);
        bed1.setBedStatus(0);
        bedMapper.addBed(bed1);
    }

    @Override
    public void updateBed(BedDto bed) {
        BedVo bedById = getBedById(bed.getId());
        bedById.setSort(bed.getSort());
        bedById.setBedNumber(bed.getBedNumber());
        if (ObjectUtil.isNotEmpty(bed.getBedStatus())) {
            bedById.setBedStatus(bed.getBedStatus());
        }
        Bed bed1 = BeanUtil.toBean(bedById, Bed.class);
        bedMapper.updateBed(bed1);
    }

    @Override
    public void deleteBedById(Long id) {
        bedMapper.deleteBedById(id);
    }


    @Override
    public BedVo getBedById(Long id) {
        return BeanUtil.toBean(bedMapper.getBedById(id), BedVo.class);
    }

    @Override
    public List<BedVo> getAllBeds() {
        List<Bed> allBeds = bedMapper.getAllBeds();
        return allBeds.stream().map(v -> BeanUtil.toBean(v, BedVo.class)).collect(Collectors.toList());
    }

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public List<BedVo> getBedsByRoomId(Long roomId) {
        return bedMapper.getBedsByRoomId(roomId);
    }

    @Override
    public BedVo getDevice(Long id, Integer type) {
        BedVo bedVo = new BedVo();
        List<DeviceVo> devices = deviceMapper.selectByLocation(Lists.newArrayList(id.toString()), type);
        devices.forEach(deviceVo -> {
            deviceVo.getDeviceDataVos().forEach(deviceDataVo -> {
                if (deviceDataVo.getStatus() > 0) {
                    bedVo.setStatus(2);
                }
            });
        });
        bedVo.setDeviceVos(devices);
        return bedVo;
    }


}

