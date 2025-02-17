package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{

    /**
     * 方向(0赠 1减)
     */
    public static final Integer ADD = 0;

    /**
     * 方向(0赠 1减)
     */
    public static final Integer subtract = 1;

    /**
     * 方向(1买涨 2买跌)
     */
    public static final Integer BUY_UP = 1;

    /**
     * 方向(1买涨 2买跌)
     */
    public static final Integer SELL_DOWN = 2;

    /**
     * 买卖(1买 2卖)
     */
    public static final Integer BUY = 1;

    /**
     * 买卖(1买 2卖)
     */
    public static final Integer SELL = 2;

    /**
     * 交易类型(1币 2现货 3合约 4理财)
     */
    public static final Integer COIN_TYPE_B = 1;

    /**
     * 交易类型(1币 2现货 3合约 4理财)
     */
    public static final Integer COIN_TYPE_SPOT = 2;

    /**
     * 交易类型(1币 2现货 3合约 4理财)
     */
    public static final Integer COIN_TYPE_CONTRACT = 3;

    /**
     * 交易类型(1币 2现货 3合约 4理财)
     */
    public static final Integer COIN_TYPE_FINANCING = 4;

    // 启涨大数据接口地址
    public static final String QI_ZHANG_URL = "http://47.120.74.91:5120";

    /**
     * 龙虎榜头
     */
    public static final String DRAGON_TIGER = "dragon_tiger:";

    /**
     * Google验证器状态头
     */
    public static final String GOOGLE_AUTH_STATUS = "google_auth_status";

    /**
     * 涨跌平柱状图头
     */
    public static final String RISE_AND_FALL_BAR = "rise_fall_bar";

    /**
     * 涨跌平头
     */
    public static final String RISE_AND_FALL = "rise_fall";

    /**
     * 板块头
     */
    public static final String BK_HOT_LST = "bkhotlst:";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 所有权限标识
     */
    public static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * 角色权限分隔符
     */
    public static final String ROLE_DELIMETER = ",";

    /**
     * 权限标识分隔符
     */
    public static final String PERMISSION_DELIMETER = ",";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.ruoyi" };

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "com.ruoyi" };

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file", "com.ruoyi.common.config" };
}
