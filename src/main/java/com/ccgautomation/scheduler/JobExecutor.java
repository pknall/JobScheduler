package com.ccgautomation.scheduler;

import com.ccgautomation.utilities.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobExecutor {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Configuration.THREAD_POOL_SIZE);

        Job processor = new Job();
        executor.schedule(processor, 5, TimeUnit.SECONDS);

        while(!executor.isTerminated()) {
            System.out.println(processor.getJobStatus());
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {}
        }

        executor.shutdown(); // Rejects any new tasks from being submitted and gracefully shuts down.

    }
}
