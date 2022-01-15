package com.example.demo.service.impl;

import com.example.demo.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @className: com.example.demo.service.impl-> AsyncServiceImpl
 * @description: TODO
 * @author: LQKun
 * @date: 2021-08-15 22:35
 * @version: 1.0
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync() {
        log.info("start executeAsync");

        System.out.println("异步线程要做的事情");
        System.out.println("可以在这里执行批量插入等耗时的事情");

        log.info("end executeAsync");
    }
}
