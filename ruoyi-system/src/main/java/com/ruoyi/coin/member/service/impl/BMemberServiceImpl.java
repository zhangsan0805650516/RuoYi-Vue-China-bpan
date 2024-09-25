package com.ruoyi.coin.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.coin.member.service.BMemberService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * B交易Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Service
public class BMemberServiceImpl implements BMemberService
{

    @Autowired
    private IFaMemberService iFaMemberService;

    /**
     * 用户账户转换
     * @param faMember
     * @throws Exception
     */
    @Override
    public void balanceChange(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getFromAccount() || null == faMember.getToAccount()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = iFaMemberService.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 兑换金额
        BigDecimal amount = faMember.getAmount();

        switch (faMember.getFromAccount()) {
            case 1:
                if (selectOne.getBalance().subtract(selectOne.getFreezeProfit()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                break;
            case 2:
                if (selectOne.getBalanceSpot().subtract(selectOne.getFreezeProfitSpot()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                break;
            case 3:
                if (selectOne.getBalanceContract().subtract(selectOne.getFreezeProfitContract()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                break;
            case 4:
                if (selectOne.getBalanceFinancing().subtract(selectOne.getFreezeProfitFinancing()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                break;
            default:
                break;
        }

        // 转出金额
        // 更新用户余额 减少
        iFaMemberService.updateMemberBalanceByType(selectOne.getId(), amount, Constants.subtract, faMember.getFromAccount());
        // 更新用户冻结 减少
        iFaMemberService.updateFaMemberFreezeProfitByType(selectOne.getId(), amount, Constants.subtract, faMember.getFromAccount());

        // 转入金额
        // 更新用户余额 增加
        iFaMemberService.updateMemberBalanceByType(selectOne.getId(), amount, Constants.ADD, faMember.getToAccount());
    }
}