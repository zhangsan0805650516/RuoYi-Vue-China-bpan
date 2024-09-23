package com.ruoyi.coin.BCoinContract.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BCoinContract.mapper.FaBCoinContractMapper;
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BCoinContract.service.IFaBCoinContractService;

/**
 * 合约交易Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBCoinContractServiceImpl extends ServiceImpl<FaBCoinContractMapper, FaBCoinContract> implements IFaBCoinContractService
{
    @Autowired
    private FaBCoinContractMapper faBCoinContractMapper;

    /**
     * 查询合约交易
     *
     * @param id 合约交易主键
     * @return 合约交易
     */
    @Override
    public FaBCoinContract selectFaBCoinContractById(Integer id)
    {
        return faBCoinContractMapper.selectFaBCoinContractById(id);
    }

    /**
     * 查询合约交易列表
     *
     * @param faBCoinContract 合约交易
     * @return 合约交易
     */
    @Override
    public List<FaBCoinContract> selectFaBCoinContractList(FaBCoinContract faBCoinContract)
    {
        faBCoinContract.setDeleteFlag(0);
        return faBCoinContractMapper.selectFaBCoinContractList(faBCoinContract);
    }

    /**
     * 新增合约交易
     *
     * @param faBCoinContract 合约交易
     * @return 结果
     */
    @Override
    public int insertFaBCoinContract(FaBCoinContract faBCoinContract)
    {
        faBCoinContract.setCreateTime(DateUtils.getNowDate());
        return faBCoinContractMapper.insertFaBCoinContract(faBCoinContract);
    }

    /**
     * 修改合约交易
     *
     * @param faBCoinContract 合约交易
     * @return 结果
     */
    @Override
    public int updateFaBCoinContract(FaBCoinContract faBCoinContract)
    {
        faBCoinContract.setUpdateTime(DateUtils.getNowDate());
        return faBCoinContractMapper.updateFaBCoinContract(faBCoinContract);
    }

    /**
     * 批量删除合约交易
     *
     * @param ids 需要删除的合约交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinContractByIds(Integer[] ids)
    {
        return faBCoinContractMapper.deleteFaBCoinContractByIds(ids);
    }

    /**
     * 删除合约交易信息
     *
     * @param id 合约交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinContractById(Integer id)
    {
        return faBCoinContractMapper.deleteFaBCoinContractById(id);
    }
}