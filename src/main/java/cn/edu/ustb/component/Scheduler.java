package cn.edu.ustb.component;

import cn.edu.ustb.service.DAGExecutor;
import cn.edu.ustb.service.RetryPolicy;
import cn.edu.ustb.task.TaskWrapper;

import java.util.List;

/**
 * 任务的调度器
 */
public class Scheduler {
    private final DAGExecutor dagExecutor = new DAGExecutor();

    public Scheduler() {

    }

    /**
     * 提交任务列表到调度器
     * @param tasks 要提交的任务列表
     */
    public void submit(List<TaskWrapper<?>> tasks) {
        tasks.forEach(this::submitTask);
    }

    /**
     * 提交单个任务到调度器
     * @param task 要提交的任务
     */
    private void submitTask(TaskWrapper<?> task) {
        dagExecutor.schedule(task);
    }

    /**
     * 在Scheduler中实现重试提交
     * @param task 需要重试的任务
     */
    public void resubmit(TaskWrapper<?> task) {
        RetryPolicy retryPolicy = new RetryPolicy();
        if (retryPolicy.shouldRetry(task)) {
            retryPolicy.calculateBackoff(task.getRetryCount());
            dagExecutor.schedule(task);
            task.incrementRetryCount();
        }
    }
}
