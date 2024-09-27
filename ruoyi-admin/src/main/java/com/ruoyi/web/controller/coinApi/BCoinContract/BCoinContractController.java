package com.ruoyi.web.controller.coinApi.BCoinContract;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BCoinContract.service.IFaBCoinContractService;
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
 * 合约交易Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "合约交易")
@RestController
@RequestMapping("/coinApi/BCoinContract")
public class BCoinContractController extends BaseController
{

    @Autowired
    private IFaBCoinContractService faBCoinContractService;

    /**
     * 查询合约列表
     */
    @ApiOperation("查询合约列表")
    @AppLog(title = "查询合约列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段(1现价 2涨跌 3涨跌幅 4成交额 5换手率 6昨收价 7今开价 8最高价)", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sort", value = "顺序(1正序 2倒序)", required = true, dataType = "Integer")
    })
    @PostMapping("/getBCoinContractList")
    public AjaxResult getBCoinContractList(@RequestBody FaBCoinContract faBCoinContract)
    {
        try {
            if (null == faBCoinContract.getPage()) {
                faBCoinContract.setPage(1);
            }
            if (null == faBCoinContract.getSize()) {
                faBCoinContract.setSize(10);
            }
            IPage<FaBCoinContract> faBCoinContractIPage = faBCoinContractService.getBCoinContractList(faBCoinContract);
            return AjaxResult.success(faBCoinContractIPage);
        } catch (ServiceException e) {
            logger.error("getBCoinContractList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinContractList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询合约详情
     */
    @ApiOperation("查询合约详情")
    @AppLog(title = "查询合约详情", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "合约id", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBCoinContractDetail")
    public AjaxResult getBCoinContractDetail(@RequestBody FaBCoinContract faBCoinContract)
    {
        try {
            faBCoinContract = faBCoinContractService.getBCoinContractDetail(faBCoinContract);
            return AjaxResult.success(faBCoinContract);
        } catch (ServiceException e) {
            logger.error("getBCoinContractDetail", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinContractDetail", e);
            return AjaxResult.error();
        }
    }

    /**
     * 查询合约K线
     */
    @ApiOperation("查询合约K线")
    @AppLog(title = "查询合约K线", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "合约id", required = true, dataType = "Integer"),
    })
    @PostMapping("/getBCoinContractKline")
    public AjaxResult getBCoinContractKline(@RequestBody FaBCoinContract faBCoinContract)
    {
        try {
            List<Map<String, String>> list = faBCoinContractService.getBCoinContractKline(faBCoinContract);
            return AjaxResult.success(list);
        } catch (ServiceException e) {
            logger.error("getBCoinContractKline", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getBCoinContractKline", e);
            return AjaxResult.error();
        }
    }

}
