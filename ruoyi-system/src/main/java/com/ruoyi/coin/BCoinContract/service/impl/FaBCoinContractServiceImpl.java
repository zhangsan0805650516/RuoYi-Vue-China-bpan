package com.ruoyi.coin.BCoinContract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BCoinContract.mapper.FaBCoinContractMapper;
import com.ruoyi.coin.BCoinContract.service.IFaBCoinContractService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询合约列表
     * @param faBCoinContract
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBCoinContract> getBCoinContractList(FaBCoinContract faBCoinContract) throws Exception {
        if (null == faBCoinContract.getSortBy() || null == faBCoinContract.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBCoinContract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        switch (faBCoinContract.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiPrice);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiPrice);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiPricechange);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiAmount);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiAmount);
                }
                break;
            // 换手率
//            case 5:
//                // 正序
//                if (1 == faStrategy.getSort()) {
//                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangeHands);
//                }
//                // 倒序
//                else if (2 == faStrategy.getSort()) {
//                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangeHands);
//                }
//                break;
            // 昨收价
            case 6:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiSettlement);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiSettlement);
                }
                break;
            // 今开价
            case 7:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiOpen);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiOpen);
                }
                break;
            // 最高价
            case 8:
                // 正序
                if (1 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinContract::getCaiHigh);
                }
                // 倒序
                else if (2 == faBCoinContract.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCaiHigh);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaBCoinContract::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBCoinContract::getCreateTime);
        IPage<FaBCoinContract> faBCoinContractIPage = this.page(new Page<>(faBCoinContract.getPage(), faBCoinContract.getSize()), lambdaQueryWrapper);
        return faBCoinContractIPage;
    }
}