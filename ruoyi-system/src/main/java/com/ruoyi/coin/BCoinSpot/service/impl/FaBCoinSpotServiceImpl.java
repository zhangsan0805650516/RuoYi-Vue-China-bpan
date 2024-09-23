package com.ruoyi.coin.BCoinSpot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.mapper.FaBCoinSpotMapper;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 现货交易Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBCoinSpotServiceImpl extends ServiceImpl<FaBCoinSpotMapper, FaBCoinSpot> implements IFaBCoinSpotService
{
    @Autowired
    private FaBCoinSpotMapper faBCoinSpotMapper;

    /**
     * 查询现货交易
     *
     * @param id 现货交易主键
     * @return 现货交易
     */
    @Override
    public FaBCoinSpot selectFaBCoinSpotById(Integer id)
    {
        return faBCoinSpotMapper.selectFaBCoinSpotById(id);
    }

    /**
     * 查询现货交易列表
     *
     * @param faBCoinSpot 现货交易
     * @return 现货交易
     */
    @Override
    public List<FaBCoinSpot> selectFaBCoinSpotList(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setDeleteFlag(0);
        return faBCoinSpotMapper.selectFaBCoinSpotList(faBCoinSpot);
    }

    /**
     * 新增现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    @Override
    public int insertFaBCoinSpot(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setCreateTime(DateUtils.getNowDate());
        return faBCoinSpotMapper.insertFaBCoinSpot(faBCoinSpot);
    }

    /**
     * 修改现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    @Override
    public int updateFaBCoinSpot(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setUpdateTime(DateUtils.getNowDate());
        return faBCoinSpotMapper.updateFaBCoinSpot(faBCoinSpot);
    }

    /**
     * 批量删除现货交易
     *
     * @param ids 需要删除的现货交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinSpotByIds(Integer[] ids)
    {
        return faBCoinSpotMapper.deleteFaBCoinSpotByIds(ids);
    }

    /**
     * 删除现货交易信息
     *
     * @param id 现货交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinSpotById(Integer id)
    {
        return faBCoinSpotMapper.deleteFaBCoinSpotById(id);
    }

    /**
     * 查询现货列表
     * @param faBCoinSpot
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBCoinSpot> getBCoinSpotList(FaBCoinSpot faBCoinSpot) throws Exception {
        if (null == faBCoinSpot.getSortBy() || null == faBCoinSpot.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBCoinSpot> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        switch (faBCoinSpot.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiPrice);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiPrice);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiPricechange);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiAmount);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiAmount);
                }
                break;
            // 换手率
//            case 5:
//                // 正序
//                if (1 == faStrategy.getSort()) {
//                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangeHands);
//                }
//                // 倒序
//                else if (2 == faStrategy.getSort()) {
//                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangeHands);
//                }
//                break;
            // 昨收价
            case 6:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiSettlement);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiSettlement);
                }
                break;
            // 今开价
            case 7:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiOpen);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiOpen);
                }
                break;
            // 最高价
            case 8:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiHigh);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiHigh);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaBCoinSpot::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCreateTime);
        IPage<FaBCoinSpot> faBCoinSpotIPage = this.page(new Page<>(faBCoinSpot.getPage(), faBCoinSpot.getSize()), lambdaQueryWrapper);
        return faBCoinSpotIPage;
    }
}