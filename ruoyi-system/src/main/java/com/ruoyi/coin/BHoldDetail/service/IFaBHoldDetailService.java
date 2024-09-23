package com.ruoyi.coin.BHoldDetail.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;

/**
 * 持仓明细Service接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IFaBHoldDetailService extends IService<FaBHoldDetail>
{
    /**
     * 查询持仓明细
     *
     * @param id 持仓明细主键
     * @return 持仓明细
     */
    public FaBHoldDetail selectFaBHoldDetailById(Integer id);

    /**
     * 查询持仓明细列表
     *
     * @param faBHoldDetail 持仓明细
     * @return 持仓明细集合
     */
    public List<FaBHoldDetail> selectFaBHoldDetailList(FaBHoldDetail faBHoldDetail);

    /**
     * 新增持仓明细
     *
     * @param faBHoldDetail 持仓明细
     * @return 结果
     */
    public int insertFaBHoldDetail(FaBHoldDetail faBHoldDetail);

    /**
     * 修改持仓明细
     *
     * @param faBHoldDetail 持仓明细
     * @return 结果
     */
    public int updateFaBHoldDetail(FaBHoldDetail faBHoldDetail);

    /**
     * 批量删除持仓明细
     *
     * @param ids 需要删除的持仓明细主键集合
     * @return 结果
     */
    public int deleteFaBHoldDetailByIds(Integer[] ids);

    /**
     * 删除持仓明细信息
     *
     * @param id 持仓明细主键
     * @return 结果
     */
    public int deleteFaBHoldDetailById(Integer id);
}