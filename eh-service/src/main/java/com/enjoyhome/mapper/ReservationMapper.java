package com.enjoyhome.mapper;

import com.enjoyhome.entity.Reservation;
import com.enjoyhome.vo.TimeCountVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {

    int insert(Reservation reservation);

    int update(Reservation reservation);

    int deleteById(Long id);

    Reservation findById(Long id);

    List<Reservation> findAll(@Param("createBy") Long userId, @Param("mobile") String mobile,
                              @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Page<Reservation> findByPage(@Param("page") int startIndex, @Param("pageSize") int pageSize,
                                 @Param("name") String name, @Param("mobile") String mobile,
                                 @Param("status") Integer status, @Param("type") Integer type,
                                 @Param("createBy") Long userId, @Param("startTime") LocalDateTime startTime, @Param(
            "endTime") LocalDateTime endTime);

    int countReservationsWithinTimeRange(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime, @Param("createBy") Long createBy,
                                         @Param("status") Integer status);

    /**
     * 查詢每個時間段剩餘預約次數
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<TimeCountVo> countReservationsForEachTimeWithinTimeRange(@Param("startTime") LocalDateTime startTime,
                                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 查询取消预约数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param updateBy  更新人
     * @return
     */
    int countCancelledReservationsWithinTimeRange(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("updateBy") Long updateBy);

    @Update("update reservation set status = 3 where status = 0 and time <= #{minusDays}")
    void updateReservationStatus(LocalDateTime minusDays);

}
