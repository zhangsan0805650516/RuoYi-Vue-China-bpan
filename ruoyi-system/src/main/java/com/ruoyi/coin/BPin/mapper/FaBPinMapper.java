package com.ruoyi.coin.BPin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.coin.BPin.domain.FaBPin;

/**
 * 插针Mapper接口
 *
 * @author ruoyi
 * @date 2024-10-07
 */
public interface FaBPinMapper extends BaseMapper<FaBPin>
{
    /**
     * 查询插针
     *
     * @param id 插针主键
     * @return 插针
     */
    public FaBPin selectFaBPinById(Integer id);

    /**
     * 查询插针列表
     *
     * @param faBPin 插针
     * @return 插针集合
     */
    public List<FaBPin> selectFaBPinList(FaBPin faBPin);

    /**
     * 新增插针
     *
     * @param faBPin 插针
     * @return 结果
     */
    public int insertFaBPin(FaBPin faBPin);

    /**
     * 修改插针
     *
     * @param faBPin 插针
     * @return 结果
     */
    public int updateFaBPin(FaBPin faBPin);

    /**
     * 删除插针
     *
     * @param id 插针主键
     * @return 结果
     */
    public int deleteFaBPinById(Integer id);

    /**
     * 批量删除插针
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaBPinByIds(Integer[] ids);
}