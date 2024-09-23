package com.ruoyi.coin.BCoinSpot.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;

/**
 * 现货交易Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface FaBCoinSpotMapper extends BaseMapper<FaBCoinSpot>
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
     * 删除现货交易
     *
     * @param id 现货交易主键
     * @return 结果
     */
    public int deleteFaBCoinSpotById(Integer id);

    /**
     * 批量删除现货交易
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaBCoinSpotByIds(Integer[] ids);
}