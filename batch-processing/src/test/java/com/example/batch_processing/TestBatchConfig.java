package com.example.batch_processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch_processing.entity.MyEntity;

@Configuration
public class TestBatchConfig {

    private static final Logger log = LoggerFactory.getLogger(TestBatchConfig.class);

    private int retryCount = 0;

    @Bean
    public ItemProcessor<MyEntity, MyEntity> testProcessor() {
        return item -> {
            retryCount++;
            if (retryCount <= 2) {
                log.info("Simulated transient error on retry: " + retryCount);
                throw new RuntimeException("Simulated transient error");
            }
            log.info("Processing entity after retry: " + item);
            item.setData(item.getData().toUpperCase()); // Example processing logic
            return item;
        };
    }
}
