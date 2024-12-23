package com.ruoyi.coin.BPin.domain;

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
 * 插针
 *
 * @author ruoyi
 * @date 2024-10-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_b_pin")
@ApiModel(value = "插针")
public class FaBPin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

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

    /** 插针价格 */
    @ApiModelProperty(value = "插针价格")
    @Excel(name = "插针价格")
    @TableField("pin_price")
    private BigDecimal pinPrice;

    /** 插针时间 */
    @ApiModelProperty(value = "插针时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "插针时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("pin_time")
    private Date pinTime;

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