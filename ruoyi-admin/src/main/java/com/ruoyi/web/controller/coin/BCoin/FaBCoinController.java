package com.ruoyi.web.controller.coin.BCoin;

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
import com.ruoyi.coin.BCoin.domain.FaBCoin;
import com.ruoyi.coin.BCoin.service.IFaBCoinService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 币种Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "币种")
@RestController
@RequestMapping("/coin/BCoin")
public class FaBCoinController extends BaseController
{
    @Autowired
    private IFaBCoinService faBCoinService;

    /**
     * 查询币种列表
     */
    @ApiOperation("查询币种列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBCoin faBCoin)
    {
        startPage();
        List<FaBCoin> list = faBCoinService.selectFaBCoinList(faBCoin);
        return getDataTable(list);
    }

    /**
     * 导出币种列表
     */
    @ApiOperation("导出币种列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:export')")
    @Log(title = "币种", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBCoin faBCoin)
    {
        List<FaBCoin> list = faBCoinService.selectFaBCoinList(faBCoin);
        ExcelUtil<FaBCoin> util = new ExcelUtil<FaBCoin>(FaBCoin.class);
        util.exportExcel(response, list, "币种数据");
    }

    /**
     * 获取币种详细信息
     */
    @ApiOperation("获取币种详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBCoinService.selectFaBCoinById(id));
    }

    /**
     * 新增币种
     */
    @ApiOperation("新增币种")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:add')")
    @Log(title = "币种", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBCoin faBCoin)
    {
        return toAjax(faBCoinService.insertFaBCoin(faBCoin));
    }

    /**
     * 修改币种
     */
    @ApiOperation("修改币种")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:edit')")
    @Log(title = "币种", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBCoin faBCoin)
    {
        return toAjax(faBCoinService.updateFaBCoin(faBCoin));
    }

    /**
     * 删除币种
     */
    @ApiOperation("删除币种")
    @PreAuthorize("@ss.hasPermi('coin:BCoin:remove')")
    @Log(title = "币种", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBCoinService.deleteFaBCoinByIds(ids));
    }
}
