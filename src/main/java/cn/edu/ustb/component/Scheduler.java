package cn.edu.ustb.component;

import cn.edu.ustb.service.DAGExecutor;
import cn.edu.ustb.service.RetryPolicy;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

/**
 * 任务的调度器
 */
public class Scheduler {
    private final ResourceManager rm;
    private final DAGExecutor dagExecutor = new DAGExecutor(this);

    public Scheduler(ResourceManager rm) {
        this.rm = rm;
    }

    public ResourceManager getResourceManager() {
        return rm;
    }

    public void submit(Task<?> rootTask) {
        TaskWrapper<?> rootWrapper = new TaskWrapper<>(rootTask);
        dagExecutor.schedule(rootWrapper);
    }

    // 在Scheduler中实现重试提交
    public void resubmit(TaskWrapper<?> task) {
        RetryPolicy retryPolicy = new RetryPolicy();
        if (retryPolicy.shouldRetry(task)) {
            retryPolicy.calculateBackoff(task.getRetryCount());
            dagExecutor.schedule(task);
           task.incrementRetryCount();
        }
    }
}
