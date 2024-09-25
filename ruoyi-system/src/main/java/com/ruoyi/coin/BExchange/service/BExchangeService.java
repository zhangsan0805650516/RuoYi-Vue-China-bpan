package com.ruoyi.coin.BExchange.service;

import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;

/**
 * B交易Service接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface BExchangeService
{

    /**
     * 买入B种
     * @param faBEntrust
     * @throws Exception
     */
    void buyBCoin(FaBEntrust faBEntrust) throws Exception;

    /**
     * 卖出B种
     * @param faBHoldDetail
     * @throws Exception
     */
    void sellBCoin(FaBHoldDetail faBHoldDetail) throws Exception;

    /**
     * 买入B现货
     * @param faBEntrust
     * @throws Exception
     */
    void buyBCoinSpot(FaBEntrust faBEntrust) throws Exception;

    /**
     * 卖出B现货
     * @param faBHoldDetail
     * @throws Exception
     */
    void sellBCoinSpot(FaBHoldDetail faBHoldDetail) throws Exception;

    /**
     * 买入B合约
     * @param faBEntrust
     * @throws Exception
     */
    void buyBCoinContract(FaBEntrust faBEntrust) throws Exception;

    /**
     * 卖出B合约
     * @param faBHoldDetail
     * @throws Exception
     */
    void sellBCoinContract(FaBHoldDetail faBHoldDetail) throws Exception;
}