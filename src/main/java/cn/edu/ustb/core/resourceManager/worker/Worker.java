package cn.edu.ustb.core.resourceManager.worker;

import cn.edu.ustb.core.task.Task;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {
    private final String id;
    private final BlockingQueue<Task<?, ?>> taskQueue = new LinkedBlockingQueue<>();
    private final AtomicInteger freeSlots = new AtomicInteger(3);
    private volatile long lastHeartbeat = System.currentTimeMillis();
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();

    public Worker() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * 提交任务到Worker队列
     */
    public boolean submitTask(Task<?, ?> task) {
        if (freeSlots.get() > 0 && taskQueue.offer(task)) {
            freeSlots.decrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * 启动任务处理线程
     */
    public void startProcessing() {
        taskExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task<?, ?> task = taskQueue.poll(1, TimeUnit.SECONDS);
                    if (task != null) {
                        processTask(task);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void processTask(Task<?, ?> task) {
        try {
            task.execute();  // 实际执行任务逻辑
            freeSlots.incrementAndGet();
            updateHeartbeat();
        } catch (Exception e) {
            // 处理任务异常
        }
    }

    /**
     * 检查Worker是否存活
     */
    public boolean checkAlive() {
        return System.currentTimeMillis() - lastHeartbeat < 30000;
    }

    /**
     * 更新心跳时间戳
     */
    public void updateHeartbeat() {
        lastHeartbeat = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public int getFreeSlots() {
        return freeSlots.get();
    }

    /**
     * 关闭Worker资源
     */
    public void shutdown() {
        taskExecutor.shutdownNow();
    }
}