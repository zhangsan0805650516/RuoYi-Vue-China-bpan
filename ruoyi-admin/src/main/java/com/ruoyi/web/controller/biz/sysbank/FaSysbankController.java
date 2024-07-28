package com.ruoyi.web.controller.biz.sysbank;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
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
import com.ruoyi.biz.sysbank.domain.FaSysbank;
import com.ruoyi.biz.sysbank.service.IFaSysbankService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通道Controller
 * 
 * @author ruoyi
 * @date 2024-01-07
 */
@Api(tags = "通道")
@RestController
@RequestMapping("/biz/sysbank")
public class FaSysbankController extends BaseController
{
    @Autowired
    private IFaSysbankService faSysbankService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 查询通道列表
     */
    @ApiOperation("查询通道列表")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:list')")
    @GetMapping("/list")
    public TableDataInfo list(FaSysbank faSysbank)
    {
        startPage();
        List<FaSysbank> list = faSysbankService.selectFaSysbankList(faSysbank);
        if (!list.isEmpty()) {
            for (FaSysbank sysbank : list) {
                if (null != sysbank.getAdminId()) {
                    SysUser sysUser = iSysUserService.selectUserById(Long.valueOf(sysbank.getAdminId()));
                    if (ObjectUtils.isNotEmpty(sysbank)) {
                        sysbank.setSysUser(sysUser);
                    }
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出通道列表
     */
    @ApiOperation("导出通道列表")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:export')")
    @Log(title = "通道", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FaSysbank faSysbank)
    {
        List<FaSysbank> list = faSysbankService.selectFaSysbankList(faSysbank);
        ExcelUtil<FaSysbank> util = new ExcelUtil<FaSysbank>(FaSysbank.class);
        util.exportExcel(response, list, "通道数据");
    }

    /**
     * 获取通道详细信息
     */
    @ApiOperation("获取通道详细信息")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(faSysbankService.selectFaSysbankById(id));
    }

    /**
     * 新增通道
     */
    @ApiOperation("新增通道")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:add')")
    @Log(title = "通道", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FaSysbank faSysbank)
    {
        return toAjax(faSysbankService.insertFaSysbank(faSysbank));
    }

    /**
     * 修改通道
     */
    @ApiOperation("修改通道")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:edit')")
    @Log(title = "通道", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FaSysbank faSysbank)
    {
        return toAjax(faSysbankService.updateFaSysbank(faSysbank));
    }

    /**
     * 删除通道
     */
    @ApiOperation("删除通道")
    @PreAuthorize("@ss.hasPermi('biz:sysbank:remove')")
    @Log(title = "通道", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(faSysbankService.deleteFaSysbankByIds(ids));
    }
}
