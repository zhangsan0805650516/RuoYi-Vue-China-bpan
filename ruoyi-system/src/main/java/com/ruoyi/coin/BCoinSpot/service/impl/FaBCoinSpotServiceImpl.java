package com.ruoyi.coin.BCoinSpot.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BCoinSpot.mapper.FaBCoinSpotMapper;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;

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
}