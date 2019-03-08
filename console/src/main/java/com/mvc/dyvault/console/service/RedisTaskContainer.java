package com.mvc.dyvault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.dyvault.console.bean.bo.BlockTransactionBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class RedisTaskContainer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static int runTaskThreadNum = 2;
    private static ExecutorService es = new ThreadPoolExecutor(runTaskThreadNum, runTaskThreadNum,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private RedisQueue redisQueue = null;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    BlockTransactionService blockTransactionService;

    @PostConstruct
    private void init() {
        redisQueue = new RedisQueue(redisTemplate);

        Consumer<String> consumer = (data) -> {
            try {
                BlockTransactionBO blockTransactionBO = JSON.parseObject(data, BlockTransactionBO.class);
                blockTransactionService.doSendTransaction(blockTransactionBO.getUserId(), blockTransactionBO.getTransactionDTO());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            return;
        };

        //提交线程
        for (int i = 0; i < runTaskThreadNum; i++) {
            es.execute(
                    new OrderSendRedisConsumer(this, consumer)
            );
        }
    }

    public RedisQueue getRedisQueue() {
        return redisQueue;
    }

}