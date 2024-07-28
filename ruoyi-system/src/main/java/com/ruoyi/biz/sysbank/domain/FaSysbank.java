package com.ruoyi.biz.sysbank.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通道
 *
 * @author ruoyi
 * @date 2024-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("fa_sysbank")
@ApiModel(value = "通道")
public class FaSysbank extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField(exist = false)
    private SysUser sysUser;

    /** id */
    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 收款名称 */
    @ApiModelProperty(value = "收款名称")
    @Excel(name = "收款名称")
    @TableField("name")
    private String name;

    /** 收款银行 */
    @ApiModelProperty(value = "收款银行")
    @Excel(name = "收款银行")
    @TableField("bank_info")
    private String bankInfo;

    /** 收款账号 */
    @ApiModelProperty(value = "收款账号")
    @Excel(name = "收款账号")
    @TableField("account")
    private String account;

    /** 状态:0=禁用,1=正常 */
    @ApiModelProperty(value = "状态:0=禁用,1=正常")
    @Excel(name = "状态:0=禁用,1=正常")
    @TableField("status")
    private Integer status;

    /** 通道名称 */
    @ApiModelProperty(value = "通道名称")
    @Excel(name = "通道名称")
    @TableField("td_name")
    private String tdName;

    /** 开户支行 */
    @ApiModelProperty(value = "开户支行")
    @Excel(name = "开户支行")
    @TableField("khzhihang")
    private String khzhihang;

    /** 代理管理员ID */
    @ApiModelProperty(value = "代理管理员ID")
    @Excel(name = "代理管理员ID")
    @TableField("admin_id")
    private Integer adminId;

    /** 查看密码 */
    @ApiModelProperty(value = "查看密码")
    @Excel(name = "查看密码")
    @TableField("ck_pass")
    private String ckPass;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @Excel(name = "删除标记")
    @TableField("delete_flag")
    private Integer deleteFlag;

}