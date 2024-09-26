package com.ruoyi.coin.task.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.strategy.service.impl.ChinaStrategyServiceImpl;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.coin.task.service.BCoinTaskService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.BCoinUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.stockUtils.StockUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * B交易Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-23
 */
@Service
public class BCoinTaskServiceImpl implements BCoinTaskService
{

    @Autowired
    private IFaBCoinService iFaBCoinService;

    @Autowired
    private IFaBCoinSpotService iFaBCoinSpotService;

    /**
     * 刷新B种
     * @throws Exception
     */
    @Override
    public void updateBCoin() throws Exception {
        Document doc = Jsoup.connect("https://www.binance.com/zh-CN/markets/overview")
                //设置爬取超时时间
                .timeout(10000)
                //get请求
                .get();

        Elements tbody = doc.getElementsByClass("css-1pysja1");
        Elements rows = tbody.get(0).getElementsByClass("css-vlibs4");
        for (int i = 0; i < rows.size(); i++) {
            Element tr = rows.get(i);

            LambdaUpdateWrapper<FaBCoin> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaBCoin::getCoinName, tr.getElementsByClass("subtitle3").get(0).text());
            lambdaUpdateWrapper.set(FaBCoin::getCaiPrice, new BigDecimal(tr.getElementsByClass("body2").get(0).text().substring(1).replaceAll(",", "")));
            lambdaUpdateWrapper.set(FaBCoin::getCaiChangepercent, new BigDecimal(tr.getElementsByClass("subtitle3").get(1).text().replaceAll("/+", "").replaceAll("%", "")));
            String caiVolumeUsdt = tr.getElementsByClass("body2").get(1).text().substring(1).replaceAll(",", "");
            if (caiVolumeUsdt.contains("B")) {
                caiVolumeUsdt = caiVolumeUsdt.replaceAll("B", "");
                lambdaUpdateWrapper.set(FaBCoin::getCaiVolumeUsdt, new BigDecimal(caiVolumeUsdt).multiply(new BigDecimal(1000000000)));
            } else if (caiVolumeUsdt.contains("M")) {
                caiVolumeUsdt = caiVolumeUsdt.replaceAll("M", "");
                lambdaUpdateWrapper.set(FaBCoin::getCaiVolumeUsdt, new BigDecimal(caiVolumeUsdt).multiply(new BigDecimal(1000000)));
            } else {
                lambdaUpdateWrapper.set(FaBCoin::getCaiVolumeUsdt, new BigDecimal(caiVolumeUsdt));
            }
            lambdaUpdateWrapper.set(FaBCoin::getUpdateTime, new Date());
            iFaBCoinService.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 刷新现货交易对
     */
    @Override
    public void updateBCoinSpot() throws Exception {
        // 总数
        LambdaQueryWrapper<FaBCoinSpot> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaBCoinSpot::getDeleteFlag, 0);
        int count = (int) iFaBCoinSpotService.count(lambdaQueryWrapper);
        if (count > 0) {
            // 100一组
            int times = count / 100 + 1;
            for (int i = 0; i < times; i++) {
                // 现货代码集合
                String[] codeList = iFaBCoinSpotService.getBCoinSpotCodeList(i * 100, 100);
                if (codeList.length > 0) {
                    Thread thread = new Thread(updateBCoinSpotBatch(codeList));
                    thread.start();
                }
            }
        }
    }

    /**
     * 刷新现货交易对 分批次
     */
    public static TimerTask updateBCoinSpotBatch(String[] codeList) {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                StringBuilder list = new StringBuilder();
                list = new StringBuilder("[");
                for (String code : codeList) {
                    list.append("\"").append(code).append("\",");
                }
                String param = list.substring(0, list.length() - 1) + "]";

                String result = BCoinUtils.sendGet("https://data-api.binance.vision/api/v3/ticker?symbols=" + param);
                if (StringUtils.isNotEmpty(result)) {
                    JSONArray jsonArray = JSONArray.parseArray(result);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
            }
        };
    }

}