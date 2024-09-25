package com.ruoyi.web.controller.coinApi.BExchange;

import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BExchange.service.BExchangeService;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.Constants;
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
 * 交易Controller
 * 
 * @author ruoyi
 * @date 2024-01-23
 */
@Api(tags = "B交易")
@RestController
@RequestMapping("/coinApi/exchange")
public class BExchangeController extends BaseController
{

    @Autowired
    private BExchangeService bExchangeService;

    /**
     * 买入B种
     */
    @RepeatSubmit
    @ApiOperation("买入B种")
    @AppLog(title = "买入B种", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "交易品id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "entrustNumber", value = "委托数量", required = true, dataType = "decimal"),
    })
    @PostMapping("/buyBCoin")
    public AjaxResult buyBCoin(@RequestBody FaBEntrust faBEntrust)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBEntrust.setUserId(loginMember.getFaMember().getId());
            faBEntrust.setCoinType(Constants.COIN_TYPE_B);
            faBEntrust.setTradingType(Constants.BUY);
            faBEntrust.setTradeDirect(Constants.BUY_UP);
            bExchangeService.buyBCoin(faBEntrust);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyBCoin", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyBCoin", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出B种
     */
    @RepeatSubmit
    @ApiOperation("卖出B种")
    @AppLog(title = "卖出B种", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer"),
    })
    @PostMapping("/sellBCoin")
    public AjaxResult sellBCoin(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBHoldDetail.setUserId(loginMember.getFaMember().getId());
            bExchangeService.sellBCoin(faBHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellBCoin", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellBCoin", e);
            return AjaxResult.error();
        }
    }

    /**
     * 买入B现货
     */
    @RepeatSubmit
    @ApiOperation("买入B现货")
    @AppLog(title = "买入B现货", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "交易品id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "entrustNumber", value = "委托数量", required = true, dataType = "decimal"),
    })
    @PostMapping("/buyBCoinSpot")
    public AjaxResult buyBCoinSpot(@RequestBody FaBEntrust faBEntrust)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBEntrust.setUserId(loginMember.getFaMember().getId());
            faBEntrust.setCoinType(Constants.COIN_TYPE_SPOT);
            faBEntrust.setTradingType(Constants.BUY);
            faBEntrust.setTradeDirect(Constants.BUY_UP);
            bExchangeService.buyBCoinSpot(faBEntrust);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyBCoinSpot", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyBCoinSpot", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出B现货
     */
    @RepeatSubmit
    @ApiOperation("卖出B现货")
    @AppLog(title = "卖出B现货", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer"),
    })
    @PostMapping("/sellBCoinSpot")
    public AjaxResult sellBCoinSpot(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBHoldDetail.setUserId(loginMember.getFaMember().getId());
            bExchangeService.sellBCoinSpot(faBHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellBCoinSpot", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellBCoinSpot", e);
            return AjaxResult.error();
        }
    }

    /**
     * 买入B合约
     */
    @RepeatSubmit
    @ApiOperation("买入B合约")
    @AppLog(title = "买入B合约", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "交易品id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "entrustNumber", value = "委托数量", required = true, dataType = "decimal"),
    })
    @PostMapping("/buyBCoinContract")
    public AjaxResult buyBCoinContract(@RequestBody FaBEntrust faBEntrust)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBEntrust.setUserId(loginMember.getFaMember().getId());
            faBEntrust.setCoinType(Constants.COIN_TYPE_CONTRACT);
            faBEntrust.setTradingType(Constants.BUY);
            faBEntrust.setTradeDirect(Constants.BUY_UP);
            bExchangeService.buyBCoinContract(faBEntrust);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("buyBCoinContract", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("buyBCoinContract", e);
            return AjaxResult.error();
        }
    }

    /**
     * 卖出B合约
     */
    @RepeatSubmit
    @ApiOperation("卖出B合约")
    @AppLog(title = "卖出B合约", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "持仓id", required = true, dataType = "Integer"),
    })
    @PostMapping("/sellBCoinContract")
    public AjaxResult sellBCoinContract(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faBHoldDetail.setUserId(loginMember.getFaMember().getId());
            bExchangeService.sellBCoinContract(faBHoldDetail);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("sellBCoinContract", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("sellBCoinContract", e);
            return AjaxResult.error();
        }
    }

}
