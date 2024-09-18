package com.ruoyi.web.controller.api.contractList;

import com.ruoyi.biz.contractList.domain.FaContractList;
import com.ruoyi.biz.contractList.service.IFaContractListService;
import com.ruoyi.biz.contractTemplate.service.IFaContractTemplateService;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.common.annotation.AppLog;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.List;

/**
 * 用户合同Controller
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
@Api(tags = "用户合同")
@RestController
@RequestMapping("/api/contractList")
public class ContractListController extends BaseController
{
    @Autowired
    private IFaContractListService faContractListService;

    @Autowired
    private IFaMemberService iFaMemberService;

    @Autowired
    private IFaContractTemplateService iFaContractTemplateService;

    /**
     * 查询用户合同列表
     */
    @ApiOperation("查询用户合同列表")
    @AppLog(title = "查询用户合同列表", businessType = BusinessType.OTHER)
    @PostMapping("/getContractList")
    public AjaxResult getContractList(HttpServletRequest request)
    {
        try {
            String serverName = request.getServerName();
            InetAddress inetAddress = InetAddress.getByName(serverName);
            String ip = inetAddress.getHostAddress();
            logger.error("getContractList.ip=" + ip);

            LoginMember loginMember = getLoginMember();
            List<FaContractList> list = faContractListService.getContractList(loginMember.getFaMember().getId());

            if (!list.isEmpty()) {
                for (FaContractList contract : list) {
                    contract.setFaMember(iFaMemberService.getById(contract.getUserId()));
                    contract.setFaContractTemplate(iFaContractTemplateService.getById(contract.getTemplateId()));
                    contract.setContractUrl("http://" + ip + "/contract/info?id=" + contract.getId());
                }
            }

            return AjaxResult.success(list);
        } catch (Exception e) {
            logger.error("getContractList", e);
            return AjaxResult.error();
        }
    }

}
