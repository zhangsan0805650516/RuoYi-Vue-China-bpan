package com.ruoyi.biz.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.capitalLog.domain.FaCapitalLog;
import com.ruoyi.biz.capitalLog.service.IFaCapitalLogService;
import com.ruoyi.biz.common.ApiCommonService;
import com.ruoyi.biz.member.domain.LoginPasswordParam;
import com.ruoyi.biz.member.domain.PaymentPasswordParam;
import com.ruoyi.biz.member.mapper.FaMemberMapper;
import com.ruoyi.biz.member.service.IFaMemberService;
import com.ruoyi.biz.recharge.domain.FaRecharge;
import com.ruoyi.biz.recharge.domain.RechargeNotify;
import com.ruoyi.biz.recharge.service.IFaRechargeService;
import com.ruoyi.biz.riskConfig.service.IFaRiskConfigService;
import com.ruoyi.biz.sgjiaoyi.service.IFaSgjiaoyiService;
import com.ruoyi.biz.withdraw.domain.FaWithdraw;
import com.ruoyi.biz.withdraw.service.IFaWithdrawService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FaMember;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginMember;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员管理Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-04
 */
@Service
public class FaMemberServiceImpl extends ServiceImpl<FaMemberMapper, FaMember> implements IFaMemberService
{
    @Autowired
    private FaMemberMapper faMemberMapper;

    @Autowired
    private IFaCapitalLogService iFaCapitalLogService;

    @Autowired
    private IFaRechargeService iFaRechargeService;

    @Autowired
    private IFaWithdrawService iFaWithdrawService;

    @Autowired
    private IFaRiskConfigService iFaRiskConfigService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IFaSgjiaoyiService iFaSgjiaoyiService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ApiCommonService apiCommonService;

