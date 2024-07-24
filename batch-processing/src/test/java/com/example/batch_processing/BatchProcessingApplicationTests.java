package com.example.batch_processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.batch_processing.config.BatchConfig;

@SpringBatchTest
@SpringBootTest(classes = {BatchConfig.class, TestBatchConfig.class})
@Import(TestBatchConfig.class)
class BatchProcessingApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context is loaded successfully
    }

    @Test
    void testRetryLogic() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        assertNotNull(jobExecution);
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
