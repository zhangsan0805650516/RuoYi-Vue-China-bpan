package com.ruoyi.web.controller.coinApi.BEntrust;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BEntrust.service.IFaBEntrustService;
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
 * 委托Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "委托")
@RestController
@RequestMapping("/coinApi/BEntrust")
public class BEntrustController extends BaseController
{
    @Autowired
    private IFaBEntrustService faBEntrustService;

    /**
     * 查询委托列表
     */
    @ApiOperation("查询委托列表")
    @AppLog(title = "查询委托列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "coinType", value = "交易类型(1币 2现货 3合约 4理财)", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBEntrustList")
    public AjaxResult getBEntrustList(@RequestBody FaBEntrust faBEntrust)
    {
        try {
            if (null == faBEntrust.getPage()) {
                faBEntrust.setPage(1);
            }
            if (null == faBEntrust.getSize()) {
                faBEntrust.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faBEntrust.setUserId(loginMember.getFaMember().getId());
            IPage<FaBEntrust> faBEntrustIPage = faBEntrustService.getBEntrustList(faBEntrust);
            return AjaxResult.success(faBEntrustIPage);
        } catch (ServiceException e) {
            logger.error("getBEntrustList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBEntrustList", e);
            return AjaxResult.error();
        }
    }

}
