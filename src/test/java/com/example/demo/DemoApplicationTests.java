package com.example.demo;

import com.example.demo.queue.RedisDelayingQueue;
import com.example.demo.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Date;
@Slf4j
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private AsyncService asyncService;

    @Test
    void contextLoads() {
    }

    @Resource
    private RedisTemplate<String, Object> redisTemplate;//使用redisTemplate操作redis

    @Test
    public void redisTest() {
        RedisDelayingQueue queue = new RedisDelayingQueue(redisTemplate, "q-demo");
        Thread product = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    queue.delay("choose" + i);
                }
            }
        };
        Thread consumer = new Thread() {
            @Override
            public void run() {
                queue.loop();
            }
        };

        System.out.println("开始时间为" + new Date());

        product.start();
        consumer.start();
        try {
            product.join();
            Thread.sleep(5100);
            consumer.interrupt();
            consumer.join();
            System.out.println("结束时间为" + new Date());
        } catch (InterruptedException e) {
            System.out.println("error>>>>>>>>>>>>>>>>>>>>>>>>");
        }

    }

    @Test
    public void executeAsync() {
        for (int i = 0; i < 5; i++) {
            asyncService.executeAsync();
        }

    }
}
