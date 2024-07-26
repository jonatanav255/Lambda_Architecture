package com.example.batch_processing.config;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch_processing.entity.MyEntity;

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

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener) // Add the listener to the job
                .start(step1()) // Start with the defined step
                .build();
    }

    @Bean
    public Step step1() {
        log.info("STEP 1 CONFIGURED");
        return stepBuilderFactory.get("step1")
                .<MyEntity, MyEntity>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .retryLimit(3) // Retry up to 3 times
                .retry(Exception.class) // Retry on any exception
                .skipLimit(5) // Skip up to 5 items in case of skippable exceptions
                .skip(Exception.class) // Skip these exceptions
                .listener(new StepFailureListener()) // Step failure listener
                .build();
    }

    @Bean
    public JpaPagingItemReader<MyEntity> reader() {
        log.info("ITEM READER INITIALIZED");
        return new JpaPagingItemReaderBuilder<MyEntity>()
                .name("jpaReader")
                .entityManagerFactory(entityManagerFactory) // Use the EntityManagerFactory
                .queryString("SELECT e FROM MyEntity e") // Define the query to fetch entities
                .pageSize(10) // Set the page size to 10
                .build();
    }

    @Bean
    public ItemProcessor<MyEntity, MyEntity> processor() {
        return item -> {
            log.info("Processing entity: " + item);
            item.setData(item.getData().toUpperCase()); // Example processing logic
            return item;
        };
    }

    @Bean
    public JpaItemWriter<MyEntity> writer() {
        return new JpaItemWriterBuilder<MyEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
