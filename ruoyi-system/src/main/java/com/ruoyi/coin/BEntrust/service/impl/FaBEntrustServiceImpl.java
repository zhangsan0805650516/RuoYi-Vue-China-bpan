package com.ruoyi.coin.BEntrust.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.stockTrading.domain.FaStockTrading;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BEntrust.mapper.FaBEntrustMapper;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BEntrust.service.IFaBEntrustService;

/**
 * 委托Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBEntrustServiceImpl extends ServiceImpl<FaBEntrustMapper, FaBEntrust> implements IFaBEntrustService
{

    @Autowired
    private FaBEntrustMapper faBEntrustMapper;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    /**
     * 查询委托
     *
     * @param id 委托主键
     * @return 委托
     */
    @Override
    public FaBEntrust selectFaBEntrustById(Integer id)
    {
        return faBEntrustMapper.selectFaBEntrustById(id);
    }

    /**
     * 查询委托列表
     *
     * @param faBEntrust 委托
     * @return 委托
     */
    @Override
    public List<FaBEntrust> selectFaBEntrustList(FaBEntrust faBEntrust)
    {
        faBEntrust.setDeleteFlag(0);
        return faBEntrustMapper.selectFaBEntrustList(faBEntrust);
    }

    /**
     * 新增委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    @Override
    public int insertFaBEntrust(FaBEntrust faBEntrust)
    {
        faBEntrust.setCreateTime(DateUtils.getNowDate());
        return faBEntrustMapper.insertFaBEntrust(faBEntrust);
    }

    /**
     * 修改委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    @Override
    public int updateFaBEntrust(FaBEntrust faBEntrust)
    {
        faBEntrust.setUpdateTime(DateUtils.getNowDate());
        return faBEntrustMapper.updateFaBEntrust(faBEntrust);
    }

    /**
     * 批量删除委托
     *
     * @param ids 需要删除的委托主键
     * @return 结果
     */
    @Override
    public int deleteFaBEntrustByIds(Integer[] ids)
    {
        return faBEntrustMapper.deleteFaBEntrustByIds(ids);
    }

    /**
     * 删除委托信息
     *
     * @param id 委托主键
     * @return 结果
     */
    @Override
    public int deleteFaBEntrustById(Integer id)
    {
        return faBEntrustMapper.deleteFaBEntrustById(id);
    }

    /**
     * 查询委托列表
     * @param faBEntrust
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBEntrust> getBEntrustList(FaBEntrust faBEntrust) throws Exception {
        if (null == faBEntrust.getUserId() || null == faBEntrust.getCoinType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBEntrust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBEntrust::getUserId, faBEntrust.getUserId());
        lambdaQueryWrapper.eq(FaBEntrust::getCoinType, faBEntrust.getCoinType());
        lambdaQueryWrapper.eq(FaBEntrust::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBEntrust::getCreateTime);
        IPage<FaBEntrust> faBEntrustIPage = this.page(new Page<>(faBEntrust.getPage(), faBEntrust.getSize()), lambdaQueryWrapper);
        return faBEntrustIPage;
    }

    /**
     * 生成买入委托
     * @param faBEntrust
     * @return
     * @throws Exception
     */
    @Override
    public FaBEntrust createBuyEntrust(FaBEntrust faBEntrust) throws Exception {
        FaBEntrust entrust = new FaBEntrust();
        entrust.setFaMember(faBEntrust.getFaMember());
        entrust.setFaBCoin(faBEntrust.getFaBCoin());

        // 委托流水号
        entrust.setEntrustNo("E" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());
        // 用户
        entrust.setUserId(faBEntrust.getUserId());
        // B种id
        entrust.setCoinId(faBEntrust.getCoinId());
        // 交易类型(1币 2现货 3合约 4理财)
        entrust.setCoinType(faBEntrust.getCoinType());
        // 委托价格
        entrust.setEntrustPrice(faBEntrust.getFaBCoin().getCaiPrice());
        // 委托数量
        entrust.setEntrustNumber(faBEntrust.getEntrustNumber());
        // 委托金额
        entrust.setEntrustAmount(entrust.getEntrustPrice().multiply(entrust.getEntrustNumber()).setScale(2, RoundingMode.HALF_UP));
        // 成交价格
        entrust.setTradePrice(entrust.getEntrustPrice());
        // 成交数量
        entrust.setTradeNumber(entrust.getEntrustNumber());
        // 成交金额
        entrust.setTradeAmount(entrust.getEntrustAmount());

        // 买入手续费率
        String maiFee = iFaRiskConfigService.getConfigValue("mai_fee", "0.0001");
        // 手续费
        BigDecimal fee = entrust.getTradeAmount().multiply(new BigDecimal(maiFee)).setScale(2, RoundingMode.HALF_UP);
        entrust.setTradingPoundage(fee);

        // 总金额
        BigDecimal totalAmount = entrust.getTradeAmount().add(fee);
        // 余额判断
        if (faBEntrust.getFaMember().getBalance().compareTo(totalAmount) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 委托状态(0委托中 1成交 2撤回 3部分成交)
        entrust.setEntrustState(1);
        // 买卖(1买 2卖)
        entrust.setTradingType(faBEntrust.getTradingType());
        // 方向(1买涨 2买跌)
        entrust.setTradeDirect(faBEntrust.getTradeDirect());
        entrust.setCreateTime(new Date());
        this.save(entrust);
        return entrust;
    }

    /**
     * 生成卖出委托
     * @param faBHoldDetail
     * @return
     * @throws Exception
     */
    @Override
    public FaBEntrust createSellEntrust(FaBHoldDetail faBHoldDetail) throws Exception {
        FaBEntrust entrust = new FaBEntrust();
        entrust.setFaMember(faBHoldDetail.getFaMember());
        entrust.setFaBCoin(faBHoldDetail.getFaBCoin());

        // 委托流水号
        entrust.setEntrustNo("E" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());
        // 用户
        entrust.setUserId(faBHoldDetail.getFaMember().getId());
        // B种id
        entrust.setCoinId(faBHoldDetail.getFaBCoin().getId());
        // 交易类型(1币 2现货 3合约 4理财)
        entrust.setCoinType(faBHoldDetail.getCoinType());
        // 委托价格
        entrust.setEntrustPrice(faBHoldDetail.getFaBCoin().getCaiPrice());
        // 委托数量
        entrust.setEntrustNumber(faBHoldDetail.getHoldNumber());
        // 委托金额
        entrust.setEntrustAmount(entrust.getEntrustPrice().multiply(faBHoldDetail.getHoldNumber()).setScale(2, RoundingMode.HALF_UP));
        // 成交价格
        entrust.setTradePrice(entrust.getEntrustPrice());
        // 成交数量
        entrust.setTradeNumber(entrust.getEntrustNumber());
        // 成交金额
        entrust.setTradeAmount(entrust.getEntrustAmount());

        // 卖出手续费率
        String sellFee = iFaRiskConfigService.getConfigValue("sell_fee", "0.0001");
        // 卖出手续费
        BigDecimal tradingPoundage = entrust.getTradeAmount().multiply(new BigDecimal(sellFee)).setScale(2, RoundingMode.HALF_UP);
        entrust.setTradingPoundage(tradingPoundage);
        // 卖出印花税
        String yhFee = iFaRiskConfigService.getConfigValue("stamp_duty", "0.0002");
        // 印花税
        BigDecimal duty = entrust.getTradeAmount().multiply(new BigDecimal(yhFee)).setScale(2, RoundingMode.HALF_UP);
        entrust.setStampDuty(duty);

        // 委托状态(0委托中 1成交 2撤回 3部分成交)
        entrust.setEntrustState(1);
        // 买卖(1买 2卖)
        entrust.setTradingType(Constants.SELL);
        // 方向(1买涨 2买跌)
        entrust.setTradeDirect(Constants.BUY_UP);
        entrust.setCreateTime(new Date());
        this.save(entrust);
        return entrust;
    }

}