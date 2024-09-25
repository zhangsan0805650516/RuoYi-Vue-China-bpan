package com.ruoyi.coin.BTrading.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BTrading.domain.FaBTrading;

/**
 * 成交记录Service接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IFaBTradingService extends IService<FaBTrading>
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
     * 批量删除成交记录
     *
     * @param ids 需要删除的成交记录主键集合
     * @return 结果
     */
    public int deleteFaBTradingByIds(Integer[] ids);

    /**
     * 删除成交记录信息
     *
     * @param id 成交记录主键
     * @return 结果
     */
    public int deleteFaBTradingById(Integer id);

    /**
     * 查询成交列表
     * @param faBTrading
     * @return
     * @throws Exception
     */
    IPage<FaBTrading> getBTradingList(FaBTrading faBTrading) throws Exception;

    /**
     * 生成交易
     * @param faBEntrust
     * @return
     * @throws Exception
     */
    FaBTrading createTrading(FaBEntrust faBEntrust) throws Exception;
}