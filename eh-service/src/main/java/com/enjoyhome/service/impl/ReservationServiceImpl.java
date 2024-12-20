package com.enjoyhome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.ReservationDto;
import com.enjoyhome.dto.VisitDto;
import com.enjoyhome.entity.Reservation;
import com.enjoyhome.enums.ReservationStatus;
import com.enjoyhome.exception.BaseException;
import com.enjoyhome.mapper.ReservationMapper;
import com.enjoyhome.service.ElderService;
import com.enjoyhome.service.ReservationService;
import com.enjoyhome.service.VisitService;
import com.enjoyhome.utils.UserThreadLocal;
import com.enjoyhome.vo.ReservationVo;
import com.enjoyhome.vo.TimeCountVo;
import com.enjoyhome.vo.retreat.ElderVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Resource
    private VisitService visitService;

    @Resource
    private ElderService elderService;

    /**
     * 添加预约
     */
    @Override
    public void add(ReservationDto dto) {
        if (dto.getType().equals(1)) {
            // 查询要探访的老人
            ElderVo elderVo = elderService.selectByPrimaryKey(dto.getElderId());

            // 如果老人状态为退住中或已退住，则不允许预约
            if (elderVo.getStatus() == 3) {
                throw new BaseException("退住中，不可预约");
            }
            if (elderVo.getStatus() == 5) {
                throw new BaseException("已退住，不可预约");
            }
        }
        Long userId = UserThreadLocal.getUserId();

        // 取消次数大于3次的也不能预约
        int cancelledReservationCount = getCancelledReservationCount(userId);

        // 如果已经预约的次数超过3次，则不需要再进行预约
        if (cancelledReservationCount >= 3) {
            throw new BaseException("今天取消次数已达上限，不可进行预约");
        }

        // 否则，允许添加预约
        // service到mapper要将dto转换为entity
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(dto, reservation);
        reservation.setStatus(ReservationStatus.PENDING.getOrdinal());
        reservation.setCreateBy(userId);

        try {
            // 插入预约
            reservationMapper.insert(reservation);
        } catch (PersistenceException e) { // 使用框架特定的异常
            // 检查是否是违反唯一约束
            if (e.getCause() instanceof SQLException && ((SQLException) e.getCause()).getErrorCode() == 1062) {
                log.info(e + "");
                throw new BaseException("此手机号已预约该时间");
            }
        } catch (Exception e) {
            // 处理其他异常
            log.error(e + "");
            throw new BaseException("预约失败");
        }
    }

    /**
     * 更新预约
     */
    @Override
    public void update(Long id, ReservationDto dto) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            BeanUtils.copyProperties(dto, reservation);
            reservation.setId(id);
            reservation.setUpdateTime(LocalDateTime.now());
            reservationMapper.update(reservation);
        }
    }

    /**
     * 取消预约
     */
    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            reservation.setStatus(ReservationStatus.CANCELED.getOrdinal());
            reservationMapper.update(reservation);
        }
    }

    /**
     * 根据id删除预约
     */
    @Override
    public void deleteById(Long id) {
        reservationMapper.deleteById(id);
    }

    /**
     * 根据id查找预约
     */
    @Override
    public ReservationVo findById(Long id) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            return convertToVO(reservation);
        }
        return null;
    }


    /**
     * 查找所有预约
     *
     * @param mobile 预约人手机号
     * @param time   预约时间
     */
    @Override
    public List<ReservationVo> findAll(String mobile, LocalDateTime time) {
        LocalDateTime endTime = time.plusHours(24); // 计算24小时后的时间
        Long userId = UserThreadLocal.getUserId();
        List<Reservation> reservations = reservationMapper.findAll(userId, mobile, time, endTime); // 根据mobile和时间范围查询预约
        return convertToVOList(reservations);
    }


    /**
     * 分页查找预约
     *
     * @param page      页码
     * @param size      每页大小
     * @param name      预约人姓名
     * @param phone     预约人手机号
     * @param status    预约状态
     * @param type      预约类型
     * @param startTime
     * @param endTime
     * @return 预约列表
     */
    @Override
    public PageResponse<ReservationVo> findByPage(int page, int size, String name, String phone, Integer status,
                                                  Integer type, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(page, size);
        Long userId = UserThreadLocal.getUserId();
        Page<Reservation> byPage = reservationMapper.findByPage(page, size, name, phone, status, type, userId,
                startTime, endTime);
        return PageResponse.of(byPage, ReservationVo.class);
    }


    /**
     * 将Reservation转换为ReservationVO
     */
    private ReservationVo convertToVO(Reservation reservation) {
        return BeanUtil.toBean(reservation, ReservationVo.class);
    }

    /**
     * 将List<Reservation>转换为List<ReservationVO>
     */
    private List<ReservationVo> convertToVOList(List<Reservation> reservations) {
        return reservations.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 将时间戳转换为字符串
     */
    private String convertTimeToStr(Long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    /**
     * 如果预约未完成，则将预约状态更新为过期
     */
    @Override
    public void updateVisitReservationStatusToExpiredIfNotCompleted(Long id) {
        Reservation visitReservation = reservationMapper.findById(id);
        if (visitReservation.getStatus().equals(ReservationStatus.COMPLETED.getOrdinal())
                || visitReservation.getStatus().equals(ReservationStatus.CANCELED.getOrdinal())) {
            return;
        }
        LocalDateTime reservationTime = visitReservation.getTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(reservationTime.plusHours(1))) {
            visitReservation.setStatus(ReservationStatus.EXPIRED.getOrdinal());
            reservationMapper.update(visitReservation);
        }
    }

    /**
     * 查询每个时间段剩余预约次数
     *
     * @param time 时间 日
     * @return 每个时间段剩余预约次数
     */
    @Override
    public List<TimeCountVo> countReservationsForEachTimeWithinTimeRange(LocalDateTime time) {
        // endtime为time的24小时后
        LocalDateTime endTime = time.plusHours(24);
        // 查詢time到endtime之间的每个时间段的预约次数
        return reservationMapper.countReservationsForEachTimeWithinTimeRange(time, endTime);
    }

    /**
     * 获取取消预约次数
     *
     * @param updateBy 更新人id
     * @return 取消预约次数
     */
    @Override
    public int getCancelledReservationCount(Long updateBy) {
        // 获取当天的开始时间和结束时间内的取消预约次数
        return reservationMapper.countCancelledReservationsWithinTimeRange(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59), updateBy);
    }

    /**
     * 来访
     *
     * @param id   ID
     * @param time 时间
     */
    @Override
    public void visit(Long id, Long time) {
        Reservation reservation = reservationMapper.findById(id);
        if (reservation != null) {
            reservation.setStatus(ReservationStatus.COMPLETED.getOrdinal());
            reservationMapper.update(reservation);
            VisitDto visitDto = BeanUtil.toBean(reservation, VisitDto.class);
            visitDto.setTime(LocalDateTimeUtil.of(time));
            visitService.add(visitDto);
        }
    }

    /**
     * xxl-job 定时任务，更新过期预约状态(每一小时的1分、31分执行)
     *
     * @param now
     */
    @Override
    public void updateReservationStatus(LocalDateTime now) {
        reservationMapper.updateReservationStatus(now);
    }
}

