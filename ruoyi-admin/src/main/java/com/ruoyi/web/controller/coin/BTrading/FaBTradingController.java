package com.ruoyi.web.controller.coin.BTrading;

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
import com.ruoyi.coin.BTrading.domain.FaBTrading;
import com.ruoyi.coin.BTrading.service.IFaBTradingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成交记录Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "成交记录")
@RestController
@RequestMapping("/coin/BTrading")
public class FaBTradingController extends BaseController
{
    @Autowired
    private IFaBTradingService faBTradingService;

    /**
     * 查询成交记录列表
     */
    @ApiOperation("查询成交记录列表")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBTrading faBTrading)
    {
        startPage();
        List<FaBTrading> list = faBTradingService.selectFaBTradingList(faBTrading);
        return getDataTable(list);
    }

    /**
     * 导出成交记录列表
     */
    @ApiOperation("导出成交记录列表")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:export')")
    @Log(title = "成交记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBTrading faBTrading)
    {
        List<FaBTrading> list = faBTradingService.selectFaBTradingList(faBTrading);
        ExcelUtil<FaBTrading> util = new ExcelUtil<FaBTrading>(FaBTrading.class);
        util.exportExcel(response, list, "成交记录数据");
    }

    /**
     * 获取成交记录详细信息
     */
    @ApiOperation("获取成交记录详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBTradingService.selectFaBTradingById(id));
    }

    /**
     * 新增成交记录
     */
    @ApiOperation("新增成交记录")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:add')")
    @Log(title = "成交记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBTrading faBTrading)
    {
        return toAjax(faBTradingService.insertFaBTrading(faBTrading));
    }

    /**
     * 修改成交记录
     */
    @ApiOperation("修改成交记录")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:edit')")
    @Log(title = "成交记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBTrading faBTrading)
    {
        return toAjax(faBTradingService.updateFaBTrading(faBTrading));
    }

    /**
     * 删除成交记录
     */
    @ApiOperation("删除成交记录")
    @PreAuthorize("@ss.hasPermi('coin:BTrading:remove')")
    @Log(title = "成交记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBTradingService.deleteFaBTradingByIds(ids));
    }
}
