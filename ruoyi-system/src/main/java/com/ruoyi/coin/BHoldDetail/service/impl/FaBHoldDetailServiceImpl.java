package com.ruoyi.coin.BHoldDetail.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.stockHoldDetail.domain.FaStockHoldDetail;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BHoldDetail.mapper.FaBHoldDetailMapper;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.coin.BHoldDetail.service.IFaBHoldDetailService;

/**
 * 持仓明细Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBHoldDetailServiceImpl extends ServiceImpl<FaBHoldDetailMapper, FaBHoldDetail> implements IFaBHoldDetailService
{
    @Autowired
    private FaBHoldDetailMapper faBHoldDetailMapper;

    /**
     * 查询持仓明细
     *
     * @param id 持仓明细主键
     * @return 持仓明细
     */
    @Override
    public FaBHoldDetail selectFaBHoldDetailById(Integer id)
    {
        return faBHoldDetailMapper.selectFaBHoldDetailById(id);
    }

    /**
     * 查询持仓明细列表
     *
     * @param faBHoldDetail 持仓明细
     * @return 持仓明细
     */
    @Override
    public List<FaBHoldDetail> selectFaBHoldDetailList(FaBHoldDetail faBHoldDetail)
    {
        faBHoldDetail.setDeleteFlag(0);
        return faBHoldDetailMapper.selectFaBHoldDetailList(faBHoldDetail);
    }

    /**
     * 新增持仓明细
     *
     * @param faBHoldDetail 持仓明细
     * @return 结果
     */
    @Override
    public int insertFaBHoldDetail(FaBHoldDetail faBHoldDetail)
    {
        faBHoldDetail.setCreateTime(DateUtils.getNowDate());
        return faBHoldDetailMapper.insertFaBHoldDetail(faBHoldDetail);
    }

    /**
     * 修改持仓明细
     *
     * @param faBHoldDetail 持仓明细
     * @return 结果
     */
    @Override
    public int updateFaBHoldDetail(FaBHoldDetail faBHoldDetail)
    {
        faBHoldDetail.setUpdateTime(DateUtils.getNowDate());
        return faBHoldDetailMapper.updateFaBHoldDetail(faBHoldDetail);
    }

    /**
     * 批量删除持仓明细
     *
     * @param ids 需要删除的持仓明细主键
     * @return 结果
     */
    @Override
    public int deleteFaBHoldDetailByIds(Integer[] ids)
    {
        return faBHoldDetailMapper.deleteFaBHoldDetailByIds(ids);
    }

    /**
     * 删除持仓明细信息
     *
     * @param id 持仓明细主键
     * @return 结果
     */
    @Override
    public int deleteFaBHoldDetailById(Integer id)
    {
        return faBHoldDetailMapper.deleteFaBHoldDetailById(id);
    }

    /**
     * 查询持仓列表
     * @param faBHoldDetail
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBHoldDetail> getBHoldDetailList(FaBHoldDetail faBHoldDetail) throws Exception {
        if (null == faBHoldDetail.getUserId() || null == faBHoldDetail.getCoinType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBHoldDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBHoldDetail::getUserId, faBHoldDetail.getUserId());
        lambdaQueryWrapper.eq(FaBHoldDetail::getCoinType, faBHoldDetail.getCoinType());
        lambdaQueryWrapper.eq(FaBHoldDetail::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBHoldDetail::getCreateTime);
        IPage<FaBHoldDetail> faBHoldDetailIPage = this.page(new Page<>(faBHoldDetail.getPage(), faBHoldDetail.getSize()), lambdaQueryWrapper);
        return faBHoldDetailIPage;
    }

    /**
     * 生成持仓
     * @param faBTrading
     * @return
     * @throws Exception
     */
    @Override
    public FaBHoldDetail createHoldDetail(FaBTrading faBTrading) throws Exception {
        FaBHoldDetail faBHoldDetail = new FaBHoldDetail();

        // 持仓流水号
        faBHoldDetail.setHoldNo("H" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());
        // 买入交易id
        faBHoldDetail.setTradeBuyId(faBTrading.getId());
        // 用户id
        faBHoldDetail.setUserId(faBTrading.getUserId());
        // 现货/合约id
        faBHoldDetail.setCoinId(faBTrading.getCoinId());
        // 交易类型(1币 2现货 3合约 4理财)
        faBHoldDetail.setCoinType(faBTrading.getCoinType());
        // 持有数量
        faBHoldDetail.setHoldNumber(faBTrading.getTradingNumber());
        // 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发 8融券)
        faBHoldDetail.setHoldType(1);
        // T+N冻结数量
        faBHoldDetail.setFreezeNumber(faBTrading.getTradingNumber());
        // T+N剩余冻结时间
        faBHoldDetail.setFreezeDaysLeft(faBTrading.getFaBCoin().getTN());
        // T+N状态(0冻结中 1解冻)
        faBHoldDetail.setFreezeStatus(0);
        // 是否锁仓(0否1是)
        faBHoldDetail.setIsLock(0);
        // 买入价
        faBHoldDetail.setBuyPrice(faBTrading.getTradingPrice());
        // 买入手续费
        faBHoldDetail.setBuyPoundage(faBTrading.getTradingPoundage());
        // 买入时间
        faBHoldDetail.setBuyTime(faBTrading.getCreateTime());
        // 交易股数
        faBHoldDetail.setTradingNumber(faBTrading.getTradingNumber());
        // 方向(1买涨 2买跌)
        faBHoldDetail.setTradeDirect(faBTrading.getTradeDirect());
        // 状态（0持有 1清空）
        faBHoldDetail.setStatus(0);
        faBHoldDetail.setCreateTime(new Date());
        this.save(faBHoldDetail);
        return faBHoldDetail;
    }

    /**
     * 扣减持仓
     * @param faBTrading
     * @param faBHoldDetail
     * @return
     */
    @Override
    public FaBHoldDetail subtractHoldDetail(FaBTrading faBTrading, FaBHoldDetail faBHoldDetail) {
        // 是否锁仓
        if (1 == faBHoldDetail.getIsLock()) {
            throw new ServiceException(MessageUtils.message("member.hold.lock"), HttpStatus.ERROR);
        }

        // 是否冻结中
        if (faBHoldDetail.getFreezeDaysLeft() > 0) {
            throw new ServiceException("用户持仓T+" + faBHoldDetail.getFreezeDaysLeft() + "锁定", HttpStatus.ERROR);
        }

        // T+N冻结数量
        faBHoldDetail.setFreezeNumber(BigDecimal.ZERO);
        // T+N剩余冻结时间
        faBHoldDetail.setFreezeDaysLeft(0);
        // T+N状态(0冻结中 1解冻)
        faBHoldDetail.setFreezeStatus(1);
        // 持有数量
        faBHoldDetail.setHoldNumber(BigDecimal.ZERO);
        // 状态（0持有 1清空）
        faBHoldDetail.setStatus(1);

        // 卖出价
        faBHoldDetail.setSellPrice(faBTrading.getTradingPrice());
        // 卖出手续费
        faBHoldDetail.setSellPoundage(faBTrading.getTradingPoundage());
        // 卖出印花税
        faBHoldDetail.setSellStampDuty(faBTrading.getStampDuty());
        // 卖出时间
        faBHoldDetail.setSellTime(faBTrading.getCreateTime());

        // 盈亏
        BigDecimal profitLose = faBHoldDetail.getSellPrice().subtract(faBHoldDetail.getBuyPrice()).multiply(faBHoldDetail.getTradingNumber());
        faBHoldDetail.setProfitLose(profitLose);

        faBHoldDetail.setUpdateTime(new Date());
        this.updateFaBHoldDetail(faBHoldDetail);
        return faBHoldDetail;
    }
}