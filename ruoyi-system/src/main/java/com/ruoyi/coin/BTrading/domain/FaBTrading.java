package com.ruoyi.coin.BTrading.domain;

import java.math.BigDecimal;
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
 * 成交记录
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_b_trading")
@ApiModel(value = "成交记录")
public class FaBTrading extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 交易流水号 */
    @ApiModelProperty(value = "交易流水号")
    @Excel(name = "交易流水号")
    @TableField("trade_no")
    private String tradeNo;

    /** 委托id */
    @ApiModelProperty(value = "委托id")
    @Excel(name = "委托id")
    @TableField("entrust_id")
    private Integer entrustId;

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

    /** 成交数量 */
    @ApiModelProperty(value = "成交数量")
    @Excel(name = "成交数量")
    @TableField("trading_number")
    private BigDecimal tradingNumber;

    /** 成交价格 */
    @ApiModelProperty(value = "成交价格")
    @Excel(name = "成交价格")
    @TableField("trading_price")
    private BigDecimal tradingPrice;

    /** 成交金额 */
    @ApiModelProperty(value = "成交金额")
    @Excel(name = "成交金额")
    @TableField("trading_amount")
    private BigDecimal tradingAmount;

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

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态")
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