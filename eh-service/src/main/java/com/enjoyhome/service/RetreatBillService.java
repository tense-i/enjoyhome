package com.enjoyhome.service;

import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.RefundVoucherDto;

public interface RetreatBillService {

    /**
     * 上传退款凭证
     * @param refundVoucherDto
     * @return
     */
    ResponseResult uploadRefundVoucher(RefundVoucherDto refundVoucherDto);

    /**
     * 查询退住账单数据
     * @param retreatId
     * @return
     */
    ResponseResult getRetreatBill(Long retreatId);
}
