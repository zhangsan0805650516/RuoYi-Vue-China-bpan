package com.ruoyi.web.controller.api.capitalLog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资金记录Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "资金记录")
@RestController
@RequestMapping("/api/capitalLog")
public class CapitalLogController extends BaseController
{
    @Autowired
    private IFaCapitalLogService faCapitalLogService;

    /**
     * 查询资金记录
     */
    @ApiOperation("查询资金记录")
    @AppLog(title = "查询资金记录", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "类型(0充值 1提现 2普通下单 3手续费 4印花税)(可选)", dataType = "Integer")})
    @PostMapping("/getFundRecord")
    public AjaxResult getFundRecord(@RequestBody FaCapitalLog faCapitalLog)
    {
        try {
            if (null == faCapitalLog.getPage()) {
                faCapitalLog.setPage(1);
            }
            if (null == faCapitalLog.getSize()) {
                faCapitalLog.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faCapitalLog.setUserId(loginMember.getFaMember().getId());
            IPage<FaCapitalLog> faCapitalLogIPage = faCapitalLogService.getFundRecord(faCapitalLog);
            return AjaxResult.success(faCapitalLogIPage);
        } catch (Exception e) {
            logger.error("getFundRecord", e);
            return AjaxResult.error();
        }
    }

}
