package com.ruoyi.coin.BTrading.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.coin.BTrading.domain.FaBTrading;

/**
 * 成交记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface FaBTradingMapper extends BaseMapper<FaBTrading>
{
    /**
     * 查询成交记录
     *
     * @param id 成交记录主键
     * @return 成交记录
     */
    public FaBTrading selectFaBTradingById(Integer id);

    /**
     * 查询成交记录列表
     *
     * @param faBTrading 成交记录
     * @return 成交记录集合
     */
    public List<FaBTrading> selectFaBTradingList(FaBTrading faBTrading);

    /**
     * 新增成交记录
     *
     * @param faBTrading 成交记录
     * @return 结果
     */
    public int insertFaBTrading(FaBTrading faBTrading);

    /**
     * 修改成交记录
     *
     * @param faBTrading 成交记录
     * @return 结果
     */
    public int updateFaBTrading(FaBTrading faBTrading);

    /**
     * 删除成交记录
     *
     * @param id 成交记录主键
     * @return 结果
     */
    public int deleteFaBTradingById(Integer id);

    /**
     * 批量删除成交记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaBTradingByIds(Integer[] ids);
}