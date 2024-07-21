package com.example.batch_processing.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

// Indicates that this class is a Spring-managed component
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    // This method is called before the job starts
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job started");
    }

    // This method is called after the job finishes
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job finished");
    }
}
