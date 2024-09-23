package com.ruoyi.coin.BCoinSpot.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;

/**
 * 现货交易Service接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IFaBCoinSpotService extends IService<FaBCoinSpot>
{
    /**
     * 查询现货交易
     *
     * @param id 现货交易主键
     * @return 现货交易
     */
    public FaBCoinSpot selectFaBCoinSpotById(Integer id);

    /**
     * 查询现货交易列表
     *
     * @param faBCoinSpot 现货交易
     * @return 现货交易集合
     */
    public List<FaBCoinSpot> selectFaBCoinSpotList(FaBCoinSpot faBCoinSpot);

    /**
     * 新增现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    public int insertFaBCoinSpot(FaBCoinSpot faBCoinSpot);

    /**
     * 修改现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    public int updateFaBCoinSpot(FaBCoinSpot faBCoinSpot);

    /**
     * 批量删除现货交易
     *
     * @param ids 需要删除的现货交易主键集合
     * @return 结果
     */
    public int deleteFaBCoinSpotByIds(Integer[] ids);

    /**
     * 删除现货交易信息
     *
     * @param id 现货交易主键
     * @return 结果
     */
    public int deleteFaBCoinSpotById(Integer id);
}