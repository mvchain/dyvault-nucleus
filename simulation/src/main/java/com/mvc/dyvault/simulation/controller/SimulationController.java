package com.mvc.dyvault.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.CallbackDTO;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.simulation.bean.SimulationDTO;
import com.mvc.dyvault.simulation.bean.ToPayEntity;
import com.mvc.dyvault.simulation.bean.ToPayResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author qiyichen
 * @create 2019/3/28 13:24
 */
@RestController
@Api(tags = "order")
@RequestMapping("order")
public class SimulationController {

    RestTemplate restTemplate = new RestTemplate();
    @Value("${simulation.appSecret}")
    private String appSecret;
    @Value("${simulation.appKey}")
    private String appKey;
    @Autowired
    StringRedisTemplate redisTemplate;
    private static String url = "http://localhost:10079/order/build";

    @PostMapping("callback")
    @ApiOperation("callback url")
    public Result<Boolean> updateOrder(@RequestBody CallbackDTO callbackDTO) {
        System.out.println(JSON.toJSONString(callbackDTO));
        return new Result(true);
    }

    @PostMapping
    @ApiOperation("create order")
    public Result<ConfirmOrderDTO> createOrder(@RequestBody SimulationDTO simulationDTO) {
        ToPayEntity entity = build(simulationDTO);
        ResponseEntity<ToPayResponse> result = restTemplate.postForEntity(url, entity, ToPayResponse.class);
        return new Result(result.getBody().getConfirmOrderDTO());
    }

    public ToPayEntity build(SimulationDTO simulationDTO) {
        ToPayEntity entity = new ToPayEntity(appKey, appSecret, simulationDTO.getCny(), 4, getOrderNumber());
        return entity;
    }

    protected String getOrderNumber() {
        Long id = redisTemplate.boundValueOps("SIMULATION_ORDER").increment();
        return "SIMULATION_ORDER_" + String.format("%09d", id);
    }

}
