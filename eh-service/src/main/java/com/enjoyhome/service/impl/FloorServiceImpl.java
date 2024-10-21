package com.enjoyhome.service.impl;


import com.enjoyhome.dto.FloorDto;
import com.enjoyhome.entity.Floor;
import com.enjoyhome.mapper.FloorMapper;
import com.enjoyhome.service.BedService;
import com.enjoyhome.service.FloorService;
import com.enjoyhome.service.RoomService;
import com.enjoyhome.vo.FloorVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service

public class FloorServiceImpl implements FloorService {


    @Autowired
    RoomService roomService;

    @Autowired
    BedService bedService;

    @Autowired
    private FloorMapper floorMapper;

    //增加楼层
    @Override
    public int addFloor(FloorDto floorDto) {
        Floor floor = new Floor();
        BeanUtils.copyProperties(floorDto, floor);
        return floorMapper.insert(floor);
    }



    //通过ID删除楼层
    @Override
    public int deleteFloor(Long id) {
        return floorMapper.deleteById(id);
    }



    //更新楼层信息
    @Override
    public int updateFloor(FloorDto floorDto) {
        Floor floor = new Floor();
        BeanUtils.copyProperties(floorDto, floor);
        floor.setUpdateTime(LocalDateTime.now());
        return floorMapper.updateById(floor);
    }


    //通过ID查询楼层信息
    @Override
    public FloorVo getFloor(Long id) {
        Floor floor = floorMapper.selectById(id);
        FloorVo floorVo = new FloorVo();
        BeanUtils.copyProperties(floor, floorVo);
        return floorVo;
    }

    //获取所有的楼层信息
    @Override
    public List<FloorVo> getAllFloors() {
       return floorMapper.selectAll();
    }

    @Override
    public List<FloorVo> getAllWithRoomAndBed() {
        return floorMapper.selectAllRoomAndBed();
    }

    @Override
    public List<FloorVo> selectAllByDevice() {
        return floorMapper.selectAllByDevice();
    }

    @Override
    public List<FloorVo> selectAllByNur() {
        return floorMapper.selectAllByNur();
    }


}

