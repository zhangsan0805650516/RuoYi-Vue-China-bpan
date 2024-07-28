package com.ruoyi.web.controller.api.common;

import com.ruoyi.biz.common.ApiCommonService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.file.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@Api(tags = "工具接口")
@RestController
@RequestMapping("/api/common")
public class ApiCommonController extends BaseController
{

    @Autowired
    private ApiCommonService apiCommonService;

    /**
     * 通用上传请求（单个）
     */
    @ApiOperation("通用上传请求（单个）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")})
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestBody MultipartFile file) throws Exception
    {
        try {
            if (null == file) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }

            String result = apiCommonService.upload(file);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", result);
            ajax.put("fileName", FileUtils.getName(result));
            ajax.put("newFileName", FileUtils.getName(result));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }catch (Exception e){
            logger.error("upload", e);
            return AjaxResult.error();
        }
    }

}
