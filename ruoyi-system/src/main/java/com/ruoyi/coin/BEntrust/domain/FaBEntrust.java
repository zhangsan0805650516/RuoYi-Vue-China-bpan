package com.ruoyi.coin.BEntrust.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.FaMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 委托
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_b_entrust")
@ApiModel(value = "委托")
public class FaBEntrust extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private FaMember faMember;

    @TableField(exist = false)
    private FaBCoin faBCoin;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 委托流水号 */
    @ApiModelProperty(value = "委托流水号")
    @Excel(name = "委托流水号")
    @TableField("entrust_no")
    private String entrustNo;

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

    /** 交易类型(1币 2现货 3合约) */
    @ApiModelProperty(value = "交易类型(1币 2现货 3合约)")
    @Excel(name = "交易类型(1币 2现货 3合约)")
    @TableField("coin_type")
    private Integer coinType;

    /** 委托价格 */
    @ApiModelProperty(value = "委托价格")
    @Excel(name = "委托价格")
    @TableField("entrust_price")
    private BigDecimal entrustPrice;

    /** 委托数量 */
    @ApiModelProperty(value = "委托数量")
    @Excel(name = "委托数量")
    @TableField("entrust_number")
    private BigDecimal entrustNumber;

    /** 委托金额 */
    @ApiModelProperty(value = "委托金额")
    @Excel(name = "委托金额")
    @TableField("entrust_amount")
    private BigDecimal entrustAmount;

    /** 成交价格 */
    @ApiModelProperty(value = "成交价格")
    @Excel(name = "成交价格")
    @TableField("trade_price")
    private BigDecimal tradePrice;

    /** 交易数量 */
    @ApiModelProperty(value = "交易数量")
    @Excel(name = "交易数量")
    @TableField("trade_number")
    private BigDecimal tradeNumber;

    /** 成交金额 */
    @ApiModelProperty(value = "成交金额")
    @Excel(name = "成交金额")
    @TableField("trade_amount")
    private BigDecimal tradeAmount;

    /** 手续费 */
    @ApiModelProperty(value = "手续费")
    @Excel(name = "手续费")
    @TableField("trading_poundage")
    private BigDecimal tradingPoundage;

    /** 印花税 */
    @ApiModelProperty(value = "印花税")
    @Excel(name = "印花税")
    @TableField("stamp_duty")
    private BigDecimal stampDuty;

    /** 撤销时间 */
    @ApiModelProperty(value = "撤销时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "撤销时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("undo_time")
    private Date undoTime;

    /** 委托状态(0委托中 1成交 2撤回 3部分成交) */
    @ApiModelProperty(value = "委托状态(0委托中 1成交 2撤回 3部分成交)")
    @Excel(name = "委托状态(0委托中 1成交 2撤回 3部分成交)")
    @TableField("entrust_state")
    private Integer entrustState;

    /** 买卖(1买 2卖) */
    @ApiModelProperty(value = "买卖(1买 2卖)")
    @Excel(name = "买卖(1买 2卖)")
    @TableField("trading_type")
    private Integer tradingType;

    /** 方向(1买涨 2买跌) */
    @ApiModelProperty(value = "方向(1买涨 2买跌)")
    @Excel(name = "方向(1买涨 2买跌)")
    @TableField("trade_direct")
    private Integer tradeDirect;

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

}