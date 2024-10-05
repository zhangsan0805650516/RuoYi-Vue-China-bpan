package com.ruoyi.coin.member.service.impl;

import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
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
import java.util.Date;

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

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

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

        if (faMember.getFromAccount().equals(faMember.getToAccount())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = iFaMemberService.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 兑换金额
        BigDecimal amount = faMember.getAmount();

        // 转出流水
        String contentOut = "";
        BigDecimal beforeMoneyOut = BigDecimal.ZERO;
        BigDecimal laterMoneyOut = BigDecimal.ZERO;
        Integer typeOut = 0;
        switch (faMember.getFromAccount()) {
            case 1:
                if (selectOne.getBalance().subtract(selectOne.getFreezeProfit()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                contentOut = "现金账户转出";
                beforeMoneyOut = selectOne.getBalance();
                laterMoneyOut = selectOne.getBalance().subtract(amount);
                typeOut = 116;
                break;
            case 2:
                if (selectOne.getBalanceSpot().subtract(selectOne.getFreezeProfitSpot()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                contentOut = "现货账户转出";
                beforeMoneyOut = selectOne.getBalanceSpot();
                laterMoneyOut = selectOne.getBalanceSpot().subtract(amount);
                typeOut = 117;
                break;
            case 3:
                if (selectOne.getBalanceContract().subtract(selectOne.getFreezeProfitContract()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                contentOut = "合约账户转出";
                beforeMoneyOut = selectOne.getBalanceContract();
                laterMoneyOut = selectOne.getBalanceContract().subtract(amount);
                typeOut = 118;
                break;
            case 4:
                if (selectOne.getBalanceFinancing().subtract(selectOne.getFreezeProfitFinancing()).compareTo(amount) < 0) {
                    throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
                }
                contentOut = "理财账户转出";
                beforeMoneyOut = selectOne.getBalanceFinancing();
                laterMoneyOut = selectOne.getBalanceFinancing().subtract(amount);
                typeOut = 119;
                break;
            default:
                break;
        }

        // 转出流水
        FaCapitalLog faCapitalLogOut = new FaCapitalLog();
        faCapitalLogOut.setUserId(selectOne.getId());
        faCapitalLogOut.setMobile(selectOne.getMobile());
        faCapitalLogOut.setName(selectOne.getName());
        faCapitalLogOut.setSuperiorId(selectOne.getSuperiorId());
        faCapitalLogOut.setSuperiorCode(selectOne.getSuperiorCode());
        faCapitalLogOut.setSuperiorName(selectOne.getSuperiorName());
        faCapitalLogOut.setContent(contentOut);
        faCapitalLogOut.setBeforeMoney(beforeMoneyOut);
        faCapitalLogOut.setLaterMoney(laterMoneyOut);
        faCapitalLogOut.setMoney(amount);
        faCapitalLogOut.setType(typeOut);
        faCapitalLogOut.setDirect(Constants.subtract);
        faCapitalLogOut.setCreateTime(new Date());
        faCapitalLogOut.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLogOut);

        // 转出金额
        // 更新用户余额 减少
        iFaMemberService.updateMemberBalanceByType(selectOne.getId(), amount, Constants.subtract, faMember.getFromAccount());
        // 更新用户冻结 减少
        iFaMemberService.updateFaMemberFreezeProfitByType(selectOne.getId(), amount, Constants.subtract, faMember.getFromAccount());


        // 转入流水
        String contentIn = "";
        BigDecimal beforeMoneyIn = BigDecimal.ZERO;
        BigDecimal laterMoneyIn = BigDecimal.ZERO;
        Integer typeIn = 0;
        switch (faMember.getToAccount()) {
            case 1:
                contentIn = "现金账户转入";
                beforeMoneyIn = selectOne.getBalance();
                laterMoneyIn = selectOne.getBalance().add(amount);
                typeIn = 120;
                break;
            case 2:
                contentIn = "现货账户转入";
                beforeMoneyIn = selectOne.getBalanceSpot();
                laterMoneyIn = selectOne.getBalanceSpot().add(amount);
                typeIn = 121;
                break;
            case 3:
                contentIn = "合约账户转入";
                beforeMoneyIn = selectOne.getBalanceContract();
                laterMoneyIn = selectOne.getBalanceContract().add(amount);
                typeIn = 122;
                break;
            case 4:
                contentIn = "理财账户转入";
                beforeMoneyIn = selectOne.getBalanceFinancing();
                laterMoneyIn = selectOne.getBalanceFinancing().add(amount);
                typeIn = 123;
                break;
            default:
                break;
        }

        // 转入流水
        FaCapitalLog faCapitalLogIn = new FaCapitalLog();
        faCapitalLogIn.setUserId(selectOne.getId());
        faCapitalLogIn.setMobile(selectOne.getMobile());
        faCapitalLogIn.setName(selectOne.getName());
        faCapitalLogIn.setSuperiorId(selectOne.getSuperiorId());
        faCapitalLogIn.setSuperiorCode(selectOne.getSuperiorCode());
        faCapitalLogIn.setSuperiorName(selectOne.getSuperiorName());
        faCapitalLogIn.setContent(contentIn);
        faCapitalLogIn.setBeforeMoney(beforeMoneyIn);
        faCapitalLogIn.setLaterMoney(laterMoneyIn);
        faCapitalLogIn.setMoney(amount);
        faCapitalLogIn.setType(typeIn);
        faCapitalLogIn.setDirect(Constants.ADD);
        faCapitalLogIn.setCreateTime(new Date());
        faCapitalLogIn.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLogIn);

        // 转入金额
        // 更新用户余额 增加
        iFaMemberService.updateMemberBalanceByType(selectOne.getId(), amount, Constants.ADD, faMember.getToAccount());
    }
}