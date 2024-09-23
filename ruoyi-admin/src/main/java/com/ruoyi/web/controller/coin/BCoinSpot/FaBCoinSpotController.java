package com.ruoyi.web.controller.coin.BCoinSpot;

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
import com.ruoyi.coin.BCoinSpot.domain.FaBCoinSpot;
import com.ruoyi.coin.BCoinSpot.service.IFaBCoinSpotService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 现货交易Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "现货交易")
@RestController
@RequestMapping("/coin/BCoinSpot")
public class FaBCoinSpotController extends BaseController
{
    @Autowired
    private IFaBCoinSpotService faBCoinSpotService;

    /**
     * 查询现货交易列表
     */
    @ApiOperation("查询现货交易列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBCoinSpot faBCoinSpot)
    {
        startPage();
        List<FaBCoinSpot> list = faBCoinSpotService.selectFaBCoinSpotList(faBCoinSpot);
        return getDataTable(list);
    }

    /**
     * 导出现货交易列表
     */
    @ApiOperation("导出现货交易列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:export')")
    @Log(title = "现货交易", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBCoinSpot faBCoinSpot)
    {
        List<FaBCoinSpot> list = faBCoinSpotService.selectFaBCoinSpotList(faBCoinSpot);
        ExcelUtil<FaBCoinSpot> util = new ExcelUtil<FaBCoinSpot>(FaBCoinSpot.class);
        util.exportExcel(response, list, "现货交易数据");
    }

    /**
     * 获取现货交易详细信息
     */
    @ApiOperation("获取现货交易详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBCoinSpotService.selectFaBCoinSpotById(id));
    }

    /**
     * 新增现货交易
     */
    @ApiOperation("新增现货交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:add')")
    @Log(title = "现货交易", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBCoinSpot faBCoinSpot)
    {
        return toAjax(faBCoinSpotService.insertFaBCoinSpot(faBCoinSpot));
    }

    /**
     * 修改现货交易
     */
    @ApiOperation("修改现货交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:edit')")
    @Log(title = "现货交易", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBCoinSpot faBCoinSpot)
    {
        return toAjax(faBCoinSpotService.updateFaBCoinSpot(faBCoinSpot));
    }

    /**
     * 删除现货交易
     */
    @ApiOperation("删除现货交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinSpot:remove')")
    @Log(title = "现货交易", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBCoinSpotService.deleteFaBCoinSpotByIds(ids));
    }
}
