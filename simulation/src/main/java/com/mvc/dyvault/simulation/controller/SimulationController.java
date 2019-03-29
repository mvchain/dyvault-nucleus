package com.mvc.dyvault.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.CallbackDTO;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.simulation.bean.Order;
import com.mvc.dyvault.simulation.bean.SimulationDTO;
import com.mvc.dyvault.simulation.bean.ToPayEntity;
import com.mvc.dyvault.simulation.bean.ToPayResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    @ApiOperation("get order list")
    public Result<List<Order>> getList(@RequestParam String uid) {
        Map<Object, Object> set = redisTemplate.opsForHash().entries("SIMULATION_ORDER" + uid);
        List<Order> result = new ArrayList<>();
        set.values().forEach(str -> {
            Order order = JSON.parseObject((String) str, Order.class);
            result.add(order);
        });
        return new Result<>(result);
    }


    @PostMapping("callback")
    @ApiOperation("callback url")
    public Result<Boolean> updateOrder(@RequestBody CallbackDTO callbackDTO) {
        System.out.println(JSON.toJSONString(callbackDTO));
        String jsonStr = (String) redisTemplate.opsForHash().get("SIMULATION_ORDER" + callbackDTO.getOrderNumber().split("#")[0], callbackDTO.getOrderNumber());
        Order order = JSON.parseObject(jsonStr, Order.class);
        order.setStatus(callbackDTO.getStatus());
        redisTemplate.opsForHash().put("SIMULATION_ORDER" + callbackDTO.getOrderNumber().split("#")[0], callbackDTO.getOrderNumber(), JSON.toJSONString(order));
        return new Result(true);
    }

    @PostMapping
    @ApiOperation("create order")
    public Result<ConfirmOrderDTO> createOrder(@RequestBody SimulationDTO simulationDTO) {
        ToPayEntity entity = build(simulationDTO);
        ResponseEntity<ToPayResponse> result = restTemplate.postForEntity(url, entity, ToPayResponse.class);
        Order order = new Order();
        order.setCny(simulationDTO.getCny());
        order.setCreatedAt(System.currentTimeMillis());
        order.setOrderNumber(result.getBody().getConfirmOrderDTO().getOrderNumber());
        order.setStatus(0);
        redisTemplate.opsForHash().put("SIMULATION_ORDER" + simulationDTO.getUid(), order.getOrderNumber(), JSON.toJSONString(order));
        return new Result(result.getBody().getConfirmOrderDTO());
    }

    public ToPayEntity build(SimulationDTO simulationDTO) {
        ToPayEntity entity = new ToPayEntity(appKey, appSecret, simulationDTO.getCny(), 4, getOrderNumber(simulationDTO.getUid()));
        return entity;
    }

    protected String getOrderNumber(String uid) {
        Long id = redisTemplate.boundValueOps("SIMULATION_ORDER").increment();
        return uid + "#SIMULATION_ORDER_" + String.format("%09d", id);
    }

}
