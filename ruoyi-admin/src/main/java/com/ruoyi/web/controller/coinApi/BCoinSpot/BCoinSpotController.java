package com.ruoyi.web.controller.coinApi.BCoinSpot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
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
 * 现货交易Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "现货交易")
@RestController
@RequestMapping("/coinApi/BCoinSpot")
public class BCoinSpotController extends BaseController
{

    @Autowired
    private IFaBCoinSpotService faBCoinSpotService;

    /**
     * 查询现货列表
     */
    @ApiOperation("查询现货列表")
    @AppLog(title = "查询现货列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "顺序(1正序 2倒序)", required = true, dataType = "Integer")
    })
    @PostMapping("/getBCoinSpotList")
    public AjaxResult getBCoinSpotList(@RequestBody FaBCoinSpot faBCoinSpot)
    {
        try {
            if (null == faBCoinSpot.getPage()) {
                faBCoinSpot.setPage(1);
            }
            if (null == faBCoinSpot.getSize()) {
                faBCoinSpot.setSize(10);
            }
            IPage<FaBCoinSpot> faBCoinSpotIPage = faBCoinSpotService.getBCoinSpotList(faBCoinSpot);
            return AjaxResult.success(faBCoinSpotIPage);
        } catch (ServiceException e) {
            logger.error("getBCoinSpotList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinSpotList", e);
            return AjaxResult.error();
        }
    }

}
