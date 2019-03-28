package com.mvc.dyvault.console.bean;

import com.alibaba.fastjson.JSONObject;
import com.mvc.dyvault.console.util.EncryptionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/28 13:30
 */
@Data
@NoArgsConstructor
public class ToPayEntity {

    private BigDecimal cny;
    // now only usdt(4)
    private BigInteger tokenId;
    private String appKey;
    private String sign;
    private String orderNumber;

    public Boolean checkSign(String sign, String appSecret) {
        JSONObject json = new JSONObject();
        json.put("appKey", appKey);
        json.put("tokenId", tokenId);
        json.put("orderNumber", orderNumber);
        json.put("cny", cny);
        json.put("appSecret", appSecret);
        try {
            return sign.equalsIgnoreCase(EncryptionUtil.md5(json.toString()));
        } catch (Exception e) {
            return false;
        }
    }


}
