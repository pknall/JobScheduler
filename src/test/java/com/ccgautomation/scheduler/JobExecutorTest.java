package com.ccgautomation.scheduler;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class JobExecutorTest {

    @Test
    public void submitJobForExecution() {
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.submitJobForExecution(new Job(5000));
        jobExecutor.submitJobForExecution(new Job(10000));
        jobExecutor.submitJobForExecution(new Job(15000));
        jobExecutor.submitJobForExecution(new Job(3000));

        for (int i = 0; i <= 30; i++){
            for (String s : jobExecutor.getJobsInExecutor()) {
                System.out.println(s);
            }
            System.out.println("- - - - - [" + i + "]");
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException ex) {}
        }

        jobExecutor.shutdown();
    }
}