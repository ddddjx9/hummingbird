package cn.edu.ustb.core.resourceManager.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class Worker implements Executor {
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    public void execute(Runnable command) {
        taskQueue.offer(command);
    }

    public void start() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}