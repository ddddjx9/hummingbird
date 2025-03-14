package cn.edu.ustb.service;

import java.util.concurrent.*;

public class ExecutorManager {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public ExecutorManager() {

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
