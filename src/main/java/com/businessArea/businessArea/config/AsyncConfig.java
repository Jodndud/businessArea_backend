// 📁 com/businessArea/businessArea/config/AsyncConfig.java
package com.businessArea.businessArea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);    // 기본 실행 대기 스레드 수
        executor.setMaxPoolSize(5);     // 최대 스레드 수
        executor.setQueueCapacity(10);  // 큐 ظرفیت
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}