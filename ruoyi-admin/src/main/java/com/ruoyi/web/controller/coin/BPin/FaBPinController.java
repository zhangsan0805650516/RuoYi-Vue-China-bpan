package com.ruoyi.web.controller.coin.BPin;

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
import com.ruoyi.coin.BPin.domain.FaBPin;
import com.ruoyi.coin.BPin.service.IFaBPinService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 插针Controller
 * 
 * @author ruoyi
 * @date 2024-10-07
 */
@Api(tags = "插针")
@RestController
@RequestMapping("/coin/BPin")
public class FaBPinController extends BaseController
{
    @Autowired
    private IFaBPinService faBPinService;

    /**
     * 查询插针列表
     */
    @ApiOperation("查询插针列表")
    @PreAuthorize("@ss.hasPermi('coin:BPin:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBPin faBPin)
    {
        startPage();
        List<FaBPin> list = faBPinService.selectFaBPinList(faBPin);
        return getDataTable(list);
    }

    /**
     * 导出插针列表
     */
    @ApiOperation("导出插针列表")
    @PreAuthorize("@ss.hasPermi('coin:BPin:export')")
    @Log(title = "插针", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBPin faBPin)
    {
        List<FaBPin> list = faBPinService.selectFaBPinList(faBPin);
        ExcelUtil<FaBPin> util = new ExcelUtil<FaBPin>(FaBPin.class);
        util.exportExcel(response, list, "插针数据");
    }

    /**
     * 获取插针详细信息
     */
    @ApiOperation("获取插针详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BPin:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBPinService.selectFaBPinById(id));
    }

    /**
     * 新增插针
     */
    @ApiOperation("新增插针")
    @PreAuthorize("@ss.hasPermi('coin:BPin:add')")
    @Log(title = "插针", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBPin faBPin)
    {
        return toAjax(faBPinService.insertFaBPin(faBPin));
    }

    /**
     * 修改插针
     */
    @ApiOperation("修改插针")
    @PreAuthorize("@ss.hasPermi('coin:BPin:edit')")
    @Log(title = "插针", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBPin faBPin)
    {
        return toAjax(faBPinService.updateFaBPin(faBPin));
    }

    /**
     * 删除插针
     */
    @ApiOperation("删除插针")
    @PreAuthorize("@ss.hasPermi('coin:BPin:remove')")
    @Log(title = "插针", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBPinService.deleteFaBPinByIds(ids));
    }
}
