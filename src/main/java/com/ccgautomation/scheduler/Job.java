package com.ccgautomation.scheduler;

import java.util.Date;
import java.util.concurrent.Future;

/* Part of the challenge here is that none of the functionality of this "Job" can block the main user thread.
 * This means that the main user thread cannot be used to:
 * (1) Execute the Job
 * (2) To Callback and fetch the results (if any)
 *
 */
//public class Job implements Runnable {
public class Job implements Runnable {

    private String name;
    private int message;
    private ThreadStatus threadStatus;
    private Future<String> futureResults = null;
    private Thread threadRunningJob = null;         // This thread runs the Job so the main user thread doesn't block
    private Runnable runningJob = null;

    public Job(int message) {
        this.name = "Thread" + new Date().getTime();
        this.message = message;
        threadStatus = ThreadStatus.QUEUED;
    }

    public Job() {
        this(3000);
    }

    // Part of Runnable
    @Override
    public void run() {
        System.out.println(Thread.currentThread() + " [RECEIVED] Message = " + message);
        threadStatus = ThreadStatus.PROCESSING;
        respondToMessage();
        System.out.println(Thread.currentThread() + "[DONE] Processing Message " + message);
        threadStatus = ThreadStatus.DONE;
    }

    private void respondToMessage() {
        try {
            Thread.sleep(message);
        }
        catch (InterruptedException ex) {

        }
    }

    public ThreadStatus getJobStatus() {
        return threadStatus;
    }

    public void setJobStatus(ThreadStatus threadStatus) {
        this.threadStatus = threadStatus;
    }

    public void setThreadStatus(ThreadStatus threadStatus) {
        this.threadStatus = threadStatus;
    }

    public void setFutureResults(Future<String> futureResults) {
        this.futureResults = futureResults;
    }

    public void setThreadRunningJob(Thread threadRunningJob) {
        this.threadRunningJob = threadRunningJob;
    }

    public Thread getThreadRunningJob() {
        return this.threadRunningJob;
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" : " );
        sb.append(threadStatus);
        if (threadRunningJob != null) {
            sb.append(" : " );
            sb.append(threadRunningJob.getName());
        }
        return sb.toString();
    }

    public Runnable getRunningJob() {
        return this.runningJob;
    }

    public void setRunningJob(Runnable runningJob) {
        this.runningJob = runningJob;
    }
}
