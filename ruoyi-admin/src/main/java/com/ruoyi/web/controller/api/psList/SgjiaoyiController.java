package com.ruoyi.web.controller.api.psList;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.biz.sgList.domain.FaSgList;
import com.ruoyi.biz.sgjiaoyi.domain.FaSgjiaoyi;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 线下配售Controller
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
@Api(tags = "配售")
@RestController
@RequestMapping("/api/sgjiaoyi")
public class SgjiaoyiController extends BaseController
{
    @Autowired
    private IFaSgjiaoyiService faSgjiaoyiService;

    /**
     * 查询用户配售列表
     */
    @ApiOperation("查询用户配售列表")
    @AppLog(title = "查询用户配售列表", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sgType", value = "1沪 2深 3创业 4北交 5科创", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "状态:0=申购中,1=中签,2=未中签,3=弃购", dataType = "Integer")
    })
    @PostMapping("/getMemberPsList")
    public AjaxResult getMemberPsList(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        try {
            if (null == faSgjiaoyi.getPage()) {
                faSgjiaoyi.setPage(1);
            }
            if (null == faSgjiaoyi.getSize()) {
                faSgjiaoyi.setSize(10);
            }
            LoginMember loginMember = getLoginMember();
            faSgjiaoyi.setUserId(loginMember.getFaMember().getId());
            IPage<FaSgjiaoyi> faSgListIPage = faSgjiaoyiService.getMemberPsList(faSgjiaoyi);
            return AjaxResult.success(faSgListIPage);
        } catch (ServiceException e) {
            logger.error("getMemberPsList", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getMemberPsList", e);
            return AjaxResult.error();
        }
    }

    /**
     * 配售认缴
     */
    @ApiOperation("配售认缴")
    @AppLog(title = "配售认缴", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "申购id", required = true, dataType = "Integer")
    })
    @PostMapping("/subscription")
    public AjaxResult subscription(@RequestBody FaSgjiaoyi faSgjiaoyi)
    {
        try {
            LoginMember loginMember = getLoginMember();
            faSgjiaoyi.setUserId(loginMember.getFaMember().getId());
            faSgjiaoyiService.subscription(faSgjiaoyi);
            return AjaxResult.success();
        } catch (ServiceException e) {
            logger.error("subscription", e);
            return AjaxResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("subscription", e);
            return AjaxResult.error();
        }
    }

}
