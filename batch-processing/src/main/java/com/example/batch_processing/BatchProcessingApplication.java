package com.example.batch_processing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Marks this as a Spring Boot application
public class BatchProcessingApplication implements CommandLineRunner {

    @Autowired // Injects the JobLauncher bean
    private JobLauncher jobLauncher;

    @Autowired // Injects the Job bean
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingApplication.class, args); // Starts the Spring Boot application
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // Adds a unique parameter to the job to ensure it runs each time
                .toJobParameters();
        jobLauncher.run(job, params); // Launches the job with the specified parameters
    }
}
