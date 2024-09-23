package com.ruoyi.coin.BCoin.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BCoin.mapper.FaBCoinMapper;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;

/**
 * 币种Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBCoinServiceImpl extends ServiceImpl<FaBCoinMapper, FaBCoin> implements IFaBCoinService
{
    @Autowired
    private FaBCoinMapper faBCoinMapper;

    /**
     * 查询币种
     *
     * @param id 币种主键
     * @return 币种
     */
    @Override
    public FaBCoin selectFaBCoinById(Integer id)
    {
        return faBCoinMapper.selectFaBCoinById(id);
    }

    /**
     * 查询币种列表
     *
     * @param faBCoin 币种
     * @return 币种
     */
    @Override
    public List<FaBCoin> selectFaBCoinList(FaBCoin faBCoin)
    {
        faBCoin.setDeleteFlag(0);
        return faBCoinMapper.selectFaBCoinList(faBCoin);
    }

    /**
     * 新增币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    @Override
    public int insertFaBCoin(FaBCoin faBCoin)
    {
        faBCoin.setCreateTime(DateUtils.getNowDate());
        return faBCoinMapper.insertFaBCoin(faBCoin);
    }

    /**
     * 修改币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    @Override
    public int updateFaBCoin(FaBCoin faBCoin)
    {
        faBCoin.setUpdateTime(DateUtils.getNowDate());
        return faBCoinMapper.updateFaBCoin(faBCoin);
    }

    /**
     * 批量删除币种
     *
     * @param ids 需要删除的币种主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinByIds(Integer[] ids)
    {
        return faBCoinMapper.deleteFaBCoinByIds(ids);
    }

    /**
     * 删除币种信息
     *
     * @param id 币种主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinById(Integer id)
    {
        return faBCoinMapper.deleteFaBCoinById(id);
    }
}