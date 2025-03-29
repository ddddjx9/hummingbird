package cn.edu.ustb.core.task;

import cn.edu.ustb.core.task.service.TaskStateManager;
import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.model.transformation.Transformation;
import cn.edu.ustb.core.task.impl.Task;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 任务的包装类
 *
 * @param <T> 执行的任务类型
 */
public class TaskWrapper<T,R> implements Task<T>, Serializable {
    private Task<T> task = null;
    private String taskId = null;
    private List<TaskWrapper<T,R>> dependencies;
    private final int retryCount = 0;
    private final String stageId;
    private List<Transformation<T,R>> transformations = null;

    public TaskWrapper(Task<T> task, String stateId) {
        this.task = task;
        this.taskId = task.getTaskId();
        this.stageId = stateId;
    }

    public TaskWrapper(String stageId, int partitionId, List<Transformation<T,R>> transformations, List<TaskWrapper<T,R>> tasks) {
        this.stageId = stageId;
        this.dependencies = tasks;
        this.transformations = transformations;
    }

    @Override
    public T call() throws Exception {
        return null;
    }

    public List<TaskWrapper<T,R>> getDependencies() {
        return dependencies;
    }

    @Override
    public T execute() {
        return task.execute();
    }

    public String getTaskId() {
        return taskId;
    }

    public Task<T> getTask() {
        return task;
    }

    public TaskStatus getStatus() {
        return TaskStateManager.getInstance().getState(taskId).getStatus();
    }

    public int getRetryCount() {
        return retryCount;
    }

    public CompletableFuture<Integer> getResultFuture() {
        // 异步获取任务执行结果
        throw new UnsupportedOperationException("Unimplemented method 'getResultFuture'");
    }

    public boolean isTimedOut() {
        return false;
    }
}
