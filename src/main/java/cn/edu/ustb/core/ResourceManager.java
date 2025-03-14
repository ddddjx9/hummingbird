package cn.edu.ustb.core;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 给定的资源分配管理者
 */
public class ResourceManager {
    private final Map<String, Worker> workers = new ConcurrentHashMap<>();
    private final BlockingQueue<Worker> freeWorkerQueue = new LinkedBlockingQueue<>();

    // 注册Worker并启动心跳线程
    public void registerWorker(Worker worker) {
        workers.put(worker.getId(), worker);
        freeWorkerQueue.offer(worker);
        startHeartbeatCheck(worker);
    }

    // 分配Worker（带超时机制）
    public Worker allocateWorker(long timeout, TimeUnit unit)
            throws InterruptedException {
        return freeWorkerQueue.poll(timeout, unit);
    }

    // 心跳检测线程
    private void startHeartbeatCheck(Worker worker) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (!worker.checkAlive()) {
                workers.remove(worker.getId());
                freeWorkerQueue.remove(worker);
                scheduler.shutdown();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}
