package com.ruoyi.coin.BHoldDetail.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
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
}