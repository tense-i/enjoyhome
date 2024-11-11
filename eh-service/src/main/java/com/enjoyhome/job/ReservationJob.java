package com.enjoyhome.job;

import com.enjoyhome.service.ReservationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author tensei
 */
@Component
@Slf4j
public class ReservationJob {

    @Autowired
    private ReservationService reservationService;

    /**
     * 预约状态-过期修改
     *
     * @scription 该任务每小时的第1、第31分钟执行一次
     * @attention 该方法为定时任务方法，不可随意更改方法名、参数、返回值等
     */
    @XxlJob("reservationStatusToExpired")
    public void updateReservationStatus() {
        log.info("预约状态-过期修改-begin");
        reservationService.updateReservationStatus(LocalDateTime.now());
        log.info("预约状态-过期修改-end");
    }
}
