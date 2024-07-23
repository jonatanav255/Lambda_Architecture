package com.example.batch_processing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobCompletionNotificationListener listener;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        log.info("STEP 1 CONFIGURED");
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(1) // Use chunk size of 1 for simplicity
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<String> reader() {
        log.info("ITEM READER INITIALIZED");
        return new ItemReader<String>() {
            private String[] messages = {"message1", "message2", "message3"};
            private int count = 0;

            @Override
            public String read() {
                log.info("Reader read method called");
                if (count < messages.length) {
                    String message = messages[count++];
                    log.info("Reading message: " + message);
                    return message;
                } else {
                    log.info("All messages read.");
                    return null; // indicates the end of the reading
                }
            }
        };
    }

    @Bean
    public ItemProcessor<String, String> processor() {
        return item -> {
            log.info("Processing message: " + item);
            return item.toUpperCase();
        };
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                log.info("Writing message: " + item);
            }
        };
    }
}
