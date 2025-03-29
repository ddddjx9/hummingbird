package cn.edu.ustb.component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 给定的资源分配管理者
 */
public class ResourceManager {
    private final Map<String, Worker> workers = new ConcurrentHashMap<>();
    private final BlockingQueue<Worker> freeWorkerQueue = new LinkedBlockingQueue<>();
    private static volatile ResourceManager instance;
    
    // 私有构造函数，防止外部直接创建实例
    private ResourceManager() {}
    
    // 双重检查锁定实现单例
    public static ResourceManager getInstance() {
        if (instance == null) {
            synchronized (ResourceManager.class) {
                if (instance == null) {
                    instance = new ResourceManager();
                }
            }
        }
        return instance;
    }

    // 注册Worker并启动心跳线程
    // 修改Worker注册方法
    public void registerWorker(Worker worker) {
        workers.put(worker.getId(), worker);
        freeWorkerQueue.offer(worker);

        // 使用共享调度器替代每个Worker独立线程
        SharedHealthMonitor.getInstance().register(worker);
    }

    // 分配Worker
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

    public void unregisterWorker(String id) {

    }
}
