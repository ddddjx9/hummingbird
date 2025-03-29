package cn.edu.ustb.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SharedHealthMonitor {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, Worker> workers = new ConcurrentHashMap<>();
    private static volatile SharedHealthMonitor instance;

    private SharedHealthMonitor() {

    }

    public static SharedHealthMonitor getInstance() {
        if (instance == null) {
            synchronized (ResourceManager.class) {
                if (instance == null) {
                    instance = new SharedHealthMonitor();
                }
            }
        }
        return instance;
    }

    public void register(Worker worker) {
        workers.put(worker.getId(), worker);
        scheduler.scheduleAtFixedRate(() -> checkWorker(worker), 10, 10, TimeUnit.SECONDS);
    }

    private void checkWorker(Worker worker) {
        if (!worker.checkAlive()) {
            ResourceManager.getInstance().unregisterWorker(worker.getId());
            workers.remove(worker.getId());
        }
    }
}
