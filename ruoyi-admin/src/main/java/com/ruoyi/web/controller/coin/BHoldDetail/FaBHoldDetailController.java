package com.ruoyi.web.controller.coin.BHoldDetail;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.ruoyi.coin.BHoldDetail.domain.FaBHoldDetail;
import com.ruoyi.coin.BHoldDetail.service.IFaBHoldDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 持仓明细Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "持仓明细")
@RestController
@RequestMapping("/coin/BHoldDetail")
public class FaBHoldDetailController extends BaseController
{
    @Autowired
    private IFaBHoldDetailService faBHoldDetailService;

    /**
     * 查询持仓明细列表
     */
    @ApiOperation("查询持仓明细列表")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBHoldDetail faBHoldDetail)
    {
        startPage();
        List<FaBHoldDetail> list = faBHoldDetailService.selectFaBHoldDetailList(faBHoldDetail);
        return getDataTable(list);
    }

    /**
     * 导出持仓明细列表
     */
    @ApiOperation("导出持仓明细列表")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:export')")
    @Log(title = "持仓明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBHoldDetail faBHoldDetail)
    {
        List<FaBHoldDetail> list = faBHoldDetailService.selectFaBHoldDetailList(faBHoldDetail);
        ExcelUtil<FaBHoldDetail> util = new ExcelUtil<FaBHoldDetail>(FaBHoldDetail.class);
        util.exportExcel(response, list, "持仓明细数据");
    }

    /**
     * 获取持仓明细详细信息
     */
    @ApiOperation("获取持仓明细详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBHoldDetailService.selectFaBHoldDetailById(id));
    }

    /**
     * 新增持仓明细
     */
    @ApiOperation("新增持仓明细")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:add')")
    @Log(title = "持仓明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        return toAjax(faBHoldDetailService.insertFaBHoldDetail(faBHoldDetail));
    }

    /**
     * 修改持仓明细
     */
    @ApiOperation("修改持仓明细")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:edit')")
    @Log(title = "持仓明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBHoldDetail faBHoldDetail)
    {
        return toAjax(faBHoldDetailService.updateFaBHoldDetail(faBHoldDetail));
    }

    /**
     * 删除持仓明细
     */
    @ApiOperation("删除持仓明细")
    @PreAuthorize("@ss.hasPermi('coin:BHoldDetail:remove')")
    @Log(title = "持仓明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBHoldDetailService.deleteFaBHoldDetailByIds(ids));
    }
}
