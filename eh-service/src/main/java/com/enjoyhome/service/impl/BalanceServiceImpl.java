package com.enjoyhome.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.enjoyhome.base.PageResponse;
import com.enjoyhome.entity.Balance;
import com.enjoyhome.entity.Bill;
import com.enjoyhome.enums.BillStatus;
import com.enjoyhome.exception.BaseException;
import com.enjoyhome.mapper.BalanceMapper;
import com.enjoyhome.mapper.BillMapper;
import com.enjoyhome.service.BalanceService;
import com.enjoyhome.utils.ObjectUtil;
import com.enjoyhome.vo.BalanceVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 余额服务
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    @Resource
    BalanceMapper balanceMapper;

    @Override
    public Balance selectByElderId(Long elderId) {
        return balanceMapper.selectByElderId(elderId);
    }

    @Override
    public void save(Balance balance) {
        balanceMapper.updateByPrimaryKey(balance);
    }

    @Resource
    private BillMapper billMapper;

    @Override
    public PageResponse<BalanceVo> page(String bedNo, String elderName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Balance> page =  balanceMapper.page(bedNo, elderName);
        // 押金是否已经交过
        List<Long> list = page.getResult().stream().map(Balance::getElderId).distinct().collect(Collectors.toList());
        if(!list.isEmpty()){
            List<Bill> bills = billMapper.selectDepositByEldersAndStatus(list, BillStatus.PAY.getOrdinal());
            Map<Long, BigDecimal> map = bills.stream().collect(Collectors.toMap(Bill::getElderId, Bill::getDepositAmount));
            page.getResult().forEach(v -> {
                BigDecimal bigDecimal = map.get(v.getElderId());
                if (ObjectUtil.isEmpty(bigDecimal)) {
                    v.setDepositAmount(new BigDecimal(0));
                }
            });
        }
        return PageResponse.of(page, BalanceVo.class);
    }
    /**
     * 关闭余额账户
     * @param elderId 老人id
     * @param deductions 扣除金额
     * @throws BaseException 关闭余额账户失败
     */
    @Override
    public void close(Long elderId, BigDecimal deductions) throws BaseException {
        Balance balance = balanceMapper.selectByElderId(elderId);
        if (ObjectUtil.isEmpty(deductions)) {
            deductions = new BigDecimal(0);
        }
        if (deductions.compareTo(balance.getPrepaidBalance()) <= 0) {
            balance.setPrepaidBalance(new BigDecimal(0));
            // 删除数据
            balanceMapper.deleteByPrimaryKey(balance.getId());
            // 其他账单全部关闭
            billMapper.close(elderId);
            return;
        }
        throw new BaseException("关闭余额账户失败");
    }
}
