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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
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
                .<MyEntity, MyEntity>chunk(10) // Set chunk size to 10
                .reader(reader()) // Set the reader
                .processor(processor()) // Set the processor
                .writer(writer(entityManagerFactory)) // Set the writer
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
        return new ItemProcessor<MyEntity, MyEntity>() {
            private static final int MAX_RETRIES = 3;

            @Override
            public MyEntity process(MyEntity item) throws Exception {
                int retryCount = 0;
                boolean success = false;
                while (!success && retryCount < MAX_RETRIES) {
                    try {
                        log.info("Processing entity: " + item);
                        item.setData(item.getData().toUpperCase()); // Example processing logic
                        success = true;
                    } catch (Exception e) {
                        retryCount++;
                        log.error("Error processing entity: " + item + ", retrying (" + retryCount + "/" + MAX_RETRIES + ")", e);
                        if (retryCount >= MAX_RETRIES) {
                            throw e; // If max retries reached, rethrow the exception
                        }
                    }
                }
                return item;
            }
        };
    }

    @Bean
    public ItemWriter<MyEntity> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<MyEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