    /**
     * 查询会员管理
     *
     * @param id 会员管理主键
     * @return 会员管理
     */
    @Override
    public FaMember selectFaMemberById(Integer id) throws Exception
    {
        FaMember faMember = faMemberMapper.selectFaMemberById(id);
        if (ObjectUtils.isNotEmpty(faMember) && null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            // 代理是客服
            if (1 == sysUser.getIsKf()) {
                faMember.setKfUrl(sysUser.getKfUrl());
            }
        }
        if (1 == faMember.getStatus() || 1 == faMember.getDeleteFlag()) {
            Thread thread = new Thread(kickoutMember(faMember.getId()));
            thread.start();
        }
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, id);
        lambdaUpdateWrapper.set(FaMember::getLoginTime, new Date());
        this.update(lambdaUpdateWrapper);
        return faMember;
    }

    /**
     * 查询会员管理列表
     *
     * @param faMember 会员管理
     * @return 会员管理
     */
    @Override
    public List<FaMember> selectFaMemberList(FaMember faMember)
    {
        try {
            faMember.setDeleteFlag(0);
            List<FaMember> list = faMemberMapper.selectFaMemberList(faMember);
            if (!list.isEmpty()) {
                for (FaMember member : list) {
                    // 新股资金 = 新股申购资金 + 新股申购资金
                    // 新股申购资金
                    BigDecimal sg = faMemberMapper.getFundInfoSg(member.getId());
                    // 新股配售资金
                    BigDecimal ps = faMemberMapper.getFundInfoPs(member.getId());
                    BigDecimal fundInfoNew = sg.add(ps);
                    member.setNewStockFreeze(fundInfoNew);

                    // 持仓市值 = 上市股票持仓 + 未上市新股转持仓
                    BigDecimal fundInfoMarketListed = faMemberMapper.getFundInfoMarketListed(member.getId());
                    BigDecimal fundInfoMarketUnlisted = faMemberMapper.getFundInfoMarketUnlisted(member.getId());
                    BigDecimal fundInfoMarket = fundInfoMarketListed.add(fundInfoMarketUnlisted);
                    member.setTakeUp(fundInfoMarket);

                    // 总盈亏 = 持仓市值 + 总提现 + 余额 + 新股申购资金 - 总充值
                    // 总充值
                    BigDecimal recharge = faMemberMapper.getFundInfoRecharge(member.getId());
                    // 总提现
                    BigDecimal withdraw = faMemberMapper.getFundInfoWithdraw(member.getId());
                    member.setTotalProfit(fundInfoMarket.add(withdraw).add(member.getBalance()).add(fundInfoNew).subtract(recharge).setScale(2, RoundingMode.HALF_UP));

                    // 总资产 = 余额 + 新股 + 持仓市值
                    BigDecimal total = member.getBalance().add(fundInfoNew).add(fundInfoMarket);
                    member.setTotal(total);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询会员实名认证列表
     * @param faMember
     * @return
     */
    @Override
    public List<FaMember> authMemberList(FaMember faMember) {
        faMember.setDeleteFlag(0);
        List<FaMember> list = faMemberMapper.authMemberList(faMember);
        return list;
    }

    /**
     * 查询会员绑卡列表
     * @param faMember
     * @return
     */
    @Override
    public List<FaMember> memberBankList(FaMember faMember) {
        faMember.setDeleteFlag(0);
        List<FaMember> list = faMemberMapper.memberBankList(faMember);
        return list;
    }

    /**
     * 新增会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertFaMember(FaMember faMember) throws Exception
    {
        faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
        faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        faMember.setCreateTime(DateUtils.getNowDate());

        // 取代理数据
        if (null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            if (ObjectUtils.isNotEmpty(sysUser)) {
                faMember.setSuperiorId(sysUser.getUserId().intValue());
                faMember.setSuperiorCode(sysUser.getInstitutionNo());
                faMember.setSuperiorName(sysUser.getNickName());
            }
        }

        // 手机号校验
        checkMobile(faMember.getMobile());
        faMember.setUsername(faMember.getMobile());
        // 校验机构码
        checkInstitutionNumber(faMember.getInstitutionNumber());
        return faMemberMapper.insertFaMember(faMember);
    }

    private void checkInstitutionNumber(String institutionNumber) throws Exception {
        if (StringUtils.isNotEmpty(institutionNumber)) {
            LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FaMember::getInstitutionNumber, institutionNumber);
            lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
            int count = (int) this.count(lambdaQueryWrapper);
            if (count > 0) {
                throw new ServiceException(MessageUtils.message("institution.number.exists"), HttpStatus.ERROR);
            }
        }
    }

    /**
     * 修改会员管理
     *
     * @param faMember 会员管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFaMember(FaMember faMember)
    {

        // 取代理数据
        if (null != faMember.getDailiId()) {
            SysUser sysUser = iSysUserService.selectUserById(faMember.getDailiId());
            if (ObjectUtils.isNotEmpty(sysUser)) {
                faMember.setSuperiorId(sysUser.getUserId().intValue());
                faMember.setSuperiorCode(sysUser.getInstitutionNo());
                faMember.setSuperiorName(sysUser.getNickName());
            }
        }

        if ("******".equals(faMember.getPassword())) {
            faMember.setPassword(null);
        } else {
            faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
        }
        if ("******".equals(faMember.getPaymentCode())) {
            faMember.setPaymentCode(null);
        } else {
            faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        }
        faMember.setUpdateTime(DateUtils.getNowDate());
        return faMemberMapper.updateFaMember(faMember);
    }

    /**
     * 批量删除会员管理
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberByIds(Integer[] ids)
    {
        return faMemberMapper.deleteFaMemberByIds(ids);
    }

    /**
     * 批量删除实名认证
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int delAuthMemberByIds(Integer[] ids)
    {
        return faMemberMapper.delAuthMemberByIds(ids);
    }

    /**
     * 批量删除绑卡
     *
     * @param ids 需要删除的会员管理主键
     * @return 结果
     */
    @Override
    public int delMemberBankByIds(Integer[] ids)
    {
        return faMemberMapper.delMemberBankByIds(ids);
    }

    /**
     * 删除会员管理信息
     *
     * @param id 会员管理主键
     * @return 结果
     */
    @Override
    public int deleteFaMemberById(Integer id)
    {
        return faMemberMapper.deleteFaMemberById(id);
    }

    /**
     * 修改会员状态
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public AjaxResult changeMemberStatus(FaMember faMember) throws Exception {
        if (StringUtils.isNotEmpty(faMember.getStatusType())) {
            LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
            switch (faMember.getStatusType()) {
                case "jiaoyi":
                    lambdaUpdateWrapper.set(FaMember::getJingzhijiaoyi, faMember.getStatus());
                    break;
                case "denglu":
                    lambdaUpdateWrapper.set(FaMember::getStatus, faMember.getStatus());

                    // 如果限制登录，先把用户踢出登录状态，异步执行
                    if (1 == faMember.getStatus()) {
                        Thread thread = new Thread(kickoutMember(faMember.getId()));
                        thread.start();
                    }
                    break;
                case "peizi":
                    lambdaUpdateWrapper.set(FaMember::getIsPz, faMember.getStatus());
                    break;
                case "dazong":
                    lambdaUpdateWrapper.set(FaMember::getIsDz, faMember.getStatus());
                    break;
                case "peishou":
                    lambdaUpdateWrapper.set(FaMember::getIsPs, faMember.getStatus());
                    break;
                case "shengou":
                    lambdaUpdateWrapper.set(FaMember::getIsSg, faMember.getStatus());
                    break;
                case "zhishu":
                    lambdaUpdateWrapper.set(FaMember::getIsZs, faMember.getStatus());
                    break;
                case "qiangchou":
                    lambdaUpdateWrapper.set(FaMember::getIsQc, faMember.getStatus());
                    break;
                case "qihuo":
                    lambdaUpdateWrapper.set(FaMember::getIsQh, faMember.getStatus());
                    break;
                default:
                    break;
            }
            this.update(lambdaUpdateWrapper);
            return AjaxResult.success(MessageUtils.message("operation.success"));
        }
        return AjaxResult.error();
    }

    /**
     * 踢出用户
     * @param id
     * @return
     * @throws Exception
     */
    private Runnable kickoutMember(Integer id) throws Exception {
        return () -> {
            // 取出所有在线用户
            Collection<String> keys = redisCache.keys(CacheConstants.MEMBER_LOGIN_TOKEN_KEY + "*");
            for (String key : keys) {
                LoginMember loginMember = redisCache.getCacheObject(key);
                // 确认用户
                if (ObjectUtils.isNotEmpty(loginMember) && id.equals(loginMember.getFaMember().getId())) {
                    redisCache.deleteObject(key);
                }
            }
        };
    }

    /**
     * 提交实名认证
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public void submitAuthMember(FaMember faMember) throws Exception {
        if (null == faMember.getId() || StringUtils.isEmpty(faMember.getName()) || StringUtils.isEmpty(faMember.getIdCard()) || null == faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getName, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getIdCard, faMember.getIdCard());
        if (StringUtils.isNotEmpty(faMember.getIdCardFrontImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardFrontImage, faMember.getIdCardFrontImage());
        }
        if (StringUtils.isNotEmpty(faMember.getIdCardBackImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardBackImage, faMember.getIdCardBackImage());
        }
        lambdaUpdateWrapper.set(FaMember::getIsAuth, faMember.getIsAuth());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        // 提交审核
        if (1 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthRejectReason, null);
            lambdaUpdateWrapper.set(FaMember::getAuthRejectTime, null);
        }
        // 审核通过
        if (2 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthTime, new Date());
        }
        // 审核失败
        if (3 == faMember.getIsAuth()) {
            lambdaUpdateWrapper.set(FaMember::getAuthRejectReason, faMember.getAuthRejectReason());
            lambdaUpdateWrapper.set(FaMember::getAuthRejectTime, new Date());
        }

        // 提交审核时间
        if (null == selectOne.getSubmitAuthTime()) {
            lambdaUpdateWrapper.set(FaMember::getSubmitAuthTime, new Date());
        }

        // 昵称
        lambdaUpdateWrapper.set(FaMember::getNickname, faMember.getName());

        this.update(lambdaUpdateWrapper);
    }

    /**
     * 提交绑定银行卡
     * @param faMember
     * @return
     */
    @Transactional
    @Override
    public void submitBindingBank(FaMember faMember) throws Exception {
        if (null == faMember.getId() || StringUtils.isEmpty(faMember.getAccountName()) || StringUtils.isEmpty(faMember.getAccount())
                || StringUtils.isEmpty(faMember.getDepositBank())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        FaMember selectOne = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(selectOne)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, faMember.getAccountName());
        lambdaUpdateWrapper.set(FaMember::getAccount, faMember.getAccount());
        lambdaUpdateWrapper.set(FaMember::getDepositBank, faMember.getDepositBank());
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, faMember.getKhzhihang());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());

        // 审核状态
        lambdaUpdateWrapper.set(FaMember::getBankCardAuth, faMember.getBankCardAuth());

        // 提交审核时间
        if (null == selectOne.getBindingTime()) {
            lambdaUpdateWrapper.set(FaMember::getBindingTime, new Date());
        }

        this.update(lambdaUpdateWrapper);
    }

    /**
     * 更新登录信息
     * @param faMember
     * @throws Exception
     */
    @Override
    public void recordLoginInfo(FaMember faMember) throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());

        // 连续登录天数
        int successions = faMember.getSuccessions();
        // 最大连续登录天数
        int maxSuccessions = faMember.getMaxSuccessions();
        // 计算是否超出48小时，否则连续登录天数加1
        long now = new Date().getTime();
        if (null == faMember.getLoginTime()) {
            faMember.setLoginTime(new Date());
        }
        long old = faMember.getLoginTime().getTime();
        long betweenDays = (now - old) / 24 / 60 / 60 / 1000;
        if (betweenDays < 2) {
            successions++;
            lambdaUpdateWrapper.set(FaMember::getSuccessions, successions);
            if (successions > maxSuccessions) {
                maxSuccessions = successions;
                lambdaUpdateWrapper.set(FaMember::getMaxSuccessions, maxSuccessions);
            }
        } else {
            successions = 1;
            lambdaUpdateWrapper.set(FaMember::getSuccessions, successions);
        }
        lambdaUpdateWrapper.set(FaMember::getPrevTime, faMember.getLoginTime());
        lambdaUpdateWrapper.set(FaMember::getLoginTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getLoginIp, IpUtils.getIpAddr());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户登录密码
     * @param loginPasswordParam
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateLoginPassword(LoginPasswordParam loginPasswordParam) throws Exception {
        if (StringUtils.isEmpty(loginPasswordParam.getOldPassword()) || StringUtils.isEmpty(loginPasswordParam.getNewPassword1()) ||
                StringUtils.isEmpty(loginPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember faMember = this.getById(loginPasswordParam.getUserId());
        // 用户不存在
        if (ObjectUtils.isEmpty(faMember)) {
            throw new UserNotExistsException();
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        if (!loginPasswordParam.getNewPassword1().equals(loginPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }
        if (!SecurityUtils.matchesPassword(loginPasswordParam.getOldPassword(), faMember.getPassword())) {
            throw new ServiceException(MessageUtils.message("user.old.password.error"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getPassword, SecurityUtils.encryptPassword(loginPasswordParam.getNewPassword1()));
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户支付密码
     * @param paymentPasswordParam
     * @throws Exception
     */
    @Transactional
    @Override
    public void updatePaymentPassword(PaymentPasswordParam paymentPasswordParam) throws Exception {
        if (StringUtils.isEmpty(paymentPasswordParam.getNewPassword1()) ||
                StringUtils.isEmpty(paymentPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember faMember = this.getById(paymentPasswordParam.getUserId());
        // 用户不存在
        if (ObjectUtils.isEmpty(faMember)) {
            throw new UserNotExistsException();
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != faMember.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 设置过支付密码,修改支付密码必须传原密码
        if (StringUtils.isNotEmpty(faMember.getPaymentCode()) && StringUtils.isEmpty(paymentPasswordParam.getOldPassword())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 判断支付密码是否正确
        if (StringUtils.isNotEmpty(faMember.getPaymentCode()) && StringUtils.isNotEmpty(paymentPasswordParam.getOldPassword())) {
            if (!SecurityUtils.matchesPassword(paymentPasswordParam.getOldPassword(), faMember.getPaymentCode())) {
                throw new ServiceException(MessageUtils.message("password.error"), HttpStatus.ERROR);
            }
        }

        // 两次新密码是否一致
        if (!paymentPasswordParam.getNewPassword1().equals(paymentPasswordParam.getNewPassword2())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getPaymentCode, SecurityUtils.encryptPassword(paymentPasswordParam.getNewPassword1()));
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改用户头像
     * @param id
     * @param avatar
     * @throws Exception
     */
    @Transactional
    @Override
    public void updateAvatar(Integer id, String avatar) throws Exception {
        if (StringUtils.isEmpty(avatar)) {
            throw new ServiceException(MessageUtils.message("no.avatar.file"), HttpStatus.ERROR);
        }
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, id);
        lambdaUpdateWrapper.set(FaMember::getAvatar, avatar);
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaMember> searchMember(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(faMember.getQueryString())) {
            lambdaQueryWrapper.and(i -> i.like(FaMember::getName, faMember.getQueryString())
                    .or().like(FaMember::getMobile, faMember.getQueryString())
                    .or().like(FaMember::getNickname, faMember.getQueryString()));
        }
        if (null != faMember.getId()) {
            lambdaQueryWrapper.eq(FaMember::getId, faMember.getId());
        }
        if (null != faMember.getIsAuth()) {
            lambdaQueryWrapper.eq(FaMember::getIsAuth, faMember.getIsAuth());
        }
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        List<FaMember> faMemberList = this.list(lambdaQueryWrapper);
        return faMemberList;
    }

    /**
     * T+1冻结资金清零
     * @throws Exception
     */
    @Override
    public void clearT1Amount() throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.gt(FaMember::getFreezeProfit, 0);
        lambdaUpdateWrapper.set(FaMember::getFreezeProfit, 0);
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 实名认证
     * @throws Exception
     */
    @Transactional
    @Override
    public void authMember(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getName()) || StringUtils.isEmpty(faMember.getIdCard())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 风控判断 是否需要上传身份证照片 默认需要
        String isauth_rz = iFaRiskConfigService.getConfigValue("isauth_rz", "1");
        // 需要上传身份证照片 判断参数
        if ("1".equals(isauth_rz)) {
            if (StringUtils.isEmpty(faMember.getIdCardFrontImage()) || StringUtils.isEmpty(faMember.getIdCardBackImage())) {
                throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
            }
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getName, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getNickname, faMember.getName());
        lambdaUpdateWrapper.set(FaMember::getIdCard, faMember.getIdCard());
        lambdaUpdateWrapper.set(FaMember::getIsAuth, 1);
        if (StringUtils.isNotEmpty(faMember.getIdCardFrontImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardFrontImage, faMember.getIdCardFrontImage());
        }
        if (StringUtils.isNotEmpty(faMember.getIdCardBackImage())) {
            lambdaUpdateWrapper.set(FaMember::getIdCardBackImage, faMember.getIdCardBackImage());
        }
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getSubmitAuthTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 绑定银行卡
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void bindingBank(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getAccountName()) || StringUtils.isEmpty(faMember.getAccount())
                || StringUtils.isEmpty(faMember.getDepositBank())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && (0 == member.getIsAuth() || 3 == member.getIsAuth())) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, faMember.getAccountName());
        lambdaUpdateWrapper.set(FaMember::getAccount, faMember.getAccount());
        lambdaUpdateWrapper.set(FaMember::getDepositBank, faMember.getDepositBank());
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, faMember.getKhzhihang());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());

        // 0未审核，初次提交，审核通过
        if (0 == member.getBankCardAuth()) {
            lambdaUpdateWrapper.set(FaMember::getBankCardAuth, 2);
        }
        // 2审核通过，修改，变审核中
        else if (2 == member.getBankCardAuth()) {
            lambdaUpdateWrapper.set(FaMember::getBankCardAuth, 1);
        }

        this.update(lambdaUpdateWrapper);
    }

    /**
     * 解绑银行卡
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void unbindingBank(FaMember faMember) throws Exception {
        LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FaMember::getId, faMember.getId());
        lambdaUpdateWrapper.set(FaMember::getAccountName, null);
        lambdaUpdateWrapper.set(FaMember::getAccount, null);
        lambdaUpdateWrapper.set(FaMember::getDepositBank, null);
        lambdaUpdateWrapper.set(FaMember::getKhzhihang, null);
        lambdaUpdateWrapper.set(FaMember::getUnbindTime, new Date());
        lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
        this.update(lambdaUpdateWrapper);
    }

    /**
     * 充值
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitRecharge(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect() || null == faMember.getRechargeType()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

//        // 实名认证判断 默认需要
//        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
//        // 需要认证 尚未实名
//        if ("1".equals(is_rz) && (0 == member.getIsAuth() || 3 == member.getIsAuth())) {
//            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
//        }

        // 充值，有充值记录，有流水
        if (0 == faMember.getRechargeType()) {
            // 充值记录
            FaRecharge faRecharge = new FaRecharge();
            faRecharge.setUserId(member.getId());
            faRecharge.setMobile(member.getMobile());
            faRecharge.setName(member.getName());
            faRecharge.setSuperiorId(member.getSuperiorId());
            faRecharge.setSuperiorCode(member.getSuperiorCode());
            faRecharge.setSuperiorName(member.getSuperiorName());
            if (0 == faMember.getDirect()) {
                faRecharge.setMoney(faMember.getAmount());
            } else if (1 == faMember.getDirect()) {
                faRecharge.setMoney(faMember.getAmount().multiply(new BigDecimal(-1)));
            }
            faRecharge.setCreateTime(new Date());
            faRecharge.setPayTime(new Date());
            faRecharge.setIsPay(1);

            faRecharge.setPayType(faMember.getPayType());
            faRecharge.setIsApprove(1);
            faRecharge.setRemarks(faMember.getRemark());

            // 订单号
            faRecharge.setOrderId("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());

            iFaRechargeService.insertFaRecharge(faRecharge);

            // 记录流水
            FaCapitalLog faCapitalLog = new FaCapitalLog();
            faCapitalLog.setUserId(member.getId());
            faCapitalLog.setMobile(member.getMobile());
            faCapitalLog.setName(member.getName());
            faCapitalLog.setSuperiorId(member.getSuperiorId());
            faCapitalLog.setSuperiorCode(member.getSuperiorCode());
            faCapitalLog.setSuperiorName(member.getSuperiorName());
            if (StringUtils.isNotEmpty(faMember.getRemark())) {
                faCapitalLog.setContent(faMember.getRemark());
            } else {
                faCapitalLog.setContent("充值");
            }
            faCapitalLog.setMoney(faMember.getAmount());
            faCapitalLog.setBeforeMoney(member.getBalance());
            if (0 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().add(faCapitalLog.getMoney()));
            } else if (1 == faMember.getDirect()) {
                faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
            }
            faCapitalLog.setType(0);
            faCapitalLog.setDirect(faMember.getDirect());
            faCapitalLog.setCreateTime(new Date());
            faCapitalLog.setDeleteFlag(0);
            iFaCapitalLogService.save(faCapitalLog);

            // 更新用户余额
            if (0 == faMember.getDirect()) {
                // 增加余额
                updateMemberBalance(member.getId(), faMember.getAmount(), 0);

                // 增加的余额去补缴
                iFaSgjiaoyiService.finishPayLater(member.getId(), faMember.getAmount());
            }
            // 减少余额
            else if (1 == faMember.getDirect()) {
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
            }
        }
        // 调整余额，无流水
        else if (1 == faMember.getRechargeType()) {
            // 更新用户余额
            if (0 == faMember.getDirect()) {
                // 增加余额
                updateMemberBalance(member.getId(), faMember.getAmount(), 0);
            }
            // 减少余额
            else if (1 == faMember.getDirect()) {
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
            }
        }
        // 调整锁定金额，无流水
        else if (2 == faMember.getRechargeType()) {
            updateFaMemberFreezeProfit(member.getId(), faMember.getAmount(), faMember.getDirect());
        }
    }

    /**
     * 修改余额
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitUpdateBalance(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 不记录流水
        // 更新用户余额
        if (0 == faMember.getDirect()) {
            // 增加余额
            updateMemberBalance(member.getId(), faMember.getAmount(), 0);
        }
        // 减少余额
        else if (1 == faMember.getDirect()) {
            // 余额不够减，清零
            if (member.getBalance().compareTo(faMember.getAmount()) < 0) {
                updateMemberBalance(member.getId(), member.getBalance(), 1);
            }
            // 余额够减，减少
            else {
                updateMemberBalance(member.getId(), faMember.getAmount(), 1);
            }
        }
    }

    /**
     * 修改T+1锁定转入转出
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void submitUpdateFreezeProfit(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || null == faMember.getDirect()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 调整锁定金额，无流水
        updateFaMemberFreezeProfit(member.getId(), faMember.getAmount(), faMember.getDirect());
    }

    /**
     * 充值申请
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public String rechargeApply(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

//        // 实名认证判断 默认需要
//        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
//        // 需要认证 尚未实名
//        if ("1".equals(is_rz) && (0 == member.getIsAuth() || 3 == member.getIsAuth())) {
//            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
//        }

        FaRecharge faRecharge = new FaRecharge();
        faRecharge.setUserId(member.getId());
        faRecharge.setMobile(member.getMobile());
        faRecharge.setName(member.getName());
        faRecharge.setSuperiorId(member.getSuperiorId());
        faRecharge.setSuperiorCode(member.getSuperiorCode());
        faRecharge.setSuperiorName(member.getSuperiorName());
        faRecharge.setMoney(faMember.getAmount());
        faRecharge.setCreateTime(new Date());
        faRecharge.setDeleteFlag(0);
        faRecharge.setIsPay(0);

        faRecharge.setPayType(faMember.getPayType());

        // 订单号
        faRecharge.setOrderId("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue());

        // 风控校验
        iFaRiskConfigService.checkRecharge(faRecharge);

        iFaRechargeService.save(faRecharge);

        // 风控挂卡通道 kaika 默认关闭0
        String kaika = iFaRiskConfigService.getConfigValue("kaika", "0");

        if ("1".equals(kaika)) {
            // 支付地址
            String result = apiCommonService.getPayment(faRecharge.getOrderId(), faRecharge.getCreateTime(), faRecharge.getMoney());
            if (StringUtils.isEmpty(result)) {
                throw new ServiceException(MessageUtils.message("payment.error"), HttpStatus.ERROR);
            }
            result = URLEncoder.encode(result, "utf-8");
            return result;
        }
        return null;
    }

    /**
     * 提现申请
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void withdrawApply(FaMember faMember) throws Exception {
        if (null == faMember.getId() || null == faMember.getAmount() || StringUtils.isEmpty(faMember.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }

        // 用户
        FaMember member = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(member)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        // 实名认证判断 默认需要
        String is_rz = iFaRiskConfigService.getConfigValue("is_rz", "1");
        // 需要认证 尚未实名
        if ("1".equals(is_rz) && 2 != member.getIsAuth()) {
            throw new ServiceException(MessageUtils.message("user.not.auth"), HttpStatus.ERROR);
        }

        // 是否绑卡审核通过
        if (2 != member.getBankCardAuth()) {
            throw new ServiceException(MessageUtils.message("user.bind.card.not.auth"), HttpStatus.ERROR);
        }

        // 支付密码校验
        if (!SecurityUtils.matchesPassword(faMember.getPaymentCode(), member.getPaymentCode())) {
            throw new ServiceException(MessageUtils.message("payment.password.error"), HttpStatus.ERROR);
        }

        FaWithdraw faWithdraw = new FaWithdraw();
        faWithdraw.setUserId(member.getId());
        faWithdraw.setMobile(member.getMobile());
        faWithdraw.setName(member.getName());
        faWithdraw.setSuperiorId(member.getSuperiorId());
        faWithdraw.setSuperiorCode(member.getSuperiorCode());
        faWithdraw.setSuperiorName(member.getSuperiorName());
        faWithdraw.setMoney(faMember.getAmount());
        faWithdraw.setCreateTime(new Date());
        faWithdraw.setDeleteFlag(0);
        faWithdraw.setIsPay(0);
        faWithdraw.setAccountName(member.getAccountName());
        faWithdraw.setAccount(member.getAccount());
        faWithdraw.setDepositBank(member.getDepositBank());

        // 余额判断
        if (member.getBalance().subtract(member.getFreezeProfit()).compareTo(faMember.getAmount()) < 0) {
            throw new ServiceException(MessageUtils.message("member.balance.not.enough"), HttpStatus.ERROR);
        }

        // 风控校验
        iFaRiskConfigService.checkWithdraw(faWithdraw);

        iFaWithdrawService.save(faWithdraw);

        // 流水
        FaCapitalLog faCapitalLog = new FaCapitalLog();
        faCapitalLog.setUserId(member.getId());
        faCapitalLog.setMobile(member.getMobile());
        faCapitalLog.setName(member.getName());
        faCapitalLog.setSuperiorId(member.getSuperiorId());
        faCapitalLog.setSuperiorCode(member.getSuperiorCode());
        faCapitalLog.setSuperiorName(member.getSuperiorName());
        faCapitalLog.setContent("提现");
        faCapitalLog.setMoney(faMember.getAmount());
        faCapitalLog.setBeforeMoney(member.getBalance());
        faCapitalLog.setLaterMoney(member.getBalance().subtract(faCapitalLog.getMoney()));
        faCapitalLog.setType(1);
        faCapitalLog.setDirect(1);
        faCapitalLog.setCreateTime(new Date());
        faCapitalLog.setDeleteFlag(0);
        iFaCapitalLogService.save(faCapitalLog);

        // 更新用户余额
        updateMemberBalance(member.getId(), faMember.getAmount(), 1);
    }

    /**
     * 充值记录
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaRecharge> rechargeList(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getUserId, faMember.getId());
        lambdaQueryWrapper.eq(FaRecharge::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaRecharge::getCreateTime);
        List<FaRecharge> list = iFaRechargeService.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 提现记录
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public List<FaWithdraw> withdrawList(FaMember faMember) throws Exception {
        LambdaQueryWrapper<FaWithdraw> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaWithdraw::getUserId, faMember.getId());
        lambdaQueryWrapper.eq(FaWithdraw::getDeleteFlag, 0);
        lambdaQueryWrapper.orderByDesc(FaWithdraw::getCreateTime);
        List<FaWithdraw> list = iFaWithdrawService.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * 注册接口
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public FaMember register(FaMember faMember) throws Exception {
        if (StringUtils.isEmpty(faMember.getMobile()) || StringUtils.isEmpty(faMember.getPassword())
                || StringUtils.isEmpty(faMember.getConfirmPassword())
                || StringUtils.isEmpty(faMember.getSuperiorCode())) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        // 两次密码校验
        if (!faMember.getPassword().equals(faMember.getConfirmPassword())) {
            throw new ServiceException(MessageUtils.message("user.two.password.not.same"), HttpStatus.ERROR);
        }

        // 手机号校验
        checkMobile(faMember.getMobile());

        // 上级机构码校验
        SysUser superior = checkSuperiorCode(faMember.getSuperiorCode());

        // 保存用户信息
        faMember.setUsername(faMember.getMobile());
        faMember.setPassword(SecurityUtils.encryptPassword(faMember.getPassword()));
//        faMember.setLevel(superior.getLevel() + 1);
        if (StringUtils.isNotEmpty(faMember.getPaymentCode())) {
            faMember.setPaymentCode(SecurityUtils.encryptPassword(faMember.getPaymentCode()));
        }
        faMember.setJoinIp(IpUtils.getIpAddr());
        faMember.setCreateTime(new Date());
        faMember.setSuperiorId(superior.getUserId().intValue());
        faMember.setSuperiorCode(superior.getInstitutionNo());
        faMember.setSuperiorName(superior.getNickName());
//        faMember.setIsSimulation(superior.getIsSimulation());
        // 生成6位机构码
//        faMember.setInstitutionNumber(getInstitutionNumber());
        // 生成 T003574928 唯一码
        faMember.setWeiyima(getWeiyima());
        faMember.setDailiId(superior.getUserId());
        faMember.setIsAuth(0);
        // 默认头像
        String avatar = iFaRiskConfigService.getConfigValue("defaultavatar", "");
        faMember.setAvatar(avatar);
        faMemberMapper.insertFaMember(faMember);
        faMember = this.getById(faMember.getId());
        return faMember;
    }

    /**
     * 生成唯一码
     * @return
     * @throws Exception
     */
    private String getWeiyima() throws Exception {
        int num = (int) Math.floor(OrderUtil.randomNumber(1000000, 9999999));
        String weiyima = "T00" + String.valueOf(num);
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getWeiyima, weiyima);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faMember)) {
            return weiyima;
        } else {
            return getWeiyima();
        }
    }

    /**
     * 生成6位机构码
     * @return
     * @throws Exception
     */
    private String getInstitutionNumber() throws Exception {
        String institutionNumber = IdUtils.fastUUID().substring(0, 6).toUpperCase();
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getInstitutionNumber, institutionNumber);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(faMember)) {
            return institutionNumber;
        } else {
            return getInstitutionNumber();
        }
    }

    /**
     * 手机号校验
     * @param mobile
     */
    private void checkMobile(String mobile) throws Exception {
        LambdaQueryWrapper<FaMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaMember::getMobile, mobile);
        lambdaQueryWrapper.eq(FaMember::getDeleteFlag, 0);
        FaMember faMember = this.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.mobile.exists"), HttpStatus.ERROR);
        }
    }

    /**
     * 上级机构码校验
     * @param superiorCode
     * @throws Exception
     */
    private SysUser checkSuperiorCode(String superiorCode) throws Exception {
        SysUser sysUser = iSysUserService.selectUserByInstitutionNo(superiorCode);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new ServiceException(MessageUtils.message("superiorCode.error"), HttpStatus.ERROR);
        }
        return sysUser;
    }

    /**
     * 用户统计数据
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getMemberStatistics(FaMember faMember) throws Exception {
        return faMemberMapper.getMemberStatistics(faMember);
    }

    /**
     * 批量审核身份认证
     * @param faMember
     * @throws Exception
     */
    @Transactional
    @Override
    public void batchAuthMember(FaMember faMember) throws Exception {
        if (ObjectUtils.isNotEmpty(faMember) && faMember.getIds().length > 0) {
            LambdaUpdateWrapper<FaMember> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.in(FaMember::getId, faMember.getIds());
            lambdaUpdateWrapper.set(FaMember::getIsAuth, 2);
            lambdaUpdateWrapper.set(FaMember::getAuthTime, new Date());
            lambdaUpdateWrapper.set(FaMember::getUpdateTime, new Date());
            this.update(lambdaUpdateWrapper);
        }
    }

    /**
     * 用户资金信息
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> fundInfo(FaMember faMember) throws Exception {
        faMember = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        Map<String, BigDecimal> map = new HashMap<>();

        // 可用资金
        map.put("available", faMember.getBalance().setScale(2, RoundingMode.HALF_UP));

        // 新股资金 = 新股申购资金 + 新股申购资金
        // 新股申购资金
        BigDecimal sg = faMemberMapper.getFundInfoSg(faMember.getId());
        // 新股配售资金
        BigDecimal ps = faMemberMapper.getFundInfoPs(faMember.getId());
        BigDecimal fundInfoNew = sg.add(ps);
        map.put("new", fundInfoNew.setScale(2, RoundingMode.HALF_UP));

        // 可取资金
        BigDecimal cash = faMember.getBalance().subtract(faMember.getFreezeProfit()).setScale(2, RoundingMode.HALF_UP);
        if (cash.compareTo(BigDecimal.ZERO) < 0) {
            cash = BigDecimal.ZERO;
        }
        map.put("cash", cash);

        // 冻结资金
        map.put("freeze", faMember.getFreezeProfit());

        // 持仓市值 = 上市股票持仓 + 未上市新股转持仓
        BigDecimal fundInfoMarketListed = faMemberMapper.getFundInfoMarketListed(faMember.getId());
        BigDecimal fundInfoMarketUnlisted = faMemberMapper.getFundInfoMarketUnlisted(faMember.getId());
        BigDecimal fundInfoMarket = fundInfoMarketListed.add(fundInfoMarketUnlisted);
        map.put("market", fundInfoMarket.setScale(2, RoundingMode.HALF_UP));

        // 总盈亏 = 持仓市值 + 总提现 + 余额 + 新股申购资金 - 总充值
        // 总充值
        BigDecimal recharge = faMemberMapper.getFundInfoRecharge(faMember.getId());
        // 总提现
        BigDecimal withdraw = faMemberMapper.getFundInfoWithdraw(faMember.getId());
        map.put("profit", fundInfoMarket.add(withdraw).add(faMember.getBalance()).add(fundInfoNew).subtract(recharge).setScale(2, RoundingMode.HALF_UP));

        // T+1 冻结资金
        map.put("T1", faMember.getFreezeProfit().setScale(2, RoundingMode.HALF_UP));

        // 总资产 = 余额 + 新股 + 持仓市值
        BigDecimal total = faMember.getBalance().add(fundInfoNew).add(fundInfoMarket);
        map.put("total", total.setScale(2, RoundingMode.HALF_UP));

        // 持仓盈亏
        BigDecimal holdProfit = faMemberMapper.getTradeInfoHoldProfit(faMember.getId());
        map.put("holdProfit", holdProfit.setScale(2, RoundingMode.HALF_UP));

        // 交易盈亏
        BigDecimal tradeProfit = faMemberMapper.getTradeInfoTradeProfit(faMember.getId());
        map.put("tradeProfit", tradeProfit.setScale(2, RoundingMode.HALF_UP));

        // 大宗持仓市值
        BigDecimal dzMarket = faMemberMapper.getDzFundInfoMarket(faMember.getId());
        map.put("dzMarket", dzMarket.setScale(2, RoundingMode.HALF_UP));

        // 大宗持仓盈亏
        BigDecimal dzHoldProfit = faMemberMapper.getDzTradeInfoHoldProfit(faMember.getId());
        map.put("dzHoldProfit", dzHoldProfit.setScale(2, RoundingMode.HALF_UP));

        // 总手续费
        BigDecimal totalPoundage = faMemberMapper.getTotalPoundage(faMember.getId());
        map.put("totalPoundage", totalPoundage.setScale(2, RoundingMode.HALF_UP));

        return map;
    }

    /**
     * 用户交易信息
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> tradeInfo(FaMember faMember) throws Exception {
        faMember = this.getById(faMember.getId());
        if (ObjectUtils.isEmpty(faMember)) {
            throw new ServiceException(MessageUtils.message("user.not.exists"), HttpStatus.ERROR);
        }

        Map<String, BigDecimal> map = new HashMap<>();

        // 可用资金
        map.put("available", faMember.getBalance());

        // 持仓盈亏
        BigDecimal holdProfit = faMemberMapper.getTradeInfoHoldProfit(faMember.getId());
        map.put("holdProfit", holdProfit);

        // 可取资金
        map.put("cash", faMember.getBalance().subtract(faMember.getFreezeProfit()));

        // 持仓市值
        BigDecimal fundInfoMarket = faMemberMapper.getFundInfoMarket(faMember.getId());
        map.put("market", fundInfoMarket);

        // 交易盈亏
        BigDecimal tradeProfit = faMemberMapper.getTradeInfoTradeProfit(faMember.getId());
        map.put("tradeProfit", tradeProfit);

        return map;
    }

    /**
     * 更新冻结资金
     * @param memberId
     * @param amount
     * @param direct
     * @throws Exception
     */
    @Override
    public void updateFaMemberFreezeProfit(Integer memberId, BigDecimal amount, int direct) throws Exception {
        faMemberMapper.updateFaMemberFreezeProfit(memberId, amount, direct);
    }

    /**
     * 更新用户余额
     * @param userId 用户id
     * @param money 变动金额
     * @param direct 方向(0赠 1减)
     * @throws Exception
     */
    @Override
    public void updateMemberBalance(Integer userId, BigDecimal money, int direct) throws Exception {
        faMemberMapper.updateMemberBalance(userId, money, direct);
    }

    /**
     * 总余额
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalBalance(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalBalance(faMember);
    }

    /**
     * 总充值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalRecharge(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalRecharge(faMember);
    }

    /**
     * 总提现
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalWithdraw(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalWithdraw(faMember);    }

    /**
     * 新股申购冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalSg(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalSg(faMember);
    }

    /**
     * 新股配售冻结
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalPs(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalPs(faMember);
    }

    /**
     * 上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalListed(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalListed(faMember);
    }

    /**
     * 未上市持仓市值
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getTotalUnlisted(FaMember faMember) throws Exception {
        return faMemberMapper.getTotalUnlisted(faMember);
    }

    /**
     * 单个用户统计
     * @param faMember
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, BigDecimal> getMemberStatisticsSingle(FaMember faMember) throws Exception {
        if (null == faMember.getId()) {
            throw new ServiceException(MessageUtils.message("params.error"), HttpStatus.ERROR);
        }
        faMember = this.getById(faMember.getId());
        // 余额
        BigDecimal balance = faMember.getBalance();
        // T+N锁定
        BigDecimal freezeProfit = faMember.getFreezeProfit();
        // 可取资金
        BigDecimal cash = balance.subtract(freezeProfit);
        if (cash.compareTo(BigDecimal.ZERO) < 0) {
            cash = BigDecimal.ZERO;
        }
        // 上市持仓市值
        BigDecimal listedHold = faMemberMapper.getListedHold(faMember);
        // 未上市持仓市值
        BigDecimal unlistedHold = faMemberMapper.getUnlistedHold(faMember);
        // 新股申购冻结
        BigDecimal sgFreeze = faMemberMapper.getSgFreeze(faMember);
        // 新股配售冻结
        BigDecimal psFreeze = faMemberMapper.getPsFreeze(faMember);
        // 总充值
        BigDecimal recharge = faMemberMapper.getRecharge(faMember);
        // 总提现
        BigDecimal withdraw = faMemberMapper.getWithdraw(faMember);
        // 总盈亏 = 余额 + 新股 + 持仓 + 提现 - 充值
        BigDecimal profitLose = balance.add(sgFreeze).add(psFreeze).add(listedHold).add(unlistedHold).add(withdraw).subtract(recharge);
        // 总资产 = 余额 + 新股 + 持仓市值
        BigDecimal fund = balance.add(sgFreeze).add(psFreeze).add(listedHold).add(unlistedHold);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("balance", balance);
        map.put("freezeProfit", freezeProfit);
        map.put("cash", cash);
        map.put("listedHold", listedHold);
        map.put("unlistedHold", unlistedHold);
        map.put("sgFreeze", sgFreeze);
        map.put("psFreeze", psFreeze);
        map.put("recharge", recharge);
        map.put("withdraw", withdraw);
        map.put("profitLose", profitLose);
        map.put("fund", fund);
        return map;
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void nineBrotherRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if ("2".equals(rechargeNotify.getState())) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getOrderId, rechargeNotify.getPayOrderId());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void rendeRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getData().getOrder_number());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 接口成功
            if (0 == rechargeNotify.getCode()) {
                // 支付完成
                if (2 == rechargeNotify.getData().getStatus()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getData().getOrderNo());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
                // 支付失败
                else {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getData().getOrderNo());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    iFaRechargeService.update(lambdaUpdateWrapper);
                }
            }
        }
    }

    /**
     * 充值回调接口
     * @param rechargeNotify
     * @throws Exception
     */
    @Override
    public void huojianRechargeNotify(RechargeNotify rechargeNotify) throws Exception {
        LambdaQueryWrapper<FaRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 商户订单号
        lambdaQueryWrapper.eq(FaRecharge::getOrderId, rechargeNotify.getOrder_no());
        FaRecharge faRecharge = iFaRechargeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isNotEmpty(faRecharge)) {
            // 支付成功
            if (2 == rechargeNotify.getStatus()) {
                // 未付款
                if (0 == faRecharge.getIsPay()) {
                    LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                    lambdaUpdateWrapper.set(FaRecharge::getIsPay, 1);
                    // 平台订单id
                    lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getUuid());
                    lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getPayTime, new Date());
                    lambdaUpdateWrapper.set(FaRecharge::getIsApprove, 1);
                    iFaRechargeService.update(lambdaUpdateWrapper);

                    // 资金流水
                    iFaCapitalLogService.save(faRecharge);
                }
            }
            // 支付失败
            else {
                LambdaUpdateWrapper<FaRecharge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FaRecharge::getId, faRecharge.getId());
                lambdaUpdateWrapper.set(FaRecharge::getIsPay, 2);
                // 平台订单id
                lambdaUpdateWrapper.set(FaRecharge::getTransactionId, rechargeNotify.getUuid());
                lambdaUpdateWrapper.set(FaRecharge::getUpdateTime, new Date());
                iFaRechargeService.update(lambdaUpdateWrapper);
            }
        }
    }

}