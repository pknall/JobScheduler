package com.ccgautomation.scheduler;

import java.util.Date;

public class Job implements Runnable {

    private int message;
    private ThreadStatus threadStatus;
    private Date startTime;
    private Date stopTime;

    public Job(int message) {
        this.message = message;
        threadStatus = ThreadStatus.QUEUED;
    }

    public Job() {
        this(3000);
    }

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

    public void setThreadStatus(ThreadStatus threadStatus) {
        this.threadStatus = threadStatus;
    }
}
