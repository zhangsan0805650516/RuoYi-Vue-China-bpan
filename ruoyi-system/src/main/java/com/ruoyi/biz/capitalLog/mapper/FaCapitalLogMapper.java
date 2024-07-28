package com.ruoyi.biz.capitalLog.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;

/**
 * 资金记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-01-07
 */
public interface FaCapitalLogMapper extends BaseMapper<FaCapitalLog>
{
    /**
     * 查询资金记录
     *
     * @param id 资金记录主键
     * @return 资金记录
     */
    public FaCapitalLog selectFaCapitalLogById(Integer id);

    /**
     * 查询资金记录列表
     *
     * @param faCapitalLog 资金记录
     * @return 资金记录集合
     */
    public List<FaCapitalLog> selectFaCapitalLogList(FaCapitalLog faCapitalLog);

    /**
     * 新增资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    public int insertFaCapitalLog(FaCapitalLog faCapitalLog);

    /**
     * 修改资金记录
     *
     * @param faCapitalLog 资金记录
     * @return 结果
     */
    public int updateFaCapitalLog(FaCapitalLog faCapitalLog);

    /**
     * 删除资金记录
     *
     * @param id 资金记录主键
     * @return 结果
     */
    public int deleteFaCapitalLogById(Integer id);

    /**
     * 批量删除资金记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFaCapitalLogByIds(Integer[] ids);
}