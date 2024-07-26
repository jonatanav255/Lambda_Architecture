package com.example.batch_processing;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableBatchProcessing
class BatchProcessingApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context is loaded successfully
    }

    // @Test
    // void testRetryLogic() throws Exception {
    //     JobParameters jobParameters = new JobParametersBuilder()
    //             .addLong("time", System.currentTimeMillis())
    //             .toJobParameters();
    //     JobExecution jobExecution = jobLauncher.run(job, jobParameters);
    //     assertNotNull(jobExecution);
    //     assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    // }
}
