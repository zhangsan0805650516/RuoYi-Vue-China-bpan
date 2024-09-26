package com.ruoyi.coin.BCoinContract.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;

/**
 * 合约交易Service接口
 *
 * @author ruoyi
 * @date 2024-09-23
 */
public interface IFaBCoinContractService extends IService<FaBCoinContract>
{
    /**
     * 查询合约交易
     *
     * @param id 合约交易主键
     * @return 合约交易
     */
    public FaBCoinContract selectFaBCoinContractById(Integer id);

    /**
     * 查询合约交易列表
     *
     * @param faBCoinContract 合约交易
     * @return 合约交易集合
     */
    public List<FaBCoinContract> selectFaBCoinContractList(FaBCoinContract faBCoinContract);

    /**
     * 新增合约交易
     *
     * @param faBCoinContract 合约交易
     * @return 结果
     */
    public int insertFaBCoinContract(FaBCoinContract faBCoinContract);

    /**
     * 修改合约交易
     *
     * @param faBCoinContract 合约交易
     * @return 结果
     */
    public int updateFaBCoinContract(FaBCoinContract faBCoinContract);

    /**
     * 批量删除合约交易
     *
     * @param ids 需要删除的合约交易主键集合
     * @return 结果
     */
    public int deleteFaBCoinContractByIds(Integer[] ids);

    /**
     * 删除合约交易信息
     *
     * @param id 合约交易主键
     * @return 结果
     */
    public int deleteFaBCoinContractById(Integer id);

    /**
     * 查询合约列表
     * @param faBCoinContract
     * @return
     * @throws Exception
     */
    IPage<FaBCoinContract> getBCoinContractList(FaBCoinContract faBCoinContract) throws Exception;

    /**
     * 获取实时价
     * @param entrust
     * @return
     * @throws Exception
     */
    BigDecimal getCurrentPrice(FaBEntrust entrust) throws Exception;
}