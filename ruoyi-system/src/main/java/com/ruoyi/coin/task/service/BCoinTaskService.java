package com.ruoyi.coin.task.service;

/**
 * B定时任务接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface BCoinTaskService
{

    /**
     * 刷新B种
     * @throws Exception
     */
    void updateBCoin() throws Exception;

    /**
     * 刷新现货交易对
     */
    void updateBCoinSpot() throws Exception;
}