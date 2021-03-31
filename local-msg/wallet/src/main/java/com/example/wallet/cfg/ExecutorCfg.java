package com.example.wallet.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ExecutorCfg {

    @Bean
    public ExecutorService commonExecutor() {
        ThreadGroup threadGroup = new ThreadGroup("OUR-COMMON");
        AtomicInteger cnt = new AtomicInteger(0);
        return new ThreadPoolExecutor(4, 4, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), r -> {
            return new Thread(threadGroup, r, "Pool-" + cnt.incrementAndGet());
        });
    }
}
