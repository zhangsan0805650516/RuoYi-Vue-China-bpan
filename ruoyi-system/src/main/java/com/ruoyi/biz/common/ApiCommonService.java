package com.ruoyi.biz.common;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略Service接口
 *
 * @author ruoyi
 * @date 2024-01-06
 */
public interface ApiCommonService
{

    String upload(MultipartFile file) throws Exception;

    String getPayment(String orderId, Date createTime, BigDecimal money) throws Exception;
}