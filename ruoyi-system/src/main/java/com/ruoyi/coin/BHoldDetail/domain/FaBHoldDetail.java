package com.ruoyi.coin.BHoldDetail.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.FaMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 持仓明细
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_b_hold_detail")
@ApiModel(value = "持仓明细")
public class FaBHoldDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private BigDecimal currentPrice;

    @TableField(exist = false)
    private BigDecimal totalFee;

    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private FaBCoin faBCoin;

    @TableField(exist = false)
    private FaBCoinSpot faBCoinSpot;

    @TableField(exist = false)
    private FaBCoinContract faBCoinContract;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 持仓流水号 */
    @ApiModelProperty(value = "持仓流水号")
    @Excel(name = "持仓流水号")
    @TableField("hold_no")
    private String holdNo;

    /** 买入交易id */
    @ApiModelProperty(value = "买入交易id")
    @Excel(name = "买入交易id")
    @TableField("trade_buy_id")
    private Integer tradeBuyId;

    /** 卖出交易id */
    @ApiModelProperty(value = "卖出交易id")
    @Excel(name = "卖出交易id")
    @TableField("trade_sell_id")
    private Integer tradeSellId;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    @Excel(name = "用户id")
    @TableField("user_id")
    private Integer userId;

    /** 现货/合约id */
    @ApiModelProperty(value = "现货/合约id")
    @Excel(name = "现货/合约id")
    @TableField("coin_id")
    private Integer coinId;

    /** 交易类型(1币 2现货 3合约 4理财) */
    @ApiModelProperty(value = "交易类型(1币 2现货 3合约 4理财)")
    @Excel(name = "交易类型(1币 2现货 3合约 4理财)")
    @TableField("coin_type")
    private Integer coinType;

    /** 持有数量 */
    @ApiModelProperty(value = "持有数量")
    @Excel(name = "持有数量")
    @TableField("hold_number")
    private BigDecimal holdNumber;

    /** 持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发) */
    @ApiModelProperty(value = "持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发)")
    @Excel(name = "持仓类型(1普通交易 2大宗交易 3配资交易 4指数交易 5期货交易 6基金 7增发)")
    @TableField("hold_type")
    private Integer holdType;

    /** T+N冻结数量 */
    @ApiModelProperty(value = "T+N冻结数量")
    @Excel(name = "T+N冻结数量")
    @TableField("freeze_number")
    private BigDecimal freezeNumber;

    /** T+N剩余冻结时间 */
    @ApiModelProperty(value = "T+N剩余冻结时间")
    @Excel(name = "T+N剩余冻结时间")
    @TableField("freeze_days_left")
    private Integer freezeDaysLeft;

    /** T+N状态(0冻结中 1解冻) */
    @ApiModelProperty(value = "T+N状态(0冻结中 1解冻)")
    @Excel(name = "T+N状态(0冻结中 1解冻)")
    @TableField("freeze_status")
    private Integer freezeStatus;

    /** 是否锁仓(0否1是) */
    @ApiModelProperty(value = "是否锁仓(0否1是)")
    @Excel(name = "是否锁仓(0否1是)")
    @TableField("is_lock")
    private Integer isLock;

    /** 买入价 */
    @ApiModelProperty(value = "买入价")
    @Excel(name = "买入价")
    @TableField("buy_price")
    private BigDecimal buyPrice;

    /** 买入手续费 */
    @ApiModelProperty(value = "买入手续费")
    @Excel(name = "买入手续费")
    @TableField("buy_poundage")
    private BigDecimal buyPoundage;

    /** 买入时间 */
    @ApiModelProperty(value = "买入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "买入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("buy_time")
    private Date buyTime;

    /** 卖出价 */
    @ApiModelProperty(value = "卖出价")
    @Excel(name = "卖出价")
    @TableField("sell_price")
    private BigDecimal sellPrice;

    /** 卖出手续费 */
    @ApiModelProperty(value = "卖出手续费")
    @Excel(name = "卖出手续费")
    @TableField("sell_poundage")
    private BigDecimal sellPoundage;

    /** 卖出印花税 */
    @ApiModelProperty(value = "卖出印花税")
    @Excel(name = "卖出印花税")
    @TableField("sell_stamp_duty")
    private BigDecimal sellStampDuty;

    /** 卖出时间 */
    @ApiModelProperty(value = "卖出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "卖出时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("sell_time")
    private Date sellTime;

    /** 交易股数 */
    @ApiModelProperty(value = "交易股数")
    @Excel(name = "交易股数")
    @TableField("trading_number")
    private BigDecimal tradingNumber;

    /** 方向(1买涨 2买跌) */
    @ApiModelProperty(value = "方向(1买涨 2买跌)")
    @Excel(name = "方向(1买涨 2买跌)")
    @TableField("trade_direct")
    private Integer tradeDirect;

    /** 盈亏 */
    @ApiModelProperty(value = "盈亏")
    @Excel(name = "盈亏")
    @TableField("profit_lose")
    private BigDecimal profitLose;

    /** 状态（0持有 1清空） */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态", readConverterExp = "0=持有,1=清空")
    @TableField("status")
    private Integer status;

    /** 删除标记(0否1是) */
    @ApiModelProperty(value = "删除标记(0否1是)")
    @Excel(name = "删除标记(0否1是)")
    @TableField("delete_flag")
    private Integer deleteFlag;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

}