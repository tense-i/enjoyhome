

package com.enjoyhome.service;

import com.enjoyhome.base.PageResponse;
import com.enjoyhome.dto.ContractDto;
import com.enjoyhome.entity.Contract;
import com.enjoyhome.vo.ContractVo;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 合同 Service
 */
public interface ContractService {

    /**
     * 根据合同id查询合同信息
     *
     * @param id 合同id
     * @return 合同信息
     */
    ContractVo getById(Long id);

    /**
     * 新增合同信息
     *
     * @param contractDto 合同信息
     * @return 受影响的行数
     */
    int sign(ContractDto contractDto);

    /**
     * 更新合同信息
     *
     * @param contractDto 合同信息
     * @return 受影响的行数
     */
    int update(ContractDto contractDto);

    /**
     * 根据合同id删除合同信息
     * * @param id 合同id
     * * @return 受影响的行数
     */
    int deleteById(Long id);


    /**
     * 分页查询合同信息
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param contractNo 合同编号
     * @param elderName 老人姓名
     * @param status 合同状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    PageResponse<ContractVo> selectByPage(Integer pageNum, Integer pageSize, String contractNo, String elderName,
                                          Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询所有合同
     * @return
     */
    List<Contract> listAllContracts();

    /**
     * 批量修改合同
     * @param updateList
     */
    void updateBatchById(List<Contract> updateList);

    /**
     * 根据手机号查询合同列表
     * @param phone
     * @return
     */
    List<ContractVo> listByMemberPhone(String phone);
}


