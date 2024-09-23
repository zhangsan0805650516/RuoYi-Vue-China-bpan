package com.ruoyi.coin.BEntrust.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;

/**
 * 委托Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface FaBEntrustMapper extends BaseMapper<FaBEntrust>
{
    /**
     * 查询委托
     *
     * @param id 委托主键
     * @return 委托
     */
    public FaBEntrust selectFaBEntrustById(Integer id);

    /**
     * 查询委托列表
     *
     * @param faBEntrust 委托
     * @return 委托集合
     */
    public List<FaBEntrust> selectFaBEntrustList(FaBEntrust faBEntrust);

    /**
     * 新增委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    public int insertFaBEntrust(FaBEntrust faBEntrust);

    /**
     * 修改委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    public int updateFaBEntrust(FaBEntrust faBEntrust);

    /**
     * 删除委托
     *
     * @param id 委托主键
     * @return 结果
     */
    public int deleteFaBEntrustById(Integer id);

    /**
     * 批量删除委托
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaBEntrustByIds(Integer[] ids);
}