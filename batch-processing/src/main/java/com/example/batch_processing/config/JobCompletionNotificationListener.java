package com.example.batch_processing.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component // Marks this as a Spring-managed component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job started"); // Logs a message before the job starts
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job finished"); // Logs a message after the job finishes
    }
}
