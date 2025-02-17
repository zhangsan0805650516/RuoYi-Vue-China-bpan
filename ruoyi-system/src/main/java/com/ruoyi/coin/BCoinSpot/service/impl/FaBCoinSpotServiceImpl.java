package com.ruoyi.coin.BCoinSpot.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.mapper.FaBCoinSpotMapper;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.BCoinUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 现货交易Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class FaBCoinSpotServiceImpl extends ServiceImpl<FaBCoinSpotMapper, FaBCoinSpot> implements IFaBCoinSpotService
{
    @Autowired
    private FaBCoinSpotMapper faBCoinSpotMapper;

    /**
     * 查询现货交易
     *
     * @param id 现货交易主键
     * @return 现货交易
     */
    @Override
    public FaBCoinSpot selectFaBCoinSpotById(Integer id)
    {
        return faBCoinSpotMapper.selectFaBCoinSpotById(id);
    }

    /**
     * 查询现货交易列表
     *
     * @param faBCoinSpot 现货交易
     * @return 现货交易
     */
    @Override
    public List<FaBCoinSpot> selectFaBCoinSpotList(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setDeleteFlag(0);
        return faBCoinSpotMapper.selectFaBCoinSpotList(faBCoinSpot);
    }

    /**
     * 新增现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    @Override
    public int insertFaBCoinSpot(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setCreateTime(DateUtils.getNowDate());
        return faBCoinSpotMapper.insertFaBCoinSpot(faBCoinSpot);
    }

    /**
     * 修改现货交易
     *
     * @param faBCoinSpot 现货交易
     * @return 结果
     */
    @Override
    public int updateFaBCoinSpot(FaBCoinSpot faBCoinSpot)
    {
        faBCoinSpot.setUpdateTime(DateUtils.getNowDate());
        return faBCoinSpotMapper.updateFaBCoinSpot(faBCoinSpot);
    }

    /**
     * 批量删除现货交易
     *
     * @param ids 需要删除的现货交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinSpotByIds(Integer[] ids)
    {
        return faBCoinSpotMapper.deleteFaBCoinSpotByIds(ids);
    }

    /**
     * 删除现货交易信息
     *
     * @param id 现货交易主键
     * @return 结果
     */
    @Override
    public int deleteFaBCoinSpotById(Integer id)
    {
        return faBCoinSpotMapper.deleteFaBCoinSpotById(id);
    }

    /**
     * 查询现货列表
     * @param faBCoinSpot
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaBCoinSpot> getBCoinSpotList(FaBCoinSpot faBCoinSpot) throws Exception {
        if (null == faBCoinSpot.getSortBy() || null == faBCoinSpot.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaBCoinSpot> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        switch (faBCoinSpot.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiPrice);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiPrice);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiPricechange);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiAmount);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiAmount);
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
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiSettlement);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiSettlement);
                }
                break;
            // 今开价
            case 7:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiOpen);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiOpen);
                }
                break;
            // 最高价
            case 8:
                // 正序
                if (1 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaBCoinSpot::getCaiHigh);
                }
                // 倒序
                else if (2 == faBCoinSpot.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCaiHigh);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaBCoinSpot::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaBCoinSpot::getCreateTime);
        IPage<FaBCoinSpot> faBCoinSpotIPage = this.page(new Page<>(faBCoinSpot.getPage(), faBCoinSpot.getSize()), lambdaQueryWrapper);
        return faBCoinSpotIPage;
    }

    /**
     * 现货代码集合
     * @param start
     * @param end
     * @return
     */
    @Override
    public String[] getBCoinSpotCodeList(int start, int end) throws Exception {
        return faBCoinSpotMapper.getBCoinSpotCodeList(start, end);
    }

    /**
     * 查询现货详情
     * @param faBCoinSpot
     * @return
     * @throws Exception
     */
    @Override
    public FaBCoinSpot getBCoinSpotDetail(FaBCoinSpot faBCoinSpot) throws Exception {
        if (null == faBCoinSpot.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faBCoinSpot = this.getById(faBCoinSpot.getId());
        // 更新价格信息
        updateBCoinSpotDetail(faBCoinSpot);
        faBCoinSpot = this.getById(faBCoinSpot.getId());
        return faBCoinSpot;
    }

    /**
     * 更新价格信息
     * @param faBCoinSpot
     * @throws Exception
     */
    private void updateBCoinSpotDetail(FaBCoinSpot faBCoinSpot) throws Exception {
        String result = BCoinUtils.sendGet("https://data-api.binance.vision/api/v3/ticker?symbol=" + faBCoinSpot.getCoinCode());
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ObjectUtils.isNotEmpty(jsonObject)) {
                LambdaUpdateWrapper<FaBCoinSpot> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaBCoinSpot::getCoinCode, jsonObject.getString("symbol"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiPrice, jsonObject.getBigDecimal("lastPrice"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiPricechange, jsonObject.getBigDecimal("priceChange"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiChangepercent, jsonObject.getBigDecimal("priceChangePercent"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiSettlement, jsonObject.getBigDecimal("lastPrice"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiOpen, jsonObject.getBigDecimal("openPrice"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiHigh, jsonObject.getBigDecimal("highPrice"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiLow, jsonObject.getBigDecimal("lowPrice"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiVolume, jsonObject.getBigDecimal("volume"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getCaiVolumeUsdt, jsonObject.getBigDecimal("quoteVolume"));
                lambdaUpdateWrapper.set(FaBCoinSpot::getUpdateTime, new Date());
                SpringUtils.getBean(IFaBCoinSpotService.class).update(lambdaUpdateWrapper);
            }
        }
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
        FaBCoinSpot faBCoinSpot = this.getById(entrust.getCoinId());
        if (ObjectUtils.isNotEmpty(faBCoinSpot)) {
            String result = BCoinUtils.sendGet("https://data-api.binance.vision/api/v3/ticker/price?symbol=" + faBCoinSpot.getCoinCode());
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("price")) {
                    currentPrice = jsonObject.getBigDecimal("price");
                    LambdaUpdateWrapper<FaBCoinSpot> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaBCoinSpot::getId, faBCoinSpot.getId());
                    lambdaUpdateWrapper.set(FaBCoinSpot::getCaiPrice, currentPrice);
                    lambdaUpdateWrapper.set(FaBCoinSpot::getUpdateTime, new Date());
                    this.update(lambdaUpdateWrapper);
                }
            }
        }
        return currentPrice;
    }

    @Override
    public List<Map<String, String>> getBCoinSpotKline(FaBCoinSpot faBCoinSpot) throws Exception {
        if (null == faBCoinSpot.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        faBCoinSpot = this.getById(faBCoinSpot.getId());

        List<Map<String, String>> list = new ArrayList<>();
        String result = BCoinUtils.sendGet("https://www.binance.com/api/v3/uiKlines?limit=1000&symbol=" + faBCoinSpot.getCoinCode() + "&interval=1m");
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONArray.parseArray(result);
            if (!jsonArray.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map<String, String> map;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONArray array = jsonArray.getJSONArray(i);
                    map = new HashMap<>();
                    map.put("close", array.getString(4));
                    map.put("datetime", sdf.format(new Date(array.getString(0))));
                    map.put("high", array.getString(2));
                    map.put("low", array.getString(3));
                    map.put("open", array.getString(1));
                    map.put("timestamp", array.getString(0));
//                    map.put("volume", object.getString("vl"));
                    list.add(map);
                }
            }
        }
        return list;
    }
}