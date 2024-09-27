package com.ruoyi.web.controller.coinApi.BCoin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;
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

import java.util.List;
import java.util.Map;

/**
 * 币种Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "币种")
@RestController
@RequestMapping("/coinApi/BCoin")
public class BCoinController extends BaseController
{

    @Autowired
    private IFaBCoinService faBCoinService;

    /**
     * 查询B种列表
     */
    @ApiOperation("查询B种列表")
    @AppLog(title = "查询B种列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "顺序(1正序 2倒序)", required = true, dataType = "Integer")
    })
    @PostMapping("/getBCoinList")
    public AjaxResult getBCoinList(@RequestBody FaBCoin faBCoin)
    {
        try {
            if (null == faBCoin.getPage()) {
                faBCoin.setPage(1);
            }
            if (null == faBCoin.getSize()) {
                faBCoin.setSize(10);
            }
            IPage<FaBCoin> faBCoinIPage = faBCoinService.getBCoinList(faBCoin);
            return AjaxResult.success(faBCoinIPage);
        } catch (ServiceException e) {
            logger.error("getBCoinList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询B种详情
     */
    @ApiOperation("查询B种详情")
    @AppLog(title = "查询B种详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "B种id", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBCoinDetail")
    public AjaxResult getBCoinDetail(@RequestBody FaBCoin faBCoin)
    {
        try {
            faBCoin = faBCoinService.getBCoinDetail(faBCoin);
            return AjaxResult.success(faBCoin);
        } catch (ServiceException e) {
            logger.error("getBCoinDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询B种K线
     */
    @ApiOperation("查询B种K线")
    @AppLog(title = "查询B种K线", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "B种id", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBCoinKline")
    public AjaxResult getBCoinKline(@RequestBody FaBCoin faBCoin)
    {
        try {
            List<Map<String, String>> list = faBCoinService.getBCoinKline(faBCoin);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getBCoinKline", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinKline", e);
            return AjaxResult.error();
        }
    }

}
