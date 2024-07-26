package com.example.batch_processing.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class StepFailureListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getExitStatus().getExitCode().equals("FAILED")) {
            // Log the failure details
            System.err.println("Step failed: " + stepExecution.getStepName());
            System.err.println("Exit status: " + stepExecution.getExitStatus());
            System.err.println("Failure exceptions: " + stepExecution.getFailureExceptions());
            // Additional failure handling logic can be added here
        }
        return stepExecution.getExitStatus();
    }
}
