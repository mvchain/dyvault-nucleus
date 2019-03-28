package com.mvc.dyvault.simulation.bean;

import com.alibaba.fastjson.JSONObject;
import com.mvc.dyvault.simulation.util.EncryptionUtil;
import com.mvc.dyvault.simulation.util.RSAEncrypt;
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
            json.put("appSecret", appSecret);
            this.sign = EncryptionUtil.md5(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
