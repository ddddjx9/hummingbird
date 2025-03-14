package cn.edu.ustb.component;

import cn.edu.ustb.task.TaskWrapper;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于具体执行任务的工作器
 */
public class Worker implements Runnable{
    private final String id;
    private final BlockingQueue<TaskWrapper<?>> taskQueue = new LinkedBlockingQueue<>();
    private final AtomicInteger freeSlots;
    private volatile long lastHeartbeat = System.currentTimeMillis();

    public Worker(int maxSlots) {
        this.id = UUID.randomUUID().toString();
        this.freeSlots = new AtomicInteger(maxSlots);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TaskWrapper<?> task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task != null) {
                    freeSlots.decrementAndGet();
                    Future<?> future = Executors.newSingleThreadExecutor().submit(task);
                    future.get(); // 阻塞直到任务完成
                    freeSlots.incrementAndGet();
                    updateHeartbeat();
                }
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean submitTask(TaskWrapper<?> task) {
        return taskQueue.offer(task);
    }

    public boolean checkAlive() {
        return System.currentTimeMillis() - lastHeartbeat < 30000;
    }

    private void updateHeartbeat() {
        lastHeartbeat = System.currentTimeMillis();
    }

    public String getId() {
        // 获取worker的id
        return id;
    }
}
