package com.ruoyi.coin.BTrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.mapper.FaBTradingMapper;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}