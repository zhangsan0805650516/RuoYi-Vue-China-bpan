package com.ruoyi.coin.BCoin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;

/**
 * 币种Service接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IFaBCoinService extends IService<FaBCoin>
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
     * 批量删除币种
     *
     * @param ids 需要删除的币种主键集合
     * @return 结果
     */
    public int deleteFaBCoinByIds(Integer[] ids);

    /**
     * 删除币种信息
     *
     * @param id 币种主键
     * @return 结果
     */
    public int deleteFaBCoinById(Integer id);

    /**
     * 查询B种列表
     * @param faBCoin
     * @return
     * @throws Exception
     */
    IPage<FaBCoin> getBCoinList(FaBCoin faBCoin) throws Exception;

    /**
     * 查询B种详情
     * @param faBCoin
     * @return
     * @throws Exception
     */
    FaBCoin getBCoinDetail(FaBCoin faBCoin) throws Exception;

    /**
     * 获取实时价
     * @param entrust
     * @return
     * @throws Exception
     */
    BigDecimal getCurrentPrice(FaBEntrust entrust) throws Exception;

    /**
     * 查询B种K线
     * @param faBCoin
     * @return
     * @throws Exception
     */
    List<Map<String, String>> getBCoinKline(FaBCoin faBCoin) throws Exception;
}