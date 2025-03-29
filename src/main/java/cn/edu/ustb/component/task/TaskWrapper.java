package cn.edu.ustb.component.task;

import cn.edu.ustb.component.TaskStateManager;
import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.model.transformation.Transformation;
import cn.edu.ustb.component.task.impl.Task;

import java.io.Serializable;
import java.util.List;
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
    private T result;
    private List<TaskWrapper<?>> dependencies;
    private volatile int retryCount = 0;
    private final String stateId;

    public TaskWrapper(Task<T> task, String stateId) {
        this.task = task;
        this.taskId = task.getTaskId();
        this.stateId = stateId;
    }

    @Override
    public T call() throws Exception {
        return null;
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
        return TaskStateManager.getInstance().getState(stateId).getStatus();
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
