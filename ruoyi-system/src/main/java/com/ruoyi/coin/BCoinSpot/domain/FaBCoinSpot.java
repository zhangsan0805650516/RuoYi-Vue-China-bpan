package com.ruoyi.coin.BCoinSpot.domain;

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
 * 现货交易
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_b_coin_spot")
@ApiModel(value = "现货交易")
public class FaBCoinSpot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)")
    @TableField(exist = false)
    private Integer sortBy;

    @ApiModelProperty(value = "顺序(1正序 2倒序)")
    @TableField(exist = false)
    private Integer sort;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 币种名称 */
    @ApiModelProperty(value = "币种名称")
    @Excel(name = "币种名称")
    @TableField("coin_name")
    private String coinName;

    /** 币种英文名称 */
    @ApiModelProperty(value = "币种英文名称")
    @Excel(name = "币种英文名称")
    @TableField("coin_name_en")
    private String coinNameEn;

    /** 币种code */
    @ApiModelProperty(value = "币种code")
    @Excel(name = "币种code")
    @TableField("coin_code")
    private String coinCode;

    /** 币种介绍 */
    @ApiModelProperty(value = "币种介绍")
    @Excel(name = "币种介绍")
    @TableField("coin_describe")
    private String coinDescribe;

    /** 币种图标 */
    @ApiModelProperty(value = "币种图标")
    @Excel(name = "币种图标")
    @TableField("coin_icon")
    private String coinIcon;

    /** 币种排序 */
    @ApiModelProperty(value = "币种排序")
    @Excel(name = "币种排序")
    @TableField("coin_sort")
    private Integer coinSort;

    /** 发行总量 */
    @ApiModelProperty(value = "发行总量")
    @Excel(name = "发行总量")
    @TableField("coin_fx_total_num")
    private BigDecimal coinFxTotalNum;

    /** 流通总量 */
    @ApiModelProperty(value = "流通总量")
    @Excel(name = "流通总量")
    @TableField("coin_lt_total_num")
    private BigDecimal coinLtTotalNum;

    /** 发行价格 */
    @ApiModelProperty(value = "发行价格")
    @Excel(name = "发行价格")
    @TableField("coin_fx_price")
    private BigDecimal coinFxPrice;

    /** 发行时间 */
    @ApiModelProperty(value = "发行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("coin_fx_time")
    private Date coinFxTime;

    /** 白皮书链接 */
    @ApiModelProperty(value = "白皮书链接")
    @Excel(name = "白皮书链接")
    @TableField("coin_white_paper_link")
    private String coinWhitePaperLink;

    /** 官网链接 */
    @ApiModelProperty(value = "官网链接")
    @Excel(name = "官网链接")
    @TableField("coin_official_website_link")
    private String coinOfficialWebsiteLink;

    /** 提币手续费 */
    @ApiModelProperty(value = "提币手续费")
    @Excel(name = "提币手续费")
    @TableField("coin_withdraw_fee")
    private BigDecimal coinWithdrawFee;

    /** 提币最小数量 */
    @ApiModelProperty(value = "提币最小数量")
    @Excel(name = "提币最小数量")
    @TableField("coin_withdraw_limit_down")
    private BigDecimal coinWithdrawLimitDown;

    /** 提币最大数量 */
    @ApiModelProperty(value = "提币最大数量")
    @Excel(name = "提币最大数量")
    @TableField("coin_withdraw_limit_up")
    private BigDecimal coinWithdrawLimitUp;

    /** 显示状态 */
    @ApiModelProperty(value = "显示状态")
    @Excel(name = "显示状态")
    @TableField("is_show")
    private Integer isShow;

    /** 是否插针 */
    @ApiModelProperty(value = "是否插针")
    @Excel(name = "是否插针")
    @TableField("is_pin")
    private Integer isPin;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 采集-当前价格 */
    @ApiModelProperty(value = "采集-当前价格")
    @Excel(name = "采集-当前价格")
    @TableField("cai_price")
    private BigDecimal caiPrice;

    /** 采集-涨跌 */
    @ApiModelProperty(value = "采集-涨跌")
    @Excel(name = "采集-涨跌")
    @TableField("cai_pricechange")
    private BigDecimal caiPricechange;

    /** 采集-涨跌幅 */
    @ApiModelProperty(value = "采集-涨跌幅")
    @Excel(name = "采集-涨跌幅")
    @TableField("cai_changepercent")
    private BigDecimal caiChangepercent;

    /** 采集-买入价 */
    @ApiModelProperty(value = "采集-买入价")
    @Excel(name = "采集-买入价")
    @TableField("cai_buy")
    private BigDecimal caiBuy;

    /** 采集-卖出价 */
    @ApiModelProperty(value = "采集-卖出价")
    @Excel(name = "采集-卖出价")
    @TableField("cai_sell")
    private BigDecimal caiSell;

    /** 采集-昨收价 */
    @ApiModelProperty(value = "采集-昨收价")
    @Excel(name = "采集-昨收价")
    @TableField("cai_settlement")
    private BigDecimal caiSettlement;

    /** 采集-今开价 */
    @ApiModelProperty(value = "采集-今开价")
    @Excel(name = "采集-今开价")
    @TableField("cai_open")
    private BigDecimal caiOpen;

    /** 采集-最高价 */
    @ApiModelProperty(value = "采集-最高价")
    @Excel(name = "采集-最高价")
    @TableField("cai_high")
    private BigDecimal caiHigh;

    /** 采集-最低价 */
    @ApiModelProperty(value = "采集-最低价")
    @Excel(name = "采集-最低价")
    @TableField("cai_low")
    private BigDecimal caiLow;

    /** 采集-成交量 */
    @ApiModelProperty(value = "采集-成交量")
    @Excel(name = "采集-成交量")
    @TableField("cai_volume")
    private BigDecimal caiVolume;

    /** 采集-成交量u */
    @ApiModelProperty(value = "采集-成交量u")
    @Excel(name = "采集-成交量u")
    @TableField("cai_volume_usdt")
    private BigDecimal caiVolumeUsdt;

    /** 采集-成交额 */
    @ApiModelProperty(value = "采集-成交额")
    @Excel(name = "采集-成交额")
    @TableField("cai_amount")
    private BigDecimal caiAmount;

    /** 采集-成交额u */
    @ApiModelProperty(value = "采集-成交额u")
    @Excel(name = "采集-成交额u")
    @TableField("cai_amount_usdt")
    private BigDecimal caiAmountUsdt;

    /** T+N */
    @ApiModelProperty(value = "T+N")
    @Excel(name = "T+N")
    @TableField("t_n")
    private Integer tN;

}