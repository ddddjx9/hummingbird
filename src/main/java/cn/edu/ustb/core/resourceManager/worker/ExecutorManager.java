package cn.edu.ustb.core.resourceManager.worker;

import java.util.concurrent.*;

public class ExecutorManager {

    private static final ExecutorManager INSTANCE = new ExecutorManager();

    private final ExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    // 私有构造函数，防止外部实例化
    private ExecutorManager() {
    }

    // 提供全局访问点
    public static ExecutorManager getInstance() {
        return INSTANCE;
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }
}
