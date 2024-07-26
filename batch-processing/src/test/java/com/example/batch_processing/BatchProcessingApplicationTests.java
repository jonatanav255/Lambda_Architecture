package com.example.batch_processing;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext; // Ensure this import is correct

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.batch_processing.entity.MyEntity;

@SpringBootTest
@EnableBatchProcessing
class BatchProcessingApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context is loaded successfully
    }

    @Test
    void testRetryLogic() throws Exception {
        // Set up job parameters with a unique timestamp
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // Launch the job
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        // Verify that the job execution is not null and has completed successfully
        assertNotNull(jobExecution);
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

        // Verify that the processed data is saved to the database
        List<MyEntity> entities = entityManager.createQuery("SELECT e FROM MyEntity e", MyEntity.class).getResultList();
        assertNotNull(entities);
        assertEquals(3, entities.size()); // Assuming we processed 3 items
        entities.forEach(entity -> assertEquals(entity.getData(), entity.getData().toUpperCase()));
    }
}
