package com.enjoyhome.service;

import com.enjoyhome.dto.CheckInConfigDto;
import com.enjoyhome.entity.CheckInConfig;

/**
 * <p>
 * check_in_config Service 接口
 * </p>
 *
 * @author tensei
 */
public interface CheckInConfigService {

    /**
     * 根据老人id查询入住配置
     *
     * @param elderId
     * @return
     */
    CheckInConfig findCurrentConfigByElderId(Long elderId);

    /**
     * 入住选择配置
     *
     * @param checkInConfigDto 入住选择配置
     * @return 受影响的行数
     */
    int checkIn(CheckInConfigDto checkInConfigDto);
}