package com.ruoyi.web.controller.coin.BEntrust;

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
import com.ruoyi.coin.BEntrust.domain.FaBEntrust;
import com.ruoyi.coin.BEntrust.service.IFaBEntrustService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 委托Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "委托")
@RestController
@RequestMapping("/coin/BEntrust")
public class FaBEntrustController extends BaseController
{
    @Autowired
    private IFaBEntrustService faBEntrustService;

    /**
     * 查询委托列表
     */
    @ApiOperation("查询委托列表")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBEntrust faBEntrust)
    {
        startPage();
        List<FaBEntrust> list = faBEntrustService.selectFaBEntrustList(faBEntrust);
        return getDataTable(list);
    }

    /**
     * 导出委托列表
     */
    @ApiOperation("导出委托列表")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:export')")
    @Log(title = "委托", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBEntrust faBEntrust)
    {
        List<FaBEntrust> list = faBEntrustService.selectFaBEntrustList(faBEntrust);
        ExcelUtil<FaBEntrust> util = new ExcelUtil<FaBEntrust>(FaBEntrust.class);
        util.exportExcel(response, list, "委托数据");
    }

    /**
     * 获取委托详细信息
     */
    @ApiOperation("获取委托详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBEntrustService.selectFaBEntrustById(id));
    }

    /**
     * 新增委托
     */
    @ApiOperation("新增委托")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:add')")
    @Log(title = "委托", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBEntrust faBEntrust)
    {
        return toAjax(faBEntrustService.insertFaBEntrust(faBEntrust));
    }

    /**
     * 修改委托
     */
    @ApiOperation("修改委托")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:edit')")
    @Log(title = "委托", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBEntrust faBEntrust)
    {
        return toAjax(faBEntrustService.updateFaBEntrust(faBEntrust));
    }

    /**
     * 删除委托
     */
    @ApiOperation("删除委托")
    @PreAuthorize("@ss.hasPermi('coin:BEntrust:remove')")
    @Log(title = "委托", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBEntrustService.deleteFaBEntrustByIds(ids));
    }
}
