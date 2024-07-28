package com.ruoyi.biz.strategy.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 策略
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_strategy")
@ApiModel(value = "策略")
public class FaStrategy extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "板块类型(1地区 2板块 3概念)")
    @TableField(exist = false)
    private Integer bkType;

    @ApiModelProperty(value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率)")
    @TableField(exist = false)
    private Integer sortBy;

    @ApiModelProperty(value = "顺序(1正序 2倒序)")
    @TableField(exist = false)
    private Integer sort;

    @TableField(exist = false)
    private String interval;

    /** 状态类型 */
    @ApiModelProperty(value = "状态类型")
    @TableField(exist = false)
    private String statusType;

    /**
     * 买1
     */
    @TableField(exist = false)
    private BigDecimal buy1;

    /**
     * 买1量
     */
    @TableField(exist = false)
    private BigDecimal buy1Num;

    /**
     * 买2
     */
    @TableField(exist = false)
    private BigDecimal buy2;

    /**
     * 买2量
     */
    @TableField(exist = false)
    private BigDecimal buy2Num;

    /**
     * 买3
     */
    @TableField(exist = false)
    private BigDecimal buy3;

    /**
     * 买3量
     */
    @TableField(exist = false)
    private BigDecimal buy3Num;

    /**
     * 买4
     */
    @TableField(exist = false)
    private BigDecimal buy4;

    /**
     * 买4量
     */
    @TableField(exist = false)
    private BigDecimal buy4Num;

    /**
     * 买5
     */
    @TableField(exist = false)
    private BigDecimal buy5;

    /**
     * 买5量
     */
    @TableField(exist = false)
    private BigDecimal buy5Num;

    /**
     * 买总量
     */
    @TableField(exist = false)
    private BigDecimal buyTotalNum;

    /**
     * 卖1
     */
    @TableField(exist = false)
    private BigDecimal sell1;

    /**
     * 卖1量
     */
    @TableField(exist = false)
    private BigDecimal sell1Num;

    /**
     * 卖2
     */
    @TableField(exist = false)
    private BigDecimal sell2;

    /**
     * 卖2量
     */
    @TableField(exist = false)
    private BigDecimal sell2Num;

    /**
     * 卖3
     */
    @TableField(exist = false)
    private BigDecimal sell3;

    /**
     * 卖3量
     */
    @TableField(exist = false)
    private BigDecimal sell3Num;

    /**
     * 卖4
     */
    @TableField(exist = false)
    private BigDecimal sell4;

    /**
     * 卖4量
     */
    @TableField(exist = false)
    private BigDecimal sell4Num;

    /**
     * 卖5
     */
    @TableField(exist = false)
    private BigDecimal sell5;

    /**
     * 卖总量
     */
    @TableField(exist = false)
    private BigDecimal sellTotalNum;

    /**
     * 卖5量
     */
    @TableField(exist = false)
    private BigDecimal sell5Num;

    @TableField(exist = false)
    private String queryString;

    /** ID */
    @ApiModelProperty(value = "ID")
    @Excel(name = "ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID")
    @Excel(name = "分类ID")
    @TableField("classify_id")
    private Integer classifyId;

    /** 标题 */
    @ApiModelProperty(value = "标题")
    @Excel(name = "标题")
    @TableField("title")
    private String title;

    /** 英文标题 */
    @ApiModelProperty(value = "英文标题")
    @Excel(name = "英文标题")
    @TableField("title_en")
    private String titleEn;

    /** 编码 */
    @ApiModelProperty(value = "编码")
    @Excel(name = "编码")
    @TableField("code")
    private String code;

    /** 完整编码 */
    @ApiModelProperty(value = "完整编码")
    @Excel(name = "完整编码")
    @TableField("all_code")
    private String allCode;

    /** 编码前缀 */
    @ApiModelProperty(value = "编码前缀")
    @Excel(name = "编码前缀")
    @TableField("prefix_code")
    private String prefixCode;

    /** 条件编码 */
    @ApiModelProperty(value = "条件编码")
    @Excel(name = "条件编码")
    @TableField("condition_code")
    private String conditionCode;

    /** 今开 */
    @ApiModelProperty(value = "今开")
    @Excel(name = "今开")
    @TableField("today")
    private BigDecimal today;

    /** 昨收 */
    @ApiModelProperty(value = "昨收")
    @Excel(name = "昨收")
    @TableField("yesterday")
    private BigDecimal yesterday;

    /** 最高 */
    @ApiModelProperty(value = "最高")
    @Excel(name = "最高")
    @TableField("most_high")
    private BigDecimal mostHigh;

    /** 最低 */
    @ApiModelProperty(value = "最低")
    @Excel(name = "最低")
    @TableField("most_low")
    private BigDecimal mostLow;

    /** 股票数 */
    @ApiModelProperty(value = "股票数")
    @Excel(name = "股票数")
    @TableField("stock_num")
    private Integer stockNum;

    /** 当前价 */
    @ApiModelProperty(value = "当前价")
    @Excel(name = "当前价")
    @TableField("current_price")
    private BigDecimal currentPrice;

    /** 买入价 */
    @ApiModelProperty(value = "买入价")
    @Excel(name = "买入价")
    @TableField("buy_price")
    private BigDecimal buyPrice;

    /** 止盈价 */
    @ApiModelProperty(value = "止盈价")
    @Excel(name = "止盈价")
    @TableField("profit_price")
    private BigDecimal profitPrice;

    /** 涨止盈百分比 */
    @ApiModelProperty(value = "涨止盈百分比")
    @Excel(name = "涨止盈百分比")
    @TableField("stop_profit_price")
    private BigDecimal stopProfitPrice;

    /** 止损价 */
    @ApiModelProperty(value = "止损价")
    @Excel(name = "止损价")
    @TableField("lose_price")
    private BigDecimal losePrice;

    /** 涨止损百分比 */
    @ApiModelProperty(value = "涨止损百分比")
    @Excel(name = "涨止损百分比")
    @TableField("stop_lose_price")
    private BigDecimal stopLosePrice;

    /** 收益率 */
    @ApiModelProperty(value = "收益率")
    @Excel(name = "收益率")
    @TableField("yield")
    private BigDecimal yield;

    /** 推荐信用金 */
    @ApiModelProperty(value = "推荐信用金")
    @Excel(name = "推荐信用金")
    @TableField("recommend_credit_money")
    private BigDecimal recommendCreditMoney;

    /** 选中信用金 */
    @ApiModelProperty(value = "选中信用金")
    @Excel(name = "选中信用金")
    @TableField("select_credit_money")
    private BigDecimal selectCreditMoney;

    /** 赔率 */
    @ApiModelProperty(value = "赔率")
    @Excel(name = "赔率")
    @TableField("multiplying")
    private BigDecimal multiplying;

    /** 可买股数描述 */
    @ApiModelProperty(value = "可买股数描述")
    @Excel(name = "可买股数描述")
    @TableField("can_buy_desc")
    private String canBuyDesc;

    /** 自动续期描述 */
    @ApiModelProperty(value = "自动续期描述")
    @Excel(name = "自动续期描述")
    @TableField("auto_renewal_desc")
    private String autoRenewalDesc;

    /** 综合费用描述 */
    @ApiModelProperty(value = "综合费用描述")
    @Excel(name = "综合费用描述")
    @TableField("all_money_desc")
    private String allMoneyDesc;

    /** 综合费用 */
    @ApiModelProperty(value = "综合费用")
    @Excel(name = "综合费用")
    @TableField("all_money")
    private BigDecimal allMoney;

    /** 可买股数 */
    @ApiModelProperty(value = "可买股数")
    @Excel(name = "可买股数")
    @TableField("can_buy")
    private Integer canBuy;

    /** 增发数量 */
    @ApiModelProperty(value = "增发数量")
    @Excel(name = "增发数量")
    @TableField("make_bargain_num")
    private Integer makeBargainNum;

    /** 成交额单位亿 */
    @ApiModelProperty(value = "成交额单位亿")
    @Excel(name = "成交额单位亿")
    @TableField("make_bargain_money")
    private BigDecimal makeBargainMoney;

    /** 市盈率 */
    @ApiModelProperty(value = "市盈率")
    @Excel(name = "市盈率")
    @TableField("city_profit")
    private BigDecimal cityProfit;

    /** 市净率 */
    @ApiModelProperty(value = "市净率")
    @Excel(name = "市净率")
    @TableField("city_clean")
    private BigDecimal cityClean;

    /** 换手率 */
    @ApiModelProperty(value = "换手率")
    @Excel(name = "换手率")
    @TableField("change_hands")
    private BigDecimal changeHands;

    /** 最低单位亿 */
    @ApiModelProperty(value = "最低单位亿")
    @Excel(name = "最低单位亿")
    @TableField("min_value")
    private BigDecimal minValue;

    /** 自动续费设置:0=否,1=是 */
    @ApiModelProperty(value = "自动续费设置:0=否,1=是")
    @Excel(name = "自动续费设置:0=否,1=是")
    @TableField("is_auto_money")
    private Integer isAutoMoney;

    /** 市值 */
    @ApiModelProperty(value = "市值")
    @Excel(name = "市值")
    @TableField("city_value")
    private BigDecimal cityValue;

    /** 删除时间 */
    @ApiModelProperty(value = "删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime;

    /** 1 涨 2跌 */
    @ApiModelProperty(value = "1 涨 2跌")
    @Excel(name = "1 涨 2跌")
    @TableField("is_rise")
    private Integer isRise;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-当前价格")
    @Excel(name = "采集-当前价格")
    @TableField("cai_trade")
    private BigDecimal caiTrade;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌")
    @Excel(name = "采集-涨跌")
    @TableField("cai_pricechange")
    private BigDecimal caiPricechange;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-涨跌幅")
    @Excel(name = "采集-涨跌幅")
    @TableField("cai_changepercent")
    private BigDecimal caiChangepercent;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-买入价")
    @Excel(name = "采集-买入价")
    @TableField("cai_buy")
    private BigDecimal caiBuy;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-卖出价")
    @Excel(name = "采集-卖出价")
    @TableField("cai_sell")
    private BigDecimal caiSell;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-昨收价")
    @Excel(name = "采集-昨收价")
    @TableField("cai_settlement")
    private BigDecimal caiSettlement;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-今开价")
    @Excel(name = "采集-今开价")
    @TableField("cai_open")
    private BigDecimal caiOpen;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最高价")
    @Excel(name = "采集-最高价")
    @TableField("cai_high")
    private BigDecimal caiHigh;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-最低价")
    @Excel(name = "采集-最低价")
    @TableField("cai_low")
    private BigDecimal caiLow;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交量(手)")
    @Excel(name = "采集-成交量(手)")
    @TableField("cai_volume")
    private BigDecimal caiVolume;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-成交额(万)")
    @Excel(name = "采集-成交额(万)")
    @TableField("cai_amount")
    private BigDecimal caiAmount;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-换手率")
    @Excel(name = "采集-换手率")
    @TableField("cai_change_hands")
    private BigDecimal caiChangeHands;

    /** 采集-均价 */
    @ApiModelProperty(value = "采集-均价")
    @Excel(name = "采集-均价")
    @TableField("cai_average")
    private BigDecimal caiAverage;

    /** 采集-涨停价 */
    @ApiModelProperty(value = "采集-涨停价")
    @Excel(name = "采集-涨停价")
    @TableField("cai_limit_up_price")
    private BigDecimal caiLimitUpPrice;

    /** 采集-跌停价 */
    @ApiModelProperty(value = "采集-跌停价")
    @Excel(name = "采集-跌停价")
    @TableField("cai_limit_down_price")
    private BigDecimal caiLimitDownPrice;

    /** $column.columnComment */
    @ApiModelProperty(value = "采集-数据时间")
    @Excel(name = "采集-数据时间")
    @TableField("cai_ticktime")
    private Date caiTicktime;

    /** 0开启1关闭 */
    @ApiModelProperty(value = "0开启1关闭")
    @Excel(name = "0开启1关闭")
    @TableField("status")
    private Integer status;

    /** 0开启1关闭 */
    @ApiModelProperty(value = "0开启1关闭")
    @Excel(name = "0开启1关闭")
    @TableField("lock_status")
    private Integer lockStatus;

    /** 1:沪2深3创业4北交5科创6基金 */
    @ApiModelProperty(value = "1:沪2深3创业4北交5科创6基金")
    @Excel(name = "1:沪2深3创业4北交5科创6基金")
    @TableField("type")
    private Integer type;

    /** 当天平仓 */
    @ApiModelProperty(value = "当天平仓")
    @Excel(name = "当天平仓")
    @TableField("current_status")
    private Integer currentStatus;

    /** 抢筹状态 */
    @ApiModelProperty(value = "抢筹状态")
    @Excel(name = "抢筹状态")
    @TableField("qc_status")
    private Integer qcStatus;

    /** vip抢筹 */
    @ApiModelProperty(value = "vip抢筹")
    @Excel(name = "vip抢筹")
    @TableField("vip_qc_status")
    private Integer vipQcStatus;

    /** 置顶 */
    @ApiModelProperty(value = "置顶")
    @Excel(name = "置顶")
    @TableField("zhiding")
    private Integer zhiding;

    /** 0:非指数 1指数 */
    @ApiModelProperty(value = "0:非指数 1指数")
    @Excel(name = "0:非指数 1指数")
    @TableField("zs_type")
    private Integer zsType;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段")
    @Excel(name = "排序字段")
    @TableField("order_flag")
    private Integer orderFlag;

    /** 是否热门 */
    @ApiModelProperty(value = "是否热门")
    @Excel(name = "是否热门")
    @TableField("is_hot")
    private Integer isHot;

    /** 开启指数交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启指数交易 0关闭 1开启")
    @Excel(name = "开启指数交易 0关闭 1开启")
    @TableField("is_zs")
    private Integer isZs;

    /** 开启配资交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启配资交易 0关闭 1开启")
    @Excel(name = "开启配资交易 0关闭 1开启")
    @TableField("is_pz")
    private Integer isPz;

    /** 开启大宗交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启大宗交易 0关闭 1开启")
    @Excel(name = "开启大宗交易 0关闭 1开启")
    @TableField("is_dz")
    private Integer isDz;

    /** 开启增发交易 0关闭 1开启 */
    @ApiModelProperty(value = "开启增发交易 0关闭 1开启")
    @Excel(name = "开启增发交易 0关闭 1开启")
    @TableField("is_zfa")
    private Integer isZfa;

    /** 增发平仓 */
    @ApiModelProperty(value = "增发平仓")
    @Excel(name = "增发平仓")
    @TableField("ping_day")
    private Integer pingDay;

    /** 增发数量万起 */
    @ApiModelProperty(value = "增发数量万起")
    @Excel(name = "增发数量万起")
    @TableField("zfa_num")
    private Integer zfaNum;

    /** 增发价格 */
    @ApiModelProperty(value = "增发价格")
    @Excel(name = "增发价格")
    @TableField("zfa_price")
    private BigDecimal zfaPrice;

    /** 下单总股数 */
    @ApiModelProperty(value = "下单总股数")
    @Excel(name = "下单总股数")
    @TableField("total_zfa_num")
    private Integer totalZfaNum;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 是否隐藏 */
    @ApiModelProperty(value = "是否隐藏")
    @Excel(name = "是否隐藏")
    @TableField("is_hide")
    private Integer isHide;

    /** vip抢筹价格 */
    @ApiModelProperty(value = "vip抢筹价格")
    @Excel(name = "vip抢筹价格")
    @TableField("vip_qc_price")
    private BigDecimal vipQcPrice;

    /** vip抢筹密钥 */
    @ApiModelProperty(value = "vip抢筹密钥")
    @Excel(name = "vip抢筹密钥")
    @TableField("vip_qc_condition_code")
    private BigDecimal vipQcConditionCode;

    /** vip抢筹总额 */
    @ApiModelProperty(value = "vip抢筹总额")
    @Excel(name = "vip抢筹总额")
    @TableField("total_qc_num")
    private Integer totalQcNum;

    /** 剩余抢筹数量 */
    @ApiModelProperty(value = "剩余抢筹数量")
    @Excel(name = "剩余抢筹数量")
    @TableField("left_qc_num")
    private Integer leftQcNum;

}