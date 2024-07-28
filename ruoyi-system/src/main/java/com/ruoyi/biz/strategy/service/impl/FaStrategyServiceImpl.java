package com.ruoyi.biz.strategy.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.shengou.domain.FaNewStock;
import com.ruoyi.biz.strategy.domain.FaStrategy;
import com.ruoyi.biz.strategy.mapper.FaStrategyMapper;
import com.ruoyi.biz.strategy.service.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.stockUtils.StockUtils;
import com.ruoyi.system.service.ISysConfigService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 策略Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class FaStrategyServiceImpl extends ServiceImpl<FaStrategyMapper, FaStrategy> implements IFaStrategyService
{
    @Autowired
    private FaStrategyMapper faStrategyMapper;

    @Autowired
    private IndiaStrategyService indiaStrategyService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private VietnamStrategyService vietnamStrategyService;

    @Autowired
    private ChinaStrategyService chinaStrategyService;

    @Autowired
    private ThailandStrategyService thailandStrategyService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private RedisCache redisCache;

    private static final String API_KEY = "55add7694cb14a9ba10296e6c1956f8a";

    /**
     * 查询策略
     *
     * @param id 策略主键
     * @return 策略
     */
    @Override
    public FaStrategy selectFaStrategyById(Integer id)
    {
        return faStrategyMapper.selectFaStrategyById(id);
    }

    /**
     * 查询策略列表
     *
     * @param faStrategy 策略
     * @return 策略
     */
    @Override
    public List<FaStrategy> selectFaStrategyList(FaStrategy faStrategy)
    {
        faStrategy.setDeleteFlag(0);
        faStrategy.setClassifyId(0);
        return faStrategyMapper.selectFaStrategyList(faStrategy);
    }

    /**
     * 新增策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaStrategy(FaStrategy faStrategy)
    {
        faStrategy.setCreateTime(DateUtils.getNowDate());
        // 校验code
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        int count = (int) this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new ServiceException(MessageUtils.message("stock.code.exists"), HttpStatus.ERROR);
        }

        return faStrategyMapper.insertFaStrategy(faStrategy);
    }

    /**
     * 修改策略
     *
     * @param faStrategy 策略
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaStrategy(FaStrategy faStrategy)
    {
        faStrategy.setUpdateTime(DateUtils.getNowDate());
        return faStrategyMapper.updateFaStrategy(faStrategy);
    }

    /**
     * 批量删除策略
     *
     * @param ids 需要删除的策略主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStrategyByIds(Integer[] ids)
    {
        return faStrategyMapper.deleteFaStrategyByIds(ids);
    }

    /**
     * 删除策略信息
     *
     * @param id 策略主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteFaStrategyById(Integer id)
    {
        return faStrategyMapper.deleteFaStrategyById(id);
    }

    /**
     * 查询大宗交易
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getDZStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 大宗打开
        lambdaQueryWrapper.eq(FaStrategy::getIsDz, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 查询抢筹列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getQCStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // VIP调研打开
        lambdaQueryWrapper.eq(FaStrategy::getVipQcStatus, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);

        // 把抢筹价格放入大宗价格
        if (!faStrategyIPage.getRecords().isEmpty()) {
            for (FaStrategy strategy : faStrategyIPage.getRecords()) {
                strategy.setZfaPrice(strategy.getVipQcPrice());
                strategy.setTotalZfaNum(strategy.getTotalQcNum());
                strategy.setZfaNum(strategy.getLeftQcNum());
            }
        }
        return faStrategyIPage;
    }

    /**
     * 股票是否存在
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public FaStrategy checkStock(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getId()) {
            lambdaQueryWrapper.eq(FaStrategy::getId, faStrategy.getId());
        }
        if (StringUtils.isNotEmpty(faStrategy.getCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        }
        if (StringUtils.isNotEmpty(faStrategy.getAllCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getAllCode, faStrategy.getAllCode());
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        FaStrategy selectOne = faStrategyMapper.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }
        return selectOne;
    }

    /**
     * 新股转入股票列表
     * @param faNewStock
     * @throws Exception
     */
    @Transactional
    @Override
    public FaStrategy addFromNewStock(FaNewStock faNewStock) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getCode, faNewStock.getCode());
        FaStrategy faStrategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faStrategy)) {
            faStrategy = new FaStrategy();
            faStrategy.setCode(faNewStock.getCode());
            faStrategy.setTitle(faNewStock.getName());
            faStrategy.setAllCode(allCodeFormat(faNewStock));
            faStrategy.setType(faNewStock.getSgType());
            faStrategy.setDeleteFlag(0);
            faStrategy.setCreateTime(DateUtils.getNowDate());
            this.save(faStrategy);
        }
        return faStrategy;
    }

    /**
     * 股票代码格式
     * @param faNewStock
     * @return
     * @throws Exception
     */
    private String allCodeFormat(FaNewStock faNewStock) throws Exception {
        if (1 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        } else if (2 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (3 == faNewStock.getSgType()) {
            return "sz" + faNewStock.getCode();
        } else if (4 == faNewStock.getSgType()) {
            return "bj" + faNewStock.getCode();
        } else if (5 == faNewStock.getSgType()) {
            return "sh" + faNewStock.getCode();
        }
        return faNewStock.getCode();
    }

    /**
     * 查询股票详情
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public FaStrategy getStockDetail(FaStrategy faStrategy) throws Exception {
        if (null == faStrategy.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getId()) {
            lambdaQueryWrapper.eq(FaStrategy::getId, faStrategy.getId());
        }
        if (StringUtils.isNotEmpty(faStrategy.getCode())) {
            lambdaQueryWrapper.eq(FaStrategy::getCode, faStrategy.getCode());
        }
        if (null != faStrategy.getType()) {
            lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        FaStrategy strategy = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(strategy)) {
            throw new ServiceException(MessageUtils.message("stock.not.exists"), HttpStatus.ERROR);
        }

        // 更新实时价格
        getCurrentPrice(strategy);

        // 再从数据库取一次
        strategy = this.getById(strategy.getId());

        // 买五卖五
        strategy = getBuySell(strategy);
        return strategy;
    }

    /**
     * 买五卖五
     * @param strategy
     * @throws Exception
     */
    private FaStrategy getBuySell(FaStrategy strategy) throws Exception {
        String currentInfo = StockUtils.getCurrentInfo(strategy.getAllCode());
        if (StringUtils.isNotEmpty(currentInfo)) {
            String[] info = currentInfo.split("~");
            if (info.length > 4) {
                strategy.setBuy1(new BigDecimal(info[9]));
                strategy.setBuy1Num(new BigDecimal(info[10]));
                strategy.setBuy2(new BigDecimal(info[11]));
                strategy.setBuy2Num(new BigDecimal(info[12]));
                strategy.setBuy3(new BigDecimal(info[13]));
                strategy.setBuy3Num(new BigDecimal(info[14]));
                strategy.setBuy4(new BigDecimal(info[15]));
                strategy.setBuy4Num(new BigDecimal(info[16]));
                strategy.setBuy5(new BigDecimal(info[17]));
                strategy.setBuy5Num(new BigDecimal(info[18]));
                strategy.setSell1(new BigDecimal(info[19]));
                strategy.setSell1Num(new BigDecimal(info[20]));
                strategy.setSell2(new BigDecimal(info[21]));
                strategy.setSell2Num(new BigDecimal(info[22]));
                strategy.setSell3(new BigDecimal(info[23]));
                strategy.setSell3Num(new BigDecimal(info[24]));
                strategy.setSell4(new BigDecimal(info[25]));
                strategy.setSell4Num(new BigDecimal(info[26]));
                strategy.setSell5(new BigDecimal(info[27]));
                strategy.setSell5Num(new BigDecimal(info[28]));

                strategy.setBuyTotalNum(strategy.getBuy1Num().add(strategy.getBuy2Num()).add(strategy.getBuy3Num()).add(strategy.getBuy4Num()).add(strategy.getBuy5Num()));
                strategy.setSellTotalNum(strategy.getSell1Num().add(strategy.getSell2Num()).add(strategy.getSell3Num()).add(strategy.getSell4Num()).add(strategy.getSell5Num()));
            }
        }
        return strategy;
    }

    /**
     * 搜素股票
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> searchStock(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faStrategy.getQueryString())) {
            // 交易所类型
//            if (null != faStrategy.getType()) {
//                lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
//            }
            lambdaQueryWrapper.and(i -> i.like(FaStrategy::getCode, faStrategy.getQueryString())
                    .or().like(FaStrategy::getAllCode, faStrategy.getQueryString())
                    .or().like(FaStrategy::getTitle, faStrategy.getQueryString())
            );
        }
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.last(" limit 20 ");
        List<FaStrategy> faStrategyList = this.list(lambdaQueryWrapper);
        return faStrategyList;
    }

    /**
     * 获取股票实时价格
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public BigDecimal getCurrentPrice(FaStrategy faStrategy) throws Exception {
        BigDecimal currentPrice = BigDecimal.ZERO;

        String country = configService.selectConfigByKey("country");
        // 印度
        if("india".equalsIgnoreCase(country)) {
            currentPrice = indiaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("vietnam".equalsIgnoreCase(country)) {
            currentPrice = vietnamStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("china".equalsIgnoreCase(country)) {
            currentPrice = chinaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("thailand".equalsIgnoreCase(country)) {
            currentPrice = thailandStrategyService.getStockCurrentPrice(faStrategy);
        }
        return currentPrice;
    }

    /**
     * 修改股票状态
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public AjaxResult changeStockStatus(FaStrategy faStrategy) throws Exception {
        if (StringUtils.isNotEmpty(faStrategy.getStatusType())) {
            LambdaUpdateWrapper<FaStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaStrategy::getId, faStrategy.getId());
            switch (faStrategy.getStatusType()) {
                case "zhiding":
                    lambdaUpdateWrapper.set(FaStrategy::getZhiding, faStrategy.getStatus());
                    break;
                case "status":
                    lambdaUpdateWrapper.set(FaStrategy::getStatus, faStrategy.getStatus());
                    break;
                case "currentStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getCurrentStatus, faStrategy.getStatus());
                    break;
                case "qcStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getQcStatus, faStrategy.getStatus());
                    break;
                case "isHide":
                    lambdaUpdateWrapper.set(FaStrategy::getIsHide, faStrategy.getStatus());
                    break;
                case "isPz":
                    lambdaUpdateWrapper.set(FaStrategy::getIsPz, faStrategy.getStatus());
                    break;
                case "isDz":
                    lambdaUpdateWrapper.set(FaStrategy::getIsDz, faStrategy.getStatus());
                    break;
                case "vipQcStatus":
                    lambdaUpdateWrapper.set(FaStrategy::getVipQcStatus, faStrategy.getStatus());
                    break;
                default:
                    break;
            }
            this.update(lambdaUpdateWrapper);
            return AjaxResult.success(MessageUtils.message("operation.success"));
        }
        return AjaxResult.error();
    }

    /**
     * 查询股票列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getStrategyList(FaStrategy faStrategy) throws Exception {
        if (null == faStrategy.getType() || null == faStrategy.getSortBy() || null == faStrategy.getSort()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
        lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
        switch (faStrategy.getSortBy()) {
            // 现价
            case 1:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiTrade);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiTrade);
                }
                break;
            // 涨跌
            case 2:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiPricechange);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiPricechange);
                }
                break;
            // 涨跌幅
            case 3:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangepercent);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangepercent);
                }
                break;
            // 成交额
            case 4:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiAmount);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiAmount);
                }
                break;
            // 换手率
            case 5:
                // 正序
                if (1 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByAsc(FaStrategy::getCaiChangeHands);
                }
                // 倒序
                else if (2 == faStrategy.getSort()) {
                    lambdaQueryWrapper.orderByDesc(FaStrategy::getCaiChangeHands);
                }
                break;
            default:
                break;
        }

        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 查询首页5大指数
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<FaStrategy> getIndexList(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 1);
        List<FaStrategy> list = this.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 查询涨跌对比
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Integer> getRiseAndFall(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        Map<String, Integer> map = redisCache.getCacheObject(Constants.RISE_AND_FALL);
        if (null == map) {
            map = new HashMap<>();
            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 涨数量
            lambdaQueryWrapper.gt(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long rise = this.count(lambdaQueryWrapper);
            map.put("rise", (int) rise);
            // 跌数量
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.lt(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long fall = this.count(lambdaQueryWrapper);
            map.put("fall", (int) fall);
            // 平数量
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaStrategy::getCaiPricechange, 0);
            lambdaQueryWrapper.eq(FaStrategy::getClassifyId, 0);
            long flat = this.count(lambdaQueryWrapper);
            map.put("flat", (int) flat);

            // 存入redis，保留5分钟
            redisCache.setCacheObject(Constants.RISE_AND_FALL, map, 5, TimeUnit.MINUTES);
        }
        return map;
    }

    /**
     * 板块涨跌幅
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getBkHotList(FaStrategy faStrategy) throws Exception {
        // 先从redis取
        JSONArray jsonArray = redisCache.getCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType());
        // redis里不存在，从接口取
        if (null == jsonArray || jsonArray.isEmpty()) {
            jsonArray = new JSONArray();
            // 接口token
            String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
            String result = HttpUtils.sendGet("http://47.120.74.91:5120/bkhotlst?pz=" + faStrategy.getBkType() + "&page=1&px=1&token=" + token);
            if (StringUtils.isNotEmpty(result)) {
                jsonArray = JSONArray.parse(result);
                if (!jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (ObjectUtils.isNotEmpty(jsonObject)) {
                            LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                            lambdaQueryWrapper.eq(FaStrategy::getCode, jsonObject.getString("stockcode"));
                            lambdaQueryWrapper.last(" limit 1 ");
                            FaStrategy fa = this.getOne(lambdaQueryWrapper);
                            if (ObjectUtils.isNotEmpty(fa)) {
                                jsonObject.put("id", fa.getId());
                                jsonObject.put("allCode", fa.getAllCode());
                            }
                        }
                    }
                    // 接口取成功，存入redis，保留5分钟
                    redisCache.setCacheObject(Constants.BK_HOT_LST + faStrategy.getBkType(), jsonArray, 5, TimeUnit.MINUTES);
                }
            }
        }
        return jsonArray;
    }

    /**
     * 板块涨跌幅-新浪 1地区 2板块 3概念
     * @param faStrategy bkType
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getBkHotListFromSina(FaStrategy faStrategy) throws Exception {
        // 地区 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=area
        // 行业 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=industry
        // 概念 http://money.finance.sina.com.cn/q/view/newFLJK.php?param=class
        JSONArray jsonArray = new JSONArray();
        switch (faStrategy.getBkType()) {
            case 1:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=area");
                break;
            case 2:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=industry");
                break;
            case 3:
                jsonArray = getBkHotListFromSina("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=class");
                break;
            default:
                break;
        }
        return jsonArray;
    }

    /**
     * 板块涨跌幅
     * @param url
     * @return
     * @throws Exception
     */
    private JSONArray getBkHotListFromSina(String url) throws Exception {
        String result = HttpUtils.sendGet(url);
        // todo
        return null;
    }

    /**
     * 资金流向
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getMoneyFlow(FaStrategy faStrategy) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // 日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        // 接口token
        String token = iFaRiskConfigService.getConfigValue("tokenp", "75896ca5bc76d3de");
        String result = HttpUtils.sendGet("http://47.120.74.91:5120/moneyflow_hsgt?date=" + date + "&page=1&px=1&token=" + token);
        if (StringUtils.isNotEmpty(result)) {
            JSONArray jsonArray = JSONArray.parse(result);
            if (!jsonArray.isEmpty()) {
                jsonObject = jsonArray.getJSONObject(0);
                // 更新到redis
                redisCache.setCacheObject("MoneyFlow", jsonObject);
            } else {
                // 从redis取最后保留的数据
                jsonObject = redisCache.getCacheObject("MoneyFlow");
            }
        }
        return jsonObject;
    }

    /**
     * 查询推荐列表
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public IPage<FaStrategy> getRecommendStrategy(FaStrategy faStrategy) throws Exception {
        LambdaQueryWrapper<FaStrategy> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != faStrategy.getType()) {
            lambdaQueryWrapper.eq(FaStrategy::getType, faStrategy.getType());
        }
        lambdaQueryWrapper.eq(FaStrategy::getZhiding, 1);
        lambdaQueryWrapper.eq(FaStrategy::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaStrategy::getCreateTime);
        IPage<FaStrategy> faStrategyIPage = this.page(new Page<>(faStrategy.getPage(), faStrategy.getSize()), lambdaQueryWrapper);
        return faStrategyIPage;
    }

    /**
     * 上证指数k线
     * @param faStrategy
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> getSHKline(FaStrategy faStrategy) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();

        String country = configService.selectConfigByKey("country");
        // 印度
        if("india".equalsIgnoreCase(country)) {
//            currentPrice = indiaStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("vietnam".equalsIgnoreCase(country)) {
//            currentPrice = vietnamStrategyService.getStockCurrentPrice(faStrategy);
        } else if ("china".equalsIgnoreCase(country)) {
            list = chinaStrategyService.getSHKline(faStrategy);
        } else if ("thailand".equalsIgnoreCase(country)) {
//            currentPrice = thailandStrategyService.getStockCurrentPrice(faStrategy);
        }
        return list;
    }

}