package com.ruoyi.common.utils.payment;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.OrderUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.*;


/**
 * 支付工具
 */
public class NineBrotherPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(NineBrotherPaymentUtil.class);

    private static final String GATEWAY = "http://api.cydjgkj.top/api/pay/createorder";
    private static final String PAY_MEMBERID = "M1722087855";
    private static final String APP_ID = "66a4f9af64dc8f78241fa0cf";
    private static final String API_KEY = "IFAcsZmoP564Egns2bRD8KVoi4vrPdxgSYEb51ETg2DtRIcL3FHgxfIJSuxilDtMGftObHJihcx52tXCMjb6Gd4DjoI72mIXiGmPU5qYNRVxRPdRUM9DrW1CIfk3wtnN";

    public static String getPaymentUrl(String orderId, String applyDate, String amount) {
        try {
            SortedMap<String, String> params = new TreeMap<>();
            params.put("merchNo", PAY_MEMBERID);
            params.put("appId", APP_ID);
            params.put("payOrderId", orderId);
            params.put("payAmt", amount);
            params.put("notifyUrl", "http://" + IpUtils.getPublicIp() + "/api/member/rechargeNotify");
            params.put("reqTime", applyDate);

            // 排序参数字符串
            String stringSignTemp = getSignTemp(params);
            // 拼接商户密钥
            stringSignTemp = stringSignTemp + "&key=" + API_KEY;
            // 加密串
            String sign = getSign(stringSignTemp);

            params.put("sign", sign);

            System.out.println(JSONUtil.toJsonStr(params));

            String result = HttpUtils.sendPostForPayment(GATEWAY, JSONUtil.toJsonStr(params));
            log.info("getPaymentUrl=" + result);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0) {
                    jsonObject = jsonObject.getJSONObject("data");
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("url")) {
                        return jsonObject.getString("url");
                    }
                }
            }
        } catch (Exception e) {
            log.error("getPaymentUrl:", e);
            return null;
        }
        return null;
    }

    public static String getPaymentUrl(String gateway, String merchNo, String appId, String apiKey, String orderId, String applyDate, String amount) {
        try {
            SortedMap<String, String> params = new TreeMap<>();
            params.put("merchNo", merchNo);
            params.put("appId", appId);
            params.put("payOrderId", orderId);
            params.put("payAmt", amount);
            params.put("notifyUrl", "http://" + IpUtils.getPublicIp() + "/api/member/rechargeNotify");
            params.put("reqTime", applyDate);

            // 排序参数字符串
            String stringSignTemp = getSignTemp(params);
            // 拼接商户密钥
            stringSignTemp = stringSignTemp + "&key=" + apiKey;
            // 加密串
            String sign = getSign(stringSignTemp);

            params.put("sign", sign);

            System.out.println(JSONUtil.toJsonStr(params));

            String result = HttpUtils.sendPostForPayment(gateway, JSONUtil.toJsonStr(params));
            log.info("getPaymentUrl=" + result);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0) {
                    jsonObject = jsonObject.getJSONObject("data");
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("url")) {
                        return jsonObject.getString("url");
                    }
                }
            }
        } catch (Exception e) {
            log.error("getPaymentUrl:", e);
            return null;
        }
        return null;
    }

    /**
     * 参数排序
     * @param params
     * @return
     * @throws Exception
     */
    private static String getSignTemp(Map<String, String> params) throws Exception {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        List<String> values = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = String.valueOf(entry.getKey());
            String v = String.valueOf(entry.getValue());
            if (StringUtils.isNotBlank(k) && StringUtils.isNotBlank(v)) {
                values.add(k + "=" + v);
            }
        }
        String result = StringUtils.join(values, "&");
        return result;
    }

    /**
     * 加密
     * @param stringSignTemp
     * @return
     * @throws Exception
     */
    private static String getSign(String stringSignTemp) throws Exception {
        String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes()).toUpperCase();
        return sign;
    }

    public static void main(String[] args) {
        getPaymentUrl("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), "20240727221125", "300");
    }

}

