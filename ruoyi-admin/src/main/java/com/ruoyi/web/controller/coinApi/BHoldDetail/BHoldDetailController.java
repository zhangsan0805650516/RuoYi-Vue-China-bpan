package com.ruoyi.web.controller.coinApi.BHoldDetail;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.coin.BHoldDetail.service.IFaBHoldDetailService;
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
 * 持仓明细Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "持仓明细")
@RestController
@RequestMapping("/coinApi/BHoldDetail")
public class BHoldDetailController extends BaseController
{

    @Autowired
    private IFaBHoldDetailService faBHoldDetailService;

    /**
     * 查询持仓列表
     */
    @ApiOperation("查询持仓列表")
    @AppLog(title = "查询持仓列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "coinType", value = "交易类型(1币 2现货 3合约 4理财)", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBHoldDetailList")
    public AjaxResult getBHoldDetailList(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        try {
            if (null == faBHoldDetail.getPage()) {
                faBHoldDetail.setPage(1);
            }
            if (null == faBHoldDetail.getSize()) {
                faBHoldDetail.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faBHoldDetail.setUserId(loginMember.getFaMember().getId());
            IPage<FaBHoldDetail> faBHoldDetailIPage = faBHoldDetailService.getBHoldDetailList(faBHoldDetail);
            return AjaxResult.success(faBHoldDetailIPage);
        } catch (ServiceException e) {
            logger.error("getBHoldDetailList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBHoldDetailList", e);
            return AjaxResult.error();
        }
    }

}
