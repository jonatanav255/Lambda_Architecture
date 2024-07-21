package com.example.batch_processing.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Indicates that this class is a configuration class
@Configuration
// Enables Spring Batch processing
@EnableBatchProcessing
public class BatchConfig {

    // Autowires the JobCompletionNotificationListener to listen for job events
    @Autowired
    private JobCompletionNotificationListener listener;

    // Defines the job bean
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("job")
                // Ensures each job run has a unique ID
                .incrementer(new RunIdIncrementer())
                // Adds the listener to the job
                .listener(listener)
                // Sets the starting step of the job
                .start(step1(stepBuilderFactory))
                .build();
    }

    // Defines the step bean
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step1")
                // Sets the tasklet for this step
                .tasklet(tasklet())
                .build();
    }

    // Defines the tasklet bean
    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            // This is the business logic of the tasklet
            System.out.println("Batch processing tasklet");
            // Indicates that the tasklet has finished executing
            return RepeatStatus.FINISHED;
        };
    }
}
