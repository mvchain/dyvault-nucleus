package com.mvc.dyvault.sdk.bean;

import com.mvc.dyvault.sdk.util.RSAEncrypt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

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

    public ToPayEntity(String appKey, String appSecret, BigDecimal cny, Integer tokenId, String orderNumber) {
        this.appKey = appKey;
        this.tokenId = BigInteger.valueOf(tokenId);
        this.orderNumber = orderNumber;
        this.cny = cny;
        JSONObject json = new JSONObject();
        try {
            json.put("appKey", appKey);
            json.put("tokenId", tokenId);
            json.put("orderNumber", orderNumber);
            json.put("cny", cny);
            this.sign = RSAEncrypt.encrypt(json.toString(), appSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
