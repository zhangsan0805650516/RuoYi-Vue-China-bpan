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

import java.math.BigDecimal;
import java.util.*;

public class HuoJianPaymentUtil {

    private static final Logger log = LoggerFactory.getLogger(HuoJianPaymentUtil.class);

//    private static final String GATEWAY = "https://api.huojian68.com/v1/payment";
//    private static final String PAY_MEMBERID = "jiangnan";
//    private static final String APP_ID = "jiangnan";
//    private static final String API_KEY = "7900c5e1-5723-4fc9-a469-d940074d809b";

    public static String getPaymentUrl(String gateway, String appId, String apiKey, String orderId, Date applyDate, BigDecimal amount) {
        try {
            SortedMap<String, Object> params = new TreeMap<>();
            params.put("app_id", appId);
            params.put("amount", amount.toString());
            params.put("order_no", orderId);
            params.put("ts", applyDate.getTime() / 1000);
            params.put("typ_id", 2);
            params.put("notify", "http://" + IpUtils.getPublicIp() + "/api/member/rechargeNotify");

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
//                    jsonObject = jsonObject.getJSONObject("data");
                    if (ObjectUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("payload")) {
                        return jsonObject.getString("payload");
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
    private static String getSignTemp(Map<String, Object> params) throws Exception {
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
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
        String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes()).toLowerCase();
        return sign;
    }

//    public static void main(String[] args) {
//        getPaymentUrl("RE" + OrderUtil.orderSn() + OrderUtil.randomNumber(0,9).intValue(), new Date(), "500.00");
//    }

}
