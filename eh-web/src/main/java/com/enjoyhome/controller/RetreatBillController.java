package com.enjoyhome.controller;

import com.enjoyhome.base.ResponseResult;
import com.enjoyhome.dto.RefundVoucherDto;
import com.enjoyhome.service.RetreatBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retreat_bills")
@Api(tags = "退住账单")
public class RetreatBillController {

    @Autowired
    private RetreatBillService retreatBillService;

    @ApiOperation(value = "上传退款凭证", notes = "用于上传退款凭证文件")
    @PutMapping("/uploadRefundVoucher")
    public ResponseResult uploadRefundVoucher(@RequestBody RefundVoucherDto refundVoucherDto) {
        return retreatBillService.uploadRefundVoucher(refundVoucherDto);
    }

    @ApiOperation(value = "上传退款凭证数据回显")
    @GetMapping
    public ResponseResult getRetreatBill(Long retreatId){
        return retreatBillService.getRetreatBill(retreatId);
    }


}
