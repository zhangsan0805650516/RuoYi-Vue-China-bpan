package com.ruoyi.web.controller.coinApi.BTrading;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
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
 * 成交记录Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "成交记录")
@RestController
@RequestMapping("/coinApi/BTrading")
public class BTradingController extends BaseController
{

    @Autowired
    private IFaBTradingService faBTradingService;

    /**
     * 查询成交列表
     */
    @ApiOperation("查询成交列表")
    @AppLog(title = "查询成交列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "coinType", value = "交易类型(1币 2现货 3合约)", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBTradingList")
    public AjaxResult getBTradingList(@RequestBody FaBTrading faBTrading)
    {
        try {
            if (null == faBTrading.getPage()) {
                faBTrading.setPage(1);
            }
            if (null == faBTrading.getSize()) {
                faBTrading.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faBTrading.setUserId(loginMember.getFaMember().getId());
            IPage<FaBTrading> faBHoldDetailIPage = faBTradingService.getBTradingList(faBTrading);
            return AjaxResult.success(faBHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getBTradingList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBTradingList", e);
            return AjaxResult.error();
        }
    }

}
