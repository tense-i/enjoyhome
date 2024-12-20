/**
 * 老人服务接口
 */
package com.enjoyhome.service;

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.ElderDto;
import com.enjoyhome.dto.NursingElderDto;
import com.enjoyhome.entity.Elder;
import com.enjoyhome.vo.retreat.ElderVo;

import java.util.List;

public interface ElderService {
    /**
     * 根据id删除老人信息
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入老人信息
     *
     * @param record
     * @return
     */
    Elder insert(ElderDto record);

    /**
     * 选择性插入老人信息
     *
     * @param record
     * @return
     */
    int insertSelective(ElderDto record);

    /**
     * 根据id选择老人信息
     *
     * @param id
     * @return
     */
    ElderVo selectByPrimaryKey(Long id);

    /**
     * 选择性更新老人信息。
     *
     * @param record
     * @param b      若为true，则更新老人的姓名为name+remark、remark+1
     * @return
     */
    Elder updateByPrimaryKeySelective(ElderDto record, boolean b);

    /**
     * 更新老人信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(ElderDto record);

    /**
     * 根据身份证号和姓名查询老人信息
     *
     * @param idCard
     * @param name
     * @return
     */
    ElderVo selectByIdCardAndName(String idCard, String name);

    /**
     * 查询所有老人
     *
     * @return
     */
    List<ElderVo> selectList();

    /**
     * 根据id集合查询老人列表
     *
     * @param ids
     * @return
     */
    List<Elder> selectByIds(List<Long> ids);

    /**
     * 选择老人列表
     *
     * @param name     老人姓名
     * @param idCardNo 身份证号
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResponse selectListByPage(String name, String idCardNo, Integer pageNum, Integer pageSize);

    /**
     * 设置护理员
     *
     * @param nursingElders
     */
    void setNursing(List<NursingElderDto> nursingElders);

    /**
     * 根据身份证号和名字查询老人
     *
     * @param idCard
     * @param name
     * @return
     */
    ElderVo selectByIdCard(String idCard, String name);

    /**
     * 根据身份证号查询老人
     *
     * @param idCard
     * @return
     */
    ElderVo selectByIdCard(String idCard);


    /**
     * 清除老人床位编号
     *
     * @param elderId
     */
    void clearBedNum(Long elderId);
}

