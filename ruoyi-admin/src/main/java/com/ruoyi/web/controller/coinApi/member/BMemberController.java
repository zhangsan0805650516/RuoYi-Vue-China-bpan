package com.ruoyi.web.controller.coinApi.member;

import com.ruoyi.coin.member.service.BMemberService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
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
 * 会员管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-03
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping("/coinApi/member")
public class BMemberController extends BaseController
{

    @Autowired
    private BMemberService bMemberService;

    /**
     * 用户账户转换
     */
    @ApiOperation("用户账户转换")
    @AppLog(title = "用户账户转换", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "转换金额", required = true, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "fromAccount", value = "转出账户(1币 2现货 3合约 4理财)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "toAccount", value = "转入账户(1币 2现货 3合约 4理财)", required = true, dataType = "Integer")
    })
    @PostMapping("/balanceChange")
    public AjaxResult balanceChange(@RequestBody FaMember faMember) throws Exception
    {
        try {
            LoginMember loginMember = getLoginMember();
            faMember.setId(loginMember.getFaMember().getId());
            bMemberService.balanceChange(faMember);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("balanceChange", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("balanceChange", e);
            return AjaxResult.error(MessageUtils.message("operation.fail"));
        }
    }

}
