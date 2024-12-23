package com.ruoyi.coin.BPin.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BPin.mapper.FaBPinMapper;
import com.ruoyi.coin.BPin.domain.FaBPin;
import com.ruoyi.coin.BPin.service.IFaBPinService;

/**
 * 插针Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-07
 */
@Service
public class FaBPinServiceImpl extends ServiceImpl<FaBPinMapper, FaBPin> implements IFaBPinService
{
    @Autowired
    private FaBPinMapper faBPinMapper;

    /**
     * 查询插针
     *
     * @param id 插针主键
     * @return 插针
     */
    @Override
    public FaBPin selectFaBPinById(Integer id)
    {
        return faBPinMapper.selectFaBPinById(id);
    }

    /**
     * 查询插针列表
     *
     * @param faBPin 插针
     * @return 插针
     */
    @Override
    public List<FaBPin> selectFaBPinList(FaBPin faBPin)
    {
        faBPin.setDeleteFlag(0);
        return faBPinMapper.selectFaBPinList(faBPin);
    }

    /**
     * 新增插针
     *
     * @param faBPin 插针
     * @return 结果
     */
    @Override
    public int insertFaBPin(FaBPin faBPin)
    {
        faBPin.setCreateTime(DateUtils.getNowDate());
        return faBPinMapper.insertFaBPin(faBPin);
    }

    /**
     * 修改插针
     *
     * @param faBPin 插针
     * @return 结果
     */
    @Override
    public int updateFaBPin(FaBPin faBPin)
    {
        faBPin.setUpdateTime(DateUtils.getNowDate());
        return faBPinMapper.updateFaBPin(faBPin);
    }

    /**
     * 批量删除插针
     *
     * @param ids 需要删除的插针主键
     * @return 结果
     */
    @Override
    public int deleteFaBPinByIds(Integer[] ids)
    {
        return faBPinMapper.deleteFaBPinByIds(ids);
    }

    /**
     * 删除插针信息
     *
     * @param id 插针主键
     * @return 结果
     */
    @Override
    public int deleteFaBPinById(Integer id)
    {
        return faBPinMapper.deleteFaBPinById(id);
    }
}