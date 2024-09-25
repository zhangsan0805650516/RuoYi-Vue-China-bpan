package com.ruoyi.coin.BExchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.exchangeConfig.domain.FaExchangeConfig;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;
import com.ruoyi.coin.BCoinContract.service.IFaBCoinContractService;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BEntrust.service.IFaBEntrustService;
import com.ruoyi.coin.BExchange.service.BExchangeService;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.coin.BHoldDetail.service.IFaBHoldDetailService;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginMember;
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
public class BExchangeServiceImpl implements BExchangeService
{

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private IFaBCoinService iFaBCoinService;

    @Autowired
    private IFaBCoinSpotService iFaBCoinSpotService;

    @Autowired
    private IFaBCoinContractService iFaBCoinContractService;

    @Autowired
    private IFaBEntrustService iFaBEntrustService;

    @Autowired
    private IFaBTradingService iFaBTradingService;

    @Autowired
    private IFaBHoldDetailService iFaBHoldDetailService;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    /**
     * 买入B种
     * @param faBEntrust
     * @throws Exception
     */
    @Override
    public void buyBCoin(FaBEntrust faBEntrust) throws Exception {
        // 参数判断
        if (null == faBEntrust.getUserId() || null == faBEntrust.getCoinType() || null == faBEntrust.getCoinId() ||
                null == faBEntrust.getEntrustNumber() || faBEntrust.getEntrustNumber().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faBEntrust.getUserId());
        faBEntrust.setFaMember(faMember);
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // B种
        FaBCoin faBCoin = iFaBCoinService.getById(faBEntrust.getCoinId());
        faBEntrust.setFaBCoin(faBCoin);
        if (ObjectUtils.isEmpty(faBCoin)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // B种锁定判断
        if (1 == faBCoin.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 委托
        FaBEntrust entrust = iFaBEntrustService.createBuyEntrust(faBEntrust);

        // 交易
        FaBTrading faBTrading = iFaBTradingService.createBuyTrading(entrust);

        // 持仓
        FaBHoldDetail faBHoldDetail = iFaBHoldDetailService.createHoldDetail(faBTrading);

        // 流水
        iFaCapitalLogService.createCapitalLog(faBTrading);
    }

    @Override
    public void sellBCoin(FaBHoldDetail faBHoldDetail) throws Exception {
        // 参数判断
        if (null == faBHoldDetail.getUserId() || null == faBHoldDetail.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 查询持仓
        faBHoldDetail = iFaBHoldDetailService.getById(faBHoldDetail.getId());
        if (ObjectUtils.isEmpty(faBHoldDetail)) {
            throw new ServiceException(MessageUtils.message("member.hold.error"), HttpStatus.ERROR);
        }

        // 持仓状态
        if (1 == faBHoldDetail.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.hold.already.sell"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faBHoldDetail.getUserId());
        faBHoldDetail.setFaMember(faMember);
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // B种
        FaBCoin faBCoin = iFaBCoinService.getById(faBHoldDetail.getCoinId());
        faBHoldDetail.setFaBCoin(faBCoin);
        if (ObjectUtils.isEmpty(faBCoin)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 股票锁定判断
        if (1 == faBCoin.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 委托
        FaBEntrust entrust = iFaBEntrustService.createSellEntrust(faBHoldDetail);

        // 交易
        FaBTrading faBTrading = iFaBTradingService.createSellTrading(entrust);

        // 扣减持仓
        faBHoldDetail = iFaBHoldDetailService.subtractHoldDetail(faBTrading, faBHoldDetail);

        // 流水
        iFaCapitalLogService.createCapitalLog(faBTrading);
    }

    @Override
    public void buyBCoinSpot(FaBEntrust faBEntrust) throws Exception {
        // 参数判断
        if (null == faBEntrust.getUserId() || null == faBEntrust.getCoinType() || null == faBEntrust.getCoinId() ||
                null == faBEntrust.getEntrustNumber() || faBEntrust.getEntrustNumber().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember faMember = iFaMemberService.getById(faBEntrust.getUserId());
        faBEntrust.setFaMember(faMember);
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 用户交易锁定判断
        if (1 == faMember.getJingzhijiaoyi()) {
            throw new ServiceException(MessageUtils.message("member.trade.lock"), HttpStatus.ERROR);
        }

        // B种
        FaBCoinSpot faBCoinSpot = iFaBCoinSpotService.getById(faBEntrust.getCoinId());
        faBEntrust.setFaBCoinSpot(faBCoinSpot);
        if (ObjectUtils.isEmpty(faBCoinSpot)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // B种锁定判断
        if (1 == faBCoinSpot.getStatus()) {
            throw new ServiceException(MessageUtils.message("stock.trade.lock"), HttpStatus.ERROR);
        }

        // 委托
        FaBEntrust entrust = iFaBEntrustService.createBuyEntrust(faBEntrust);

        // 交易
        FaBTrading faBTrading = iFaBTradingService.createBuyTrading(entrust);

        // 持仓
        FaBHoldDetail faBHoldDetail = iFaBHoldDetailService.createHoldDetail(faBTrading);

        // 流水
        iFaCapitalLogService.createCapitalLog(faBTrading);
    }

    @Override
    public void sellBCoinSpot(FaBHoldDetail faBHoldDetail) throws Exception {

    }

    @Override
    public void buyBCoinContract(FaBEntrust faBEntrust) throws Exception {

    }

    @Override
    public void sellBCoinContract(FaBHoldDetail faBHoldDetail) throws Exception {

    }
}