package com.ruoyi.coin.BEntrust.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.coin.BEntrust.mapper.FaBEntrustMapper;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BEntrust.service.IFaBEntrustService;

/**
 * 委托Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBEntrustServiceImpl extends ServiceImpl<FaBEntrustMapper, FaBEntrust> implements IFaBEntrustService
{

    @Autowired
    private FaBEntrustMapper faBEntrustMapper;

    /**
     * 查询委托
     *
     * @param id 委托主键
     * @return 委托
     */
    @Override
    public FaBEntrust selectFaBEntrustById(Integer id)
    {
        return faBEntrustMapper.selectFaBEntrustById(id);
    }

    /**
     * 查询委托列表
     *
     * @param faBEntrust 委托
     * @return 委托
     */
    @Override
    public List<FaBEntrust> selectFaBEntrustList(FaBEntrust faBEntrust)
    {
        faBEntrust.setDeleteFlag(0);
        return faBEntrustMapper.selectFaBEntrustList(faBEntrust);
    }

    /**
     * 新增委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    @Override
    public int insertFaBEntrust(FaBEntrust faBEntrust)
    {
        faBEntrust.setCreateTime(DateUtils.getNowDate());
        return faBEntrustMapper.insertFaBEntrust(faBEntrust);
    }

    /**
     * 修改委托
     *
     * @param faBEntrust 委托
     * @return 结果
     */
    @Override
    public int updateFaBEntrust(FaBEntrust faBEntrust)
    {
        faBEntrust.setUpdateTime(DateUtils.getNowDate());
        return faBEntrustMapper.updateFaBEntrust(faBEntrust);
    }

    /**
     * 批量删除委托
     *
     * @param ids 需要删除的委托主键
     * @return 结果
     */
    @Override
    public int deleteFaBEntrustByIds(Integer[] ids)
    {
        return faBEntrustMapper.deleteFaBEntrustByIds(ids);
    }

    /**
     * 删除委托信息
     *
     * @param id 委托主键
     * @return 结果
     */
    @Override
    public int deleteFaBEntrustById(Integer id)
    {
        return faBEntrustMapper.deleteFaBEntrustById(id);
    }

    /**
     * 查询委托列表
     * @param faBEntrust
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBEntrust> getBEntrustList(FaBEntrust faBEntrust) throws Exception {
        if (null == faBEntrust.getUserId() || null == faBEntrust.getCoinType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBEntrust> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBEntrust::getUserId, faBEntrust.getUserId());
        lambdaQueryWrapper.eq(FaBEntrust::getCoinType, faBEntrust.getCoinType());
        lambdaQueryWrapper.eq(FaBEntrust::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBEntrust::getCreateTime);
        IPage<FaBEntrust> faBEntrustIPage = this.page(new Page<>(faBEntrust.getPage(), faBEntrust.getSize()), lambdaQueryWrapper);
        return faBEntrustIPage;
    }

}