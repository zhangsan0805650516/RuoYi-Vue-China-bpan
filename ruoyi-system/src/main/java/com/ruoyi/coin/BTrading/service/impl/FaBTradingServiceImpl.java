package com.ruoyi.coin.BTrading.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BTrading.mapper.FaBTradingMapper;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;

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
}