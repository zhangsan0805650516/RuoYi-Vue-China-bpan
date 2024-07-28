package com.ruoyi.biz.adv.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.adv.domain.FaAdv;

/**
 * 广告图Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-09
 */
public interface FaAdvMapper extends BaseMapper<FaAdv>
{
    /**
     * 查询广告图
     *
     * @param id 广告图主键
     * @return 广告图
     */
    public FaAdv selectFaAdvById(Integer id);

    /**
     * 查询广告图列表
     *
     * @param faAdv 广告图
     * @return 广告图集合
     */
    public List<FaAdv> selectFaAdvList(FaAdv faAdv);

    /**
     * 新增广告图
     *
     * @param faAdv 广告图
     * @return 结果
     */
    public int insertFaAdv(FaAdv faAdv);

    /**
     * 修改广告图
     *
     * @param faAdv 广告图
     * @return 结果
     */
    public int updateFaAdv(FaAdv faAdv);

    /**
     * 删除广告图
     *
     * @param id 广告图主键
     * @return 结果
     */
    public int deleteFaAdvById(Integer id);

    /**
     * 批量删除广告图
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaAdvByIds(Integer[] ids);
}