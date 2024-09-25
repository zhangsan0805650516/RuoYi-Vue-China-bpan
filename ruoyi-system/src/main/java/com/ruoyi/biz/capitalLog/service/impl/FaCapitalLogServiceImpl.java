package com.ruoyi.biz.capitalLog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.mapper.FaCapitalLogMapper;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.biz.stockHoldDetail.service.IFaStockHoldDetailService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.stockTrading.service.IFaStockTradingService;
import com.ruoyi.biz.tradeApprove.domain.FaTradeApprove;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 资金记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Service
public class FaCapitalLogServiceImpl extends ServiceImpl<FaCapitalLogMapper, FaCapitalLog> implements IFaCapitalLogService
{
    @Autowired
    private FaCapitalLogMapper faCapitalLogMapper;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaStockTradingService iFaStockTradingService;

    @Autowired
    private IFaStockHoldDetailService iFaStockHoldDetailService;

    /**
     * 查询资金记录
     *
     * @param id 资金记录主键
     * @return 资金记录
     */
    @Override
    public FaCapitalLog selectFaCapitalLogById(Integer id)
    {
        return faCapitalLogMapper.selectFaCapitalLogById(id);
    }

    /**
     * 查询资金记录列表
     *
     * @param faCapitalLog 资金记录
     * @return 资金记录
     */
    @Override
    public List<FaCapitalLog> selectFaCapitalLogList(FaCapitalLog faCapitalLog)
    {
        faCapitalLog.setDeleteFlag(0);
        return faCapitalLogMapper.selectFaCapitalLogList(faCapitalLog);
    }

    /**
     * 新增资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    @Override
    public int insertFaCapitalLog(FaCapitalLog faCapitalLog)
    {
        faCapitalLog.setCreateTime(DateUtils.getNowDate());
        return faCapitalLogMapper.insertFaCapitalLog(faCapitalLog);
    }

    /**
     * 修改资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    @Override
    public int updateFaCapitalLog(FaCapitalLog faCapitalLog)
    {
        faCapitalLog.setMobile(null);
        faCapitalLog.setUpdateTime(DateUtils.getNowDate());
        return faCapitalLogMapper.updateFaCapitalLog(faCapitalLog);
    }

    /**
     * 批量删除资金记录
     *
     * @param ids 需要删除的资金记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCapitalLogByIds(Integer[] ids)
    {
        return faCapitalLogMapper.deleteFaCapitalLogByIds(ids);
    }

    /**
     * 删除资金记录信息
     *
     * @param id 资金记录主键
     * @return 结果
     */
    @Override
    public int deleteFaCapitalLogById(Integer id)
    {
        return faCapitalLogMapper.deleteFaCapitalLogById(id);
    }

    /**
     * 查询资金记录
     * @param faCapitalLog
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaCapitalLog> getFundRecord(FaCapitalLog faCapitalLog) throws Exception {
        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, faCapitalLog.getUserId());
        if (null != faCapitalLog.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, faCapitalLog.getType());
        }

//        类型(0充值 1提现 2普通下单 3普通下单手续费 4印花税 5平仓收益 6中签认缴 7提现退回 8大宗下单 " +
//                "9大宗下单手续费 10大宗平仓收益 11大宗印花税 12配售冻结 13未中签退回 14普通卖出手续费 15大宗卖出手续费" +
//                "16VIP调研下单 17VIP调研下单手续费 18VIP调研平仓收益 19VIP调研印花税 20VIP调研卖出手续费)
        lambdaQueryWrapper.notIn(FaCapitalLog::getType, 0, 1, 3, 4, 6, 7, 12, 13, 9, 11, 14, 15, 17, 19, 20);

        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaCapitalLog::getId);
        IPage<FaCapitalLog> faCapitalLogIPage = this.page(new Page<>(faCapitalLog.getPage(), faCapitalLog.getSize()), lambdaQueryWrapper);
        if (faCapitalLogIPage.getRecords().size() > 0) {
            for (FaCapitalLog log : faCapitalLogIPage.getRecords()) {
                // 买入流水，集合买入手续费
                if (2 == log.getType() || 8 == log.getType() || 16 == log.getType()) {
                    String description = "买入股票，" + log.getStockSymbol() + "/" + log.getStockName() + "，";
                    description += "占用本金：" + log.getMoney().setScale(2, RoundingMode.HALF_UP) + "，";
                    // 手续费
                    BigDecimal fee = getFee(log);
                    description += "总手续费：" + fee.setScale(2, RoundingMode.HALF_UP) + "，递延费：0.00，";
                    // 印花税
                    BigDecimal stampDuty = getStampDuty(log);
                    description += "印花税：" + stampDuty.setScale(2, RoundingMode.HALF_UP);
                    log.setDescription(description);
                }
                // 卖出流水，集合卖出手续费，卖出印花税
                else if (5 == log.getType() || 10 == log.getType() || 18 == log.getType()) {
                    String description = "卖出股票，" + log.getStockSymbol() + "/" + log.getStockName() + "，";
                    description += "占用本金：" + log.getMoney().setScale(2, RoundingMode.HALF_UP) + "，";
                    // 手续费
                    BigDecimal fee = getFee(log);
                    description += "总手续费：" + fee.setScale(2, RoundingMode.HALF_UP) + "，递延费：0.00，";
                    // 印花税
                    BigDecimal stampDuty = getStampDuty(log);
                    description += "印花税：" + stampDuty.setScale(2, RoundingMode.HALF_UP) + "，";
                    // 盈亏
                    BigDecimal profit = getProfit(log);
                    description += "盈亏：" + profit.setScale(2, RoundingMode.HALF_UP) + "，";
                    // 总盈亏
                    BigDecimal totalProfit = profit.subtract(fee).subtract(stampDuty);
                    description += "总盈亏：" + totalProfit.setScale(2, RoundingMode.HALF_UP);
                    log.setDescription(description);
                }
            }
        }
        return faCapitalLogIPage;
    }

    /**
     * 关联盈亏
     * @param log
     * @return
     * @throws Exception
     */
    private BigDecimal getProfit(FaCapitalLog log) throws Exception {
        BigDecimal profit = BigDecimal.ZERO;
        // 交易记录
        FaStockTrading faStockTrading = iFaStockTradingService.getById(log.getOrderId());
        if (ObjectUtils.isNotEmpty(faStockTrading)) {
            // 持仓记录
            FaStockHoldDetail faStockHoldDetail = iFaStockHoldDetailService.getById(faStockTrading.getHoldId());
            if (ObjectUtils.isNotEmpty(faStockHoldDetail)) {
                profit = faStockHoldDetail.getProfitLose();
            }
        }
        return profit;
    }

    /**
     * 关联手续费
     * @param log
     * @return
     * @throws Exception
     */
    private BigDecimal getFee(FaCapitalLog log) throws Exception {
        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, log.getUserId());
        lambdaQueryWrapper.eq(FaCapitalLog::getOrderId, log.getOrderId());
        lambdaQueryWrapper.eq(FaCapitalLog::getDirect, 1);
        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);

        // 买入
        if (2 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 3);
        } else if (8 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 9);
        } else if (16 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 17);
        }
        // 卖出
        else if (5 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 14);
        } else if (10 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 15);
        } else if (18 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 20);
        }
        lambdaQueryWrapper.last(" limit 1 ");
        FaCapitalLog faCapitalLog = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faCapitalLog)) {
            return faCapitalLog.getMoney();
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 关联印花税
     * @param log
     * @return
     * @throws Exception
     */
    private BigDecimal getStampDuty(FaCapitalLog log) throws Exception {
        LambdaQueryWrapper<FaCapitalLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaCapitalLog::getUserId, log.getUserId());
        lambdaQueryWrapper.eq(FaCapitalLog::getOrderId, log.getOrderId());
        lambdaQueryWrapper.eq(FaCapitalLog::getDirect, 1);
        lambdaQueryWrapper.eq(FaCapitalLog::getDeleteFlag, 0);

        // 买入
        if (2 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 4);
        } else if (8 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 11);
        } else if (16 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 19);
        }
        // 卖出
        else if (5 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 4);
        } else if (10 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 11);
        } else if (18 == log.getType()) {
            lambdaQueryWrapper.eq(FaCapitalLog::getType, 19);
        }
        lambdaQueryWrapper.last(" limit 1 ");
        FaCapitalLog faCapitalLog = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faCapitalLog)) {
            return faCapitalLog.getMoney();
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 保存普通交易资金流水
     * @param faStockTrading
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaStockTrading faStockTrading) throws Exception {
        // 买入流水
        if (1 == faStockTrading.getTradingType()) {
            saveBuyCapitalLog(faStockTrading);
        }
        // 卖出流水
        else if (2 == faStockTrading.getTradingType()) {
            saveSellCapitalLog(faStockTrading);
        }
    }

    /**
     * 买入流水
     * @param faStockTrading
     */
    private void saveBuyCapitalLog(FaStockTrading faStockTrading) throws Exception {
        BigDecimal beforeMoney = faStockTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faStockTrading.getFaMember().getBalance().subtract(faStockTrading.getTradingAmount());

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faStockTrading.getFaMember().getId());
        faCapitalLog.setMobile(faStockTrading.getFaMember().getMobile());
        faCapitalLog.setName(faStockTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
        if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("普通下单");
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("大宗下单");
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("VIP调研下单");
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("融券下单");
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faStockTrading.getTradingAmount());
        if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(2);
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(8);
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(16);
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(25);
        }
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faStockTrading.getId());
        faCapitalLog.setStockId(faStockTrading.getFaStrategy().getId());
        faCapitalLog.setStockName(faStockTrading.getFaStrategy().getTitle());
        faCapitalLog.setStockSymbol(faStockTrading.getFaStrategy().getCode());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 手续费
        if (faStockTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setTradingPoundage(faStockTrading.getTradingPoundage().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faStockTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faStockTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("普通下单手续费");
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("大宗下单手续费");
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("VIP调研下单手续费");
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("融券下单手续费");
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faStockTrading.getTradingPoundage());
            if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(3);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(9);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(17);
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(26);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLogFee.setOrderId(faStockTrading.getId());
            faCapitalLogFee.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogFee.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogFee.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 减少 交易金额+手续费
        iFaMemberService.updateMemberBalance(faStockTrading.getMemberId(), faStockTrading.getTradingAmount().add(faStockTrading.getTradingPoundage()), 1);

        // 更新用户冻结 减少 交易金额+手续费
        iFaMemberService.updateFaMemberFreezeProfit(faStockTrading.getMemberId(), faStockTrading.getTradingAmount().add(faStockTrading.getTradingPoundage()), 1);
    }

    /**
     * 卖出流水
     * @param faStockTrading
     */
    private void saveSellCapitalLog(FaStockTrading faStockTrading) throws Exception {
        BigDecimal beforeMoney = faStockTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faStockTrading.getFaMember().getBalance().add(faStockTrading.getTradingAmount());

        // 印花税
        FaCapitalLog faCapitalLogDuty = new FaCapitalLog();

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faStockTrading.getFaMember().getId());
        faCapitalLog.setMobile(faStockTrading.getFaMember().getMobile());
        faCapitalLog.setName(faStockTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
        if (0 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("平仓收益");
        } else if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("平仓收益");
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("大宗平仓收益");
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("VIP调研平仓收益");
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setContent("融券平仓收益");
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faStockTrading.getTradingAmount());
        if (0 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(5);
        } else if (1 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(5);
        } else if (2 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(10);
        } else if (3 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(18);
        } else if (8 == faStockTrading.getHoldType()) {
            faCapitalLog.setType(27);
        }
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faStockTrading.getId());
        faCapitalLog.setStockId(faStockTrading.getFaStrategy().getId());
        faCapitalLog.setStockName(faStockTrading.getFaStrategy().getTitle());
        faCapitalLog.setStockSymbol(faStockTrading.getFaStrategy().getCode());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 印花税
        if (faStockTrading.getStampDuty().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setStampDuty(faStockTrading.getStampDuty().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = faCapitalLog.getLaterMoney();
            laterMoney = faCapitalLog.getLaterMoney().subtract(faStockTrading.getStampDuty());

            faCapitalLogDuty.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogDuty.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogDuty.setName(faStockTrading.getFaMember().getName());
            faCapitalLogDuty.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogDuty.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogDuty.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent("印花税");
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent("印花税");
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent("大宗印花税");
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent("VIP调研印花税");
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setContent("融券印花税");
            }
            faCapitalLogDuty.setBeforeMoney(beforeMoney);
            faCapitalLogDuty.setLaterMoney(laterMoney);
            faCapitalLogDuty.setMoney(faStockTrading.getStampDuty());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(4);
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(4);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(11);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(19);
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogDuty.setType(28);
            }
            faCapitalLogDuty.setDirect(1);
            faCapitalLogDuty.setCreateTime(new Date());
            faCapitalLogDuty.setOrderId(faStockTrading.getId());
            faCapitalLogDuty.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogDuty.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogDuty.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogDuty.setDeleteFlag(0);
            this.save(faCapitalLogDuty);
        }

        // 手续费
        if (faStockTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            faStockTrading.setTradingPoundage(faStockTrading.getTradingPoundage().setScale(2, RoundingMode.HALF_UP));

            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faStockTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faStockTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faStockTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faStockTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faStockTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faStockTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faStockTrading.getFaMember().getSuperiorName());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("普通卖出手续费");
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("普通卖出手续费");
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("大宗卖出手续费");
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("VIP调研卖出手续费");
            } else if (8 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setContent("融券平仓手续费");
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faStockTrading.getTradingPoundage());
            if (0 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(14);
            } else if (1 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(14);
            } else if (2 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(15);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(20);
            } else if (3 == faStockTrading.getHoldType()) {
                faCapitalLogFee.setType(29);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLogFee.setOrderId(faStockTrading.getId());
            faCapitalLogFee.setStockId(faStockTrading.getFaStrategy().getId());
            faCapitalLogFee.setStockName(faStockTrading.getFaStrategy().getTitle());
            faCapitalLogFee.setStockSymbol(faStockTrading.getFaStrategy().getCode());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 增加 交易金额-印花税-手续费
        iFaMemberService.updateMemberBalance(faStockTrading.getMemberId(),
                faStockTrading.getTradingAmount().subtract(faStockTrading.getStampDuty()).subtract(faStockTrading.getTradingPoundage()), 0);

        // 判断卖出资金T+N 冻结资金 增加 交易金额-印花税-手续费
        int tn = Integer.parseInt(iFaRiskConfigService.getConfigValue("kq_dj", "1"));
        if (tn > 0){
            iFaMemberService.updateFaMemberFreezeProfit(faStockTrading.getMemberId(),
                    faStockTrading.getTradingAmount().subtract(faStockTrading.getStampDuty()).subtract(faStockTrading.getTradingPoundage()), 0);
        }

    }

    /**
     * 保存认缴流水
     * @param sgList
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaSgList sgList) throws Exception {
        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(sgList.getFaMember().getId());
        faCapitalLog.setMobile(sgList.getFaMember().getMobile());
        faCapitalLog.setName(sgList.getFaMember().getName());
        faCapitalLog.setSuperiorId(sgList.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(sgList.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(sgList.getFaMember().getSuperiorName());
        faCapitalLog.setContent("新股认缴");
        faCapitalLog.setMoney(sgList.getZqMoney());
        faCapitalLog.setBeforeMoney(sgList.getFaMember().getBalance());
        faCapitalLog.setLaterMoney(sgList.getFaMember().getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(6);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockSymbol(sgList.getCode());
        faCapitalLog.setStockName(sgList.getName());
        faCapitalLog.setDeleteFlag(0);
        faCapitalLog.setOrderId(sgList.getId());
        this.save(faCapitalLog);

        // 更新用户余额 减少 认缴金额
        iFaMemberService.updateMemberBalance(sgList.getUserId(), sgList.getZqMoney(), 1);

        // 更新用户冻结余额 减少 认缴金额
        iFaMemberService.updateFaMemberFreezeProfit(sgList.getUserId(), sgList.getZqMoney(), 1);
    }

    /**
     * 保存配售中签认缴流水
     * @param sgjiaoyi
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaSgjiaoyi sgjiaoyi) throws Exception {
        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(sgjiaoyi.getFaMember().getId());
        faCapitalLog.setMobile(sgjiaoyi.getFaMember().getMobile());
        faCapitalLog.setName(sgjiaoyi.getFaMember().getName());
        faCapitalLog.setSuperiorId(sgjiaoyi.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(sgjiaoyi.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(sgjiaoyi.getFaMember().getSuperiorName());
        faCapitalLog.setContent("新股认缴");
        faCapitalLog.setMoney(sgjiaoyi.getZqMoney());
        faCapitalLog.setBeforeMoney(sgjiaoyi.getFaMember().getBalance());
        faCapitalLog.setLaterMoney(sgjiaoyi.getFaMember().getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(6);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockSymbol(sgjiaoyi.getCode());
        faCapitalLog.setDeleteFlag(0);
        faCapitalLog.setOrderId(sgjiaoyi.getId());
        this.save(faCapitalLog);

        // 更新用户余额 减少 认缴金额
        iFaMemberService.updateMemberBalance(sgjiaoyi.getUserId(), sgjiaoyi.getZqMoney(), 1);
    }

    /**
     * 充值资金流水
     * @param recharge
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaRecharge recharge) throws Exception {
        FaMember faMember = iFaMemberService.getById(recharge.getUserId());

        // 充值
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent("充值");
        faCapitalLog.setMoney(recharge.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().add(faCapitalLog.getMoney()));
        faCapitalLog.setType(0);
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(recharge.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 增加 充值金额
        iFaMemberService.updateMemberBalance(recharge.getUserId(), recharge.getMoney(), 0);
    }

    /**
     * 提现资金流水
     * @param withdraw
     * @throws Exception
     */
    @Transactional
    @Override
    public void save(FaWithdraw withdraw) throws Exception {
        FaMember faMember = iFaMemberService.getById(withdraw.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent("提现");
        faCapitalLog.setMoney(withdraw.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(1);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(withdraw.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 减少 提现金额
        iFaMemberService.updateMemberBalance(withdraw.getUserId(), withdraw.getMoney(), 1);
    }

    /**
     * 提交配售，资金转冻结
     * @param faSgjiaoyi
     * @throws Exception
     */
    @Override
    public void savePeiShou(FaSgjiaoyi faSgjiaoyi) throws Exception {
        FaMember faMember = iFaMemberService.getById(faSgjiaoyi.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent("配售冻结");
        faCapitalLog.setMoney(faSgjiaoyi.getMoney());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(12);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockName(faSgjiaoyi.getName());
        faCapitalLog.setStockSymbol(faSgjiaoyi.getCode());
        faCapitalLog.setOrderId(faSgjiaoyi.getId());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额
        iFaMemberService.updateMemberBalance(faSgjiaoyi.getUserId(), faCapitalLog.getMoney(), 1);

        // 更新用户冻结资金 减少
        iFaMemberService.updateFaMemberFreezeProfit(faSgjiaoyi.getUserId(), faCapitalLog.getMoney(), 1);
    }

    /**
     * 保存大宗提交流水
     * @param faTradeApprove
     * @return
     * @throws Exception
     */
    @Override
    public FaCapitalLog save(FaTradeApprove faTradeApprove) throws Exception {
        FaMember faMember = iFaMemberService.getById(faTradeApprove.getUserId());

        // 提现
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faMember.getId());
        faCapitalLog.setMobile(faMember.getMobile());
        faCapitalLog.setName(faMember.getName());
        faCapitalLog.setSuperiorId(faMember.getSuperiorId());
        faCapitalLog.setSuperiorCode(faMember.getSuperiorCode());
        faCapitalLog.setSuperiorName(faMember.getSuperiorName());
        faCapitalLog.setContent("大宗冻结");
        faCapitalLog.setMoney(faTradeApprove.getShouldPayAmount());
        faCapitalLog.setBeforeMoney(faMember.getBalance());
        faCapitalLog.setLaterMoney(faMember.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(23);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setStockId(faTradeApprove.getStockId());
        faCapitalLog.setStockName(faTradeApprove.getStockName());
        faCapitalLog.setStockSymbol(faTradeApprove.getStockSymbol());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 更新用户余额 减少 应缴
        iFaMemberService.updateMemberBalance(faMember.getId(), faTradeApprove.getShouldPayAmount(), 1);

        // 更新用户冻结 减少 应缴
        iFaMemberService.updateFaMemberFreezeProfit(faMember.getId(), faTradeApprove.getShouldPayAmount(), 1);

        return faCapitalLog;
    }

    /**
     * 生成流水
     * @param faBTrading
     * @throws Exception
     */
    @Override
    public void createCapitalLog(FaBTrading faBTrading) throws Exception {
        // 买入流水
        if (1 == faBTrading.getTradingType()) {
            saveBuyCapitalLog(faBTrading);
        }
        // 卖出流水
        else if (2 == faBTrading.getTradingType()) {
            saveSellCapitalLog(faBTrading);
        }
    }

    /**
     * 买入流水
     * @param faBTrading
     */
    private void saveBuyCapitalLog(FaBTrading faBTrading) throws Exception {
        BigDecimal beforeMoney = faBTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faBTrading.getFaMember().getBalance().subtract(faBTrading.getTradingAmount());

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faBTrading.getFaMember().getId());
        faCapitalLog.setMobile(faBTrading.getFaMember().getMobile());
        faCapitalLog.setName(faBTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faBTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faBTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faBTrading.getFaMember().getSuperiorName());
        if (1 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("币买入");
        } else if (2 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("现货买入");
        } else if (3 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("合约买入");
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faBTrading.getTradingAmount());
        if (1 == faBTrading.getCoinType()) {
            faCapitalLog.setType(101);
        } else if (2 == faBTrading.getCoinType()) {
            faCapitalLog.setType(102);
        } else if (3 == faBTrading.getCoinType()) {
            faCapitalLog.setType(103);
        }
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faBTrading.getId());
        faCapitalLog.setStockId(faBTrading.getFaBCoin().getId());
        faCapitalLog.setStockName(faBTrading.getFaBCoin().getCoinName());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();
        if (faBTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faBTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faBTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faBTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faBTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faBTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faBTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faBTrading.getFaMember().getSuperiorName());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLogFee.setContent("币买入手续费");
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLogFee.setContent("现货买入手续费");
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLogFee.setContent("合约买入手续费");
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faBTrading.getTradingPoundage());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLogFee.setType(104);
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLogFee.setType(105);
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLogFee.setType(106);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLog.setOrderId(faBTrading.getId());
            faCapitalLog.setStockId(faBTrading.getFaBCoin().getId());
            faCapitalLog.setStockName(faBTrading.getFaBCoin().getCoinName());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 减少 交易金额+手续费
        iFaMemberService.updateMemberBalanceByType(faBTrading.getUserId(), faBTrading.getTradingAmount().add(faBTrading.getTradingPoundage()), Constants.subtract, faBTrading.getCoinType());

        // 更新用户冻结 减少 交易金额+手续费
        iFaMemberService.updateFaMemberFreezeProfitByType(faBTrading.getUserId(), faBTrading.getTradingAmount().add(faBTrading.getTradingPoundage()), Constants.subtract, faBTrading.getCoinType());
    }

    /**
     * 卖出流水
     * @param faBTrading
     */
    private void saveSellCapitalLog(FaBTrading faBTrading) throws Exception {
        BigDecimal beforeMoney = faBTrading.getFaMember().getBalance();
        BigDecimal laterMoney = faBTrading.getFaMember().getBalance().add(faBTrading.getTradingAmount());

        // 交易
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(faBTrading.getFaMember().getId());
        faCapitalLog.setMobile(faBTrading.getFaMember().getMobile());
        faCapitalLog.setName(faBTrading.getFaMember().getName());
        faCapitalLog.setSuperiorId(faBTrading.getFaMember().getSuperiorId());
        faCapitalLog.setSuperiorCode(faBTrading.getFaMember().getSuperiorCode());
        faCapitalLog.setSuperiorName(faBTrading.getFaMember().getSuperiorName());
        if (1 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("币平仓");
        } else if (2 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("现货平仓");
        } else if (3 == faBTrading.getCoinType()) {
            faCapitalLog.setContent("合约平仓");
        }
        faCapitalLog.setBeforeMoney(beforeMoney);
        faCapitalLog.setLaterMoney(laterMoney);
        faCapitalLog.setMoney(faBTrading.getTradingAmount());
        if (1 == faBTrading.getCoinType()) {
            faCapitalLog.setType(107);
        } else if (2 == faBTrading.getCoinType()) {
            faCapitalLog.setType(108);
        } else if (3 == faBTrading.getCoinType()) {
            faCapitalLog.setType(109);
        }
        faCapitalLog.setDirect(0);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setOrderId(faBTrading.getId());
        faCapitalLog.setStockId(faBTrading.getFaBCoin().getId());
        faCapitalLog.setStockName(faBTrading.getFaBCoin().getCoinName());
        faCapitalLog.setDeleteFlag(0);
        this.save(faCapitalLog);

        // 印花税
        FaCapitalLog faCapitalLogDuty = new FaCapitalLog();
        if (faBTrading.getStampDuty().compareTo(BigDecimal.ZERO) > 0) {
            beforeMoney = faCapitalLog.getLaterMoney();
            laterMoney = faCapitalLog.getLaterMoney().subtract(faBTrading.getStampDuty());

            faCapitalLogDuty.setUserId(faBTrading.getFaMember().getId());
            faCapitalLogDuty.setMobile(faBTrading.getFaMember().getMobile());
            faCapitalLogDuty.setName(faBTrading.getFaMember().getName());
            faCapitalLogDuty.setSuperiorId(faBTrading.getFaMember().getSuperiorId());
            faCapitalLogDuty.setSuperiorCode(faBTrading.getFaMember().getSuperiorCode());
            faCapitalLogDuty.setSuperiorName(faBTrading.getFaMember().getSuperiorName());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("币印花税");
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("现货印花税");
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("合约印花税");
            }
            faCapitalLogDuty.setBeforeMoney(beforeMoney);
            faCapitalLogDuty.setLaterMoney(laterMoney);
            faCapitalLogDuty.setMoney(faBTrading.getStampDuty());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLog.setType(110);
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLog.setType(111);
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLog.setType(112);
            }
            faCapitalLogDuty.setDirect(1);
            faCapitalLogDuty.setCreateTime(new Date());
            faCapitalLogDuty.setOrderId(faBTrading.getId());
            faCapitalLogDuty.setStockId(faBTrading.getFaBCoin().getId());
            faCapitalLogDuty.setStockName(faBTrading.getFaBCoin().getCoinName());
            faCapitalLogDuty.setDeleteFlag(0);
            this.save(faCapitalLogDuty);
        }

        // 手续费
        FaCapitalLog faCapitalLogFee = new FaCapitalLog();
        if (faBTrading.getTradingPoundage().compareTo(BigDecimal.ZERO) > 0) {
            beforeMoney = laterMoney;
            laterMoney = laterMoney.subtract(faBTrading.getTradingPoundage());

            faCapitalLogFee.setUserId(faBTrading.getFaMember().getId());
            faCapitalLogFee.setMobile(faBTrading.getFaMember().getMobile());
            faCapitalLogFee.setName(faBTrading.getFaMember().getName());
            faCapitalLogFee.setSuperiorId(faBTrading.getFaMember().getSuperiorId());
            faCapitalLogFee.setSuperiorCode(faBTrading.getFaMember().getSuperiorCode());
            faCapitalLogFee.setSuperiorName(faBTrading.getFaMember().getSuperiorName());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("币平仓手续费");
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("现货平仓手续费");
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLog.setContent("合约平仓手续费");
            }
            faCapitalLogFee.setBeforeMoney(beforeMoney);
            faCapitalLogFee.setLaterMoney(laterMoney);
            faCapitalLogFee.setMoney(faBTrading.getTradingPoundage());
            if (1 == faBTrading.getCoinType()) {
                faCapitalLog.setType(113);
            } else if (2 == faBTrading.getCoinType()) {
                faCapitalLog.setType(114);
            } else if (3 == faBTrading.getCoinType()) {
                faCapitalLog.setType(115);
            }
            faCapitalLogFee.setDirect(1);
            faCapitalLogFee.setCreateTime(new Date());
            faCapitalLogFee.setOrderId(faBTrading.getId());
            faCapitalLogFee.setStockId(faBTrading.getFaBCoin().getId());
            faCapitalLogFee.setStockName(faBTrading.getFaBCoin().getCoinName());
            faCapitalLogFee.setDeleteFlag(0);
            this.save(faCapitalLogFee);
        }

        // 更新用户余额 增加 交易金额-印花税-手续费
        iFaMemberService.updateMemberBalanceByType(faBTrading.getUserId(),
                faBTrading.getTradingAmount().subtract(faBTrading.getStampDuty()).subtract(faBTrading.getTradingPoundage()), Constants.ADD, faBTrading.getCoinType());

        // 判断卖出资金T+N 冻结资金 增加 交易金额-印花税-手续费
        int tn = Integer.parseInt(iFaRiskConfigService.getConfigValue("kq_dj", "1"));
        if (tn > 0){
            iFaMemberService.updateFaMemberFreezeProfitByType(faBTrading.getUserId(),
                    faBTrading.getTradingAmount().subtract(faBTrading.getStampDuty()).subtract(faBTrading.getTradingPoundage()), Constants.ADD, faBTrading.getCoinType());
        }
    }

}