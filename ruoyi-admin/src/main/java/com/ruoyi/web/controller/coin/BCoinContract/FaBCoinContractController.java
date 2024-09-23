package com.ruoyi.web.controller.coin.BCoinContract;

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
import com.ruoyi.coin.BCoinContract.domain.FaBCoinContract;
import com.ruoyi.coin.BCoinContract.service.IFaBCoinContractService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合约交易Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Api(tags = "合约交易")
@RestController
@RequestMapping("/coin/BCoinContract")
public class FaBCoinContractController extends BaseController
{
    @Autowired
    private IFaBCoinContractService faBCoinContractService;

    /**
     * 查询合约交易列表
     */
    @ApiOperation("查询合约交易列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaBCoinContract faBCoinContract)
    {
        startPage();
        List<FaBCoinContract> list = faBCoinContractService.selectFaBCoinContractList(faBCoinContract);
        return getDataTable(list);
    }

    /**
     * 导出合约交易列表
     */
    @ApiOperation("导出合约交易列表")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:export')")
    @Log(title = "合约交易", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaBCoinContract faBCoinContract)
    {
        List<FaBCoinContract> list = faBCoinContractService.selectFaBCoinContractList(faBCoinContract);
        ExcelUtil<FaBCoinContract> util = new ExcelUtil<FaBCoinContract>(FaBCoinContract.class);
        util.exportExcel(response, list, "合约交易数据");
    }

    /**
     * 获取合约交易详细信息
     */
    @ApiOperation("获取合约交易详细信息")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(faBCoinContractService.selectFaBCoinContractById(id));
    }

    /**
     * 新增合约交易
     */
    @ApiOperation("新增合约交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:add')")
    @Log(title = "合约交易", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaBCoinContract faBCoinContract)
    {
        return toAjax(faBCoinContractService.insertFaBCoinContract(faBCoinContract));
    }

    /**
     * 修改合约交易
     */
    @ApiOperation("修改合约交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:edit')")
    @Log(title = "合约交易", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaBCoinContract faBCoinContract)
    {
        return toAjax(faBCoinContractService.updateFaBCoinContract(faBCoinContract));
    }

    /**
     * 删除合约交易
     */
    @ApiOperation("删除合约交易")
    @PreAuthorize("@ss.hasPermi('coin:BCoinContract:remove')")
    @Log(title = "合约交易", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(faBCoinContractService.deleteFaBCoinContractByIds(ids));
    }
}
