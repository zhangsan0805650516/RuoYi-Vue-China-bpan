package com.ruoyi.coin.member.service;

import com.ruoyi.common.core.domain.entity.FaMember;

/**
 * B交易Service接口
 *
 * @author ruoyi
 * @date 2024-01-23
 */
public interface BMemberService
{

    /**
     * 用户账户转换
     * @param faMember
     * @throws Exception
     */
    void balanceChange(FaMember faMember) throws Exception;
}