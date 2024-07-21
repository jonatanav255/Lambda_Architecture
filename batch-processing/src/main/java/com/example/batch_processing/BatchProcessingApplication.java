package com.example.batch_processing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Indicates that this is a Spring Boot application
@SpringBootApplication
public class BatchProcessingApplication implements CommandLineRunner {

    // Autowires the JobLauncher to launch the job
    @Autowired
    private JobLauncher jobLauncher;

    // Autowires the job to be executed
    @Autowired
    private Job job;

    // The main method to run the Spring Boot application
    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }

    // This method is executed after the application context is loaded
    @Override
    public void run(String... args) throws Exception {
        // Launches the job with empty parameters
        jobLauncher.run(job, new JobParameters());
    }
}
