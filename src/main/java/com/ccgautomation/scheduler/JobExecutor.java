package com.ccgautomation.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    public final ThreadPoolExecutor threadPoolExecutor;                                 // ThreadPoolExecutor (TPE)
    public final ArrayList<Job> jobsToSubmitForExecution;                               // Add new jobs to this Collection
    private final ArrayList<Job> jobsInExecutor;                                        // Keep track of what is in TPE
    private volatile boolean done = false;                                              // Sentinel

    public JobExecutor() {
        this.jobsToSubmitForExecution = new ArrayList<>();
        this.jobsInExecutor = new ArrayList<>();
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(JobExecutorConfig.THREAD_POOL_SIZE);

        // You may be in the main user's thread at this point
        // Attach the launcher() method to a thread and start to prevent blocking the user's thread
        // This is a high priority thread, make sure it is not a daemon thread
        // Prevents JVM from exiting until this thread is terminated
        Thread launcherThread = new Thread(this::launcher, "Job Launcher");
        if (launcherThread.isDaemon()) {
            launcherThread.setDaemon(false);
        }
        launcherThread.start();
    }

    /**
     * Used to submit Jobs to the Executor
     * @param jobToSubmitForExecution
     */
    public void submitJobForExecution(Job jobToSubmitForExecution) {
        Objects.requireNonNull(jobToSubmitForExecution, "Null Job sent to JobScheduler.addJobForExecution()");
        jobsToSubmitForExecution.add(jobToSubmitForExecution);
    }

    public List<String> getJobsInExecutor() {
        List<String> results = new ArrayList<>();
        System.out.println("Active Jobs in TPE: " + threadPoolExecutor.getActiveCount());
        if (jobsInExecutor.size() > 0) {
            for (Job job:jobsInExecutor) {
                results.add(job.getName());
            }
        }
        else {
            results.add("Empty");
        }
        return results;
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    /* Run on a different thread than the main user thread.
     * Submit tasks from the jobsToSubmitForExecution collection to the threadPoolExecutor
     */
    private void launcher() {
        while (!done) {
            while (jobsToSubmitForExecution.size() > 0) {
                Job jobToSubmitForExecution = jobsToSubmitForExecution.remove(0);           // Get Next Job
                jobsInExecutor.add(jobToSubmitForExecution);                                      // Add Job to Tracked List

                jobToSubmitForExecution.setThreadStatus(ThreadStatus.QUEUED);
                jobToSubmitForExecution.setRunningJob(() -> runJob(jobToSubmitForExecution));     // Give Job a reference to Runnable
                threadPoolExecutor.execute(jobToSubmitForExecution.getRunningJob());              // Added to Executor
                //jobToSubmitForExecution.setFutureResults(threadPoolExecutor.submit(jobToSubmitForExecution));  // Callable
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            catch(InterruptedException ex) {}
        }
        threadPoolExecutor.shutdown();
    }

    // Worker bee
    private void runJob(Job jobToRun) {
        jobToRun.setJobStatus(ThreadStatus.PROCESSING);
        jobToRun.setThreadRunningJob(Thread.currentThread());       // New thread is assigned by Executor | Give the Job a handle to its own thread (this thread)

        try {
            jobToRun.run();                                         // Blocking Call
        }
        catch(Throwable t) {
            logger.error("Error during job '{}' execution", jobToRun.getName(), t);
        }
        jobToRun.setJobStatus(ThreadStatus.DONE);
        jobToRun.setThreadRunningJob(null);                         // Party is over, remove the reference to the thread about to end
    }
}
