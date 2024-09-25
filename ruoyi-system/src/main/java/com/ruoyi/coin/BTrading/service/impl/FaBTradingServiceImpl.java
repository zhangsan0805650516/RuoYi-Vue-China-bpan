package com.ruoyi.coin.BTrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.mapper.FaBTradingMapper;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 成交记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBTradingServiceImpl extends ServiceImpl<FaBTradingMapper, FaBTrading> implements IFaBTradingService
{
    @Autowired
    private FaBTradingMapper faBTradingMapper;

    /**
     * 查询成交记录
     *
     * @param id 成交记录主键
     * @return 成交记录
     */
    @Override
    public FaBTrading selectFaBTradingById(Integer id)
    {
        return faBTradingMapper.selectFaBTradingById(id);
    }

    /**
     * 查询成交记录列表
     *
     * @param faBTrading 成交记录
     * @return 成交记录
     */
    @Override
    public List<FaBTrading> selectFaBTradingList(FaBTrading faBTrading)
    {
        faBTrading.setDeleteFlag(0);
        return faBTradingMapper.selectFaBTradingList(faBTrading);
    }

    /**
     * 新增成交记录
     *
     * @param faBTrading 成交记录
     * @return 结果
     */
    @Override
    public int insertFaBTrading(FaBTrading faBTrading)
    {
        faBTrading.setCreateTime(DateUtils.getNowDate());
        return faBTradingMapper.insertFaBTrading(faBTrading);
    }

    /**
     * 修改成交记录
     *
     * @param faBTrading 成交记录
     * @return 结果
     */
    @Override
    public int updateFaBTrading(FaBTrading faBTrading)
    {
        faBTrading.setUpdateTime(DateUtils.getNowDate());
        return faBTradingMapper.updateFaBTrading(faBTrading);
    }

    /**
     * 批量删除成交记录
     *
     * @param ids 需要删除的成交记录主键
     * @return 结果
     */
    @Override
    public int deleteFaBTradingByIds(Integer[] ids)
    {
        return faBTradingMapper.deleteFaBTradingByIds(ids);
    }

    /**
     * 删除成交记录信息
     *
     * @param id 成交记录主键
     * @return 结果
     */
    @Override
    public int deleteFaBTradingById(Integer id)
    {
        return faBTradingMapper.deleteFaBTradingById(id);
    }

    /**
     * 查询成交列表
     * @param faBTrading
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBTrading> getBTradingList(FaBTrading faBTrading) throws Exception {
        if (null == faBTrading.getUserId() || null == faBTrading.getCoinType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBTrading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBTrading::getUserId, faBTrading.getUserId());
        lambdaQueryWrapper.eq(FaBTrading::getCoinType, faBTrading.getCoinType());
        lambdaQueryWrapper.eq(FaBTrading::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBTrading::getCreateTime);
        IPage<FaBTrading> faBTradingIPage = this.page(new Page<>(faBTrading.getPage(), faBTrading.getSize()), lambdaQueryWrapper);
        return faBTradingIPage;
    }

    /**
     * 生成买入交易
     * @param faBEntrust
     * @return
     * @throws Exception
     */
    @Override
    public FaBTrading createBuyTrading(FaBEntrust faBEntrust) throws Exception {
        FaBTrading faBTrading = new FaBTrading();
        faBTrading.setFaMember(faBEntrust.getFaMember());
        faBTrading.setFaBCoin(faBEntrust.getFaBCoin());
        faBTrading.setFaBCoinSpot(faBEntrust.getFaBCoinSpot());
        faBTrading.setFaBCoinContract(faBEntrust.getFaBCoinContract());

        // 交易流水号
        faBTrading.setTradeNo("T" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());
        // 委托id
        faBTrading.setEntrustId(faBEntrust.getId());
        // 用户id
        faBTrading.setUserId(faBEntrust.getUserId());
        // 交易品id
        faBTrading.setCoinId(faBEntrust.getCoinId());
        // 交易类型(1币 2现货 3合约 4理财)
        faBTrading.setCoinType(faBEntrust.getCoinType());
        // 成交数量
        faBTrading.setTradingNumber(faBEntrust.getTradeNumber());
        // 成交价格
        faBTrading.setTradingPrice(faBEntrust.getTradePrice());
        // 成交金额
        faBTrading.setTradingAmount(faBEntrust.getTradeAmount());
        // 买卖(1买 2卖)
        faBTrading.setTradingType(faBEntrust.getTradingType());
        // 方向(1买涨 2买跌)
        faBTrading.setTradeDirect(faBEntrust.getTradeDirect());
        // 手续费
        faBTrading.setTradingPoundage(faBEntrust.getTradingPoundage());
        // 印花税
        faBTrading.setStampDuty(faBEntrust.getStampDuty());
        faBTrading.setCreateTime(new Date());
        this.save(faBTrading);
        return faBTrading;
    }

    /**
     * 生成卖出交易
     * @param entrust
     * @return
     * @throws Exception
     */
    @Override
    public FaBTrading createSellTrading(FaBEntrust faBEntrust) throws Exception {
        FaBTrading faBTrading = new FaBTrading();
        faBTrading.setFaMember(faBEntrust.getFaMember());
        faBTrading.setFaBCoin(faBEntrust.getFaBCoin());

        // 交易流水号
        faBTrading.setTradeNo("T" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());
        // 委托id
        faBTrading.setEntrustId(faBEntrust.getId());
        // 用户id
        faBTrading.setUserId(faBEntrust.getUserId());
        // 交易品id
        faBTrading.setCoinId(faBEntrust.getCoinId());
        // 交易类型(1币 2现货 3合约 4理财)
        faBTrading.setCoinType(faBEntrust.getCoinType());
        // 成交数量
        faBTrading.setTradingNumber(faBEntrust.getTradeNumber());
        // 成交价格
        faBTrading.setTradingPrice(faBEntrust.getTradePrice());
        // 成交金额
        faBTrading.setTradingAmount(faBEntrust.getTradeAmount());
        // 买卖(1买 2卖)
        faBTrading.setTradingType(faBEntrust.getTradingType());
        // 方向(1买涨 2买跌)
        faBTrading.setTradeDirect(faBEntrust.getTradeDirect());
        // 手续费
        faBTrading.setTradingPoundage(faBEntrust.getTradingPoundage());
        // 印花税
        faBTrading.setStampDuty(faBEntrust.getStampDuty());
        faBTrading.setCreateTime(new Date());
        this.save(faBTrading);
        return faBTrading;
    }
}