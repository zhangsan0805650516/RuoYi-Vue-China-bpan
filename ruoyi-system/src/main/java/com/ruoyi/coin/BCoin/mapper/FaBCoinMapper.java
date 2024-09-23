package com.ruoyi.coin.BCoin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.coin.BCoin.domain.FaBCoin;

/**
 * 币种Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface FaBCoinMapper extends BaseMapper<FaBCoin>
{
    /**
     * 查询币种
     *
     * @param id 币种主键
     * @return 币种
     */
    public FaBCoin selectFaBCoinById(Integer id);

    /**
     * 查询币种列表
     *
     * @param faBCoin 币种
     * @return 币种集合
     */
    public List<FaBCoin> selectFaBCoinList(FaBCoin faBCoin);

    /**
     * 新增币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    public int insertFaBCoin(FaBCoin faBCoin);

    /**
     * 修改币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    public int updateFaBCoin(FaBCoin faBCoin);

    /**
     * 删除币种
     *
     * @param id 币种主键
     * @return 结果
     */
    public int deleteFaBCoinById(Integer id);

    /**
     * 批量删除币种
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaBCoinByIds(Integer[] ids);
}