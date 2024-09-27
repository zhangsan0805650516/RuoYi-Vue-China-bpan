package com.ruoyi.coin.BCoin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.mapper.FaBCoinMapper;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 币种Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBCoinServiceImpl extends ServiceImpl<FaBCoinMapper, FaBCoin> implements IFaBCoinService
{
    @Autowired
    private FaBCoinMapper faBCoinMapper;

    /**
     * 查询币种
     *
     * @param id 币种主键
     * @return 币种
     */
    @Override
    public FaBCoin selectFaBCoinById(Integer id)
    {
        return faBCoinMapper.selectFaBCoinById(id);
    }

    /**
     * 查询币种列表
     *
     * @param faBCoin 币种
     * @return 币种
     */
    @Override
    public List<FaBCoin> selectFaBCoinList(FaBCoin faBCoin)
    {
        faBCoin.setDeleteFlag(0);
        return faBCoinMapper.selectFaBCoinList(faBCoin);
    }

    /**
     * 新增币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    @Override
    public int insertFaBCoin(FaBCoin faBCoin)
    {
        faBCoin.setCreateTime(DateUtils.getNowDate());
        return faBCoinMapper.insertFaBCoin(faBCoin);
    }

    /**
     * 修改币种
     *
     * @param faBCoin 币种
     * @return 结果
     */
    @Override
    public int updateFaBCoin(FaBCoin faBCoin)
    {
        faBCoin.setUpdateTime(DateUtils.getNowDate());
        return faBCoinMapper.updateFaBCoin(faBCoin);
    }

    /**
     * 批量删除币种
     *
     * @param ids 需要删除的币种主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinByIds(Integer[] ids)
    {
        return faBCoinMapper.deleteFaBCoinByIds(ids);
    }

    /**
     * 删除币种信息
     *
     * @param id 币种主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinById(Integer id)
    {
        return faBCoinMapper.deleteFaBCoinById(id);
    }

    /**
     * 查询B种列表
     * @param faBCoin
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBCoin> getBCoinList(FaBCoin faBCoin) throws Exception {
        if (null == faBCoin.getSortBy() || null == faBCoin.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBCoin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        switch (faBCoin.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiPrice);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiPrice);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiPricechange);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiAmount);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiAmount);
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
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiSettlement);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiSettlement);
                }
                break;
            // 今开价
            case 7:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiOpen);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiOpen);
                }
                break;
            // 最高价
            case 8:
                // 正序
                if (1 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoin::getCaiHigh);
                }
                // 倒序
                else if (2 == faBCoin.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoin::getCaiHigh);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaBCoin::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBCoin::getCreateTime);
        IPage<FaBCoin> faBCoinIPage = this.page(new Page<>(faBCoin.getPage(), faBCoin.getSize()), lambdaQueryWrapper);
        return faBCoinIPage;
    }

    /**
     * 查询B种详情
     * @param faBCoin
     * @return
     * @throws Exception
     */
    @Override
    public FaBCoin getBCoinDetail(FaBCoin faBCoin) throws Exception {
        if (null == faBCoin.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faBCoin = this.getById(faBCoin.getId());
        // 更新价格信息
        updateBCoinDetail(faBCoin);
        faBCoin = this.getById(faBCoin.getId());
        return faBCoin;
    }

    /**
     * 更新价格信息
     * @param faBCoin
     * @throws Exception
     */
    private BigDecimal updateBCoinDetail(FaBCoin faBCoin) throws Exception {
        Document doc = Jsoup.connect("https://www.binance.com/zh-CN/price/" + faBCoin.getCoinCode())
                //设置爬取超时时间
                .timeout(10000)
                //get请求
                .get();

        LambdaUpdateWrapper<FaBCoin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaBCoin::getId, faBCoin.getId());
        BigDecimal currentPrice = new BigDecimal(doc.getElementsByClass("css-1bwgsh3").get(0).text().replace("/$", "").replaceAll(",", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getCaiPrice, doc.getElementsByClass("css-1bwgsh3").get(0).text().replace("/$", "").replaceAll(",", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getCaiPricechange, doc.getElementsByClass("css-1tu5w8v").get(0).text().replace("/$", "").replaceAll(",", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getCaiChangepercent, doc.getElementsByClass("css-12i542z").get(0).text().replace("/+", "").replaceAll("%", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getCaiLow, doc.getElementsByClass("css-7g4hlk").get(1).text().replace("低: $", "").replaceAll(",", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getCaiHigh, doc.getElementsByClass("css-7g4hlk").get(2).text().replace("高: $", "").replaceAll(",", "").trim());
        lambdaUpdateWrapper.set(FaBCoin::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
        return currentPrice;
    }

    /**
     * 获取实时价
     * @param entrust
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getCurrentPrice(FaBEntrust entrust) throws Exception {
        BigDecimal currentPrice = BigDecimal.ZERO;
        FaBCoin faBCoin = this.getById(entrust.getCoinId());
        currentPrice = updateBCoinDetail(faBCoin);
        return currentPrice;
    }

}