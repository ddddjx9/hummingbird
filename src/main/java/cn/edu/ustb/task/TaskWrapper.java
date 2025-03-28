package cn.edu.ustb.task;

import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.model.transformation.Transformation;
import cn.edu.ustb.task.impl.Task;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * 任务的包装类
 *
 * @param <T> 执行的任务类型
 */
public class TaskWrapper<T> implements Task<T>, Serializable {
    private final Task<T> task;
    private final String taskId;
    private volatile TaskStatus status = TaskStatus.PENDING;
    private T result;
    private List<TaskWrapper<?>> dependencies;
    private volatile int retryCount = 0;

    public TaskWrapper(Task<T> task) {
        this.task = task;
        this.taskId = task.getTaskId();
    }

    @Override
    public T call() throws Exception {
        if (status != TaskStatus.PENDING) return null;

        status = TaskStatus.WAITING_DEPENDENCIES;

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

    public List<TaskWrapper<?>> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<TaskWrapper<?>> dependencies) {
        this.dependencies=dependencies;
    }

    @Override
    public T execute() {
        return null;
    }

    public String getTaskId() {
        return taskId;  
    }

    @Override
    public T executeTransformations(List<Transformation<?>> transformations) {
        T result = null;
        for (Transformation<?> transformation : transformations) {
            // 假设 Transformation 类有一个 apply 方法用于执行算子
            result = (T) transformation.apply(result);
        }
        return result;
    }

    public Task<T> getTask() {
        return task;
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
