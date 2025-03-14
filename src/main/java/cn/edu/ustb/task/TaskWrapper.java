package cn.edu.ustb.task;

import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.task.impl.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * 任务的包装类
 *
 * @param <T> 执行的任务类型
 */
public class TaskWrapper<T> implements Callable<T>, Serializable {
    private final Task<T> task;
    private final String taskId;
    private volatile TaskStatus status = TaskStatus.PENDING;
    private T result;
    private final List<TaskWrapper<?>> dependencies = new ArrayList<>();
    private final CountDownLatch dependencyLatch = new CountDownLatch(0);
    private volatile int retryCount = 0;

    public TaskWrapper(Task<T> task) {
        this.task = task;
        this.taskId = UUID.randomUUID().toString();
        // 初始化依赖
        for (Task<?> dep : task.getDependencies()) {
            dependencies.add(new TaskWrapper<>(dep));
            dependencyLatch.countDown();
        }
    }

    @Override
    public T call() throws Exception {
        if (status != TaskStatus.PENDING) return null;

        status = TaskStatus.WAITING_DEPENDENCIES;
        dependencyLatch.await(); // 等待依赖完成

        status = TaskStatus.RUNNING;
        try {
            result = task.execute();
            status = TaskStatus.SUCCESS;
            return result;
        } catch (Exception e) {
            status = TaskStatus.FAILED;
            throw e;
        }
    }

    // 实现Future接口核心方法
    public boolean isDone() {
        return status.isTerminalState();
    }

    public T get() throws InterruptedException {
        while (!isDone()) {
            Thread.sleep(100);
        }
        return result;
    }

    public List<TaskWrapper<?>> getDependencies() {
        return dependencies;
    }

    public String getTaskId() {
        return taskId;  
    }

    public TaskStatus getStatus() {
        return status;
    }

    public boolean isTimedOut() {
        return status == TaskStatus.FAILED && result instanceof TimeoutException;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void incrementRetryCount() {
        retryCount++;
    }

    public CompletableFuture<Integer> getResultFuture() {
        // 异步获取任务执行结果
        throw new UnsupportedOperationException("Unimplemented method 'getResultFuture'");
    }
}
