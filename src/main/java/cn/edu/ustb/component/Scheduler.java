package cn.edu.ustb.component;

import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.service.DAGScheduler;
import cn.edu.ustb.service.ExecutorManager;
import cn.edu.ustb.service.RetryPolicy;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

import java.util.List;

/**
 * 任务的调度器
 */
public class Scheduler {
    private final DAGScheduler dagScheduler = new DAGScheduler();
    private final ExecutorManager executor = new ExecutorManager();

    public Scheduler() {

    }

    /**
     * 提交任务列表到调度器
     *
     * @param stages 要提交的任务列表
     */
    public void submit(List<Stage> stages) {
        stages.forEach(this::submitStage);
    }

    /**
     * 提交任务阶段
     *
     * @param stage 给定的阶段
     */
    public void submitStage(Stage stage) {
        for (Task<?> task : stage.getTasks()) {
            executor.submit(() -> {
                task.execute();
                // 通知阶段完成
                return null;
            });
        }
    }

    /**
     * 在Scheduler中实现重试提交
     *
     * @param task 需要重试的任务
     */
    public void resubmit(TaskWrapper<?> task) {
        RetryPolicy retryPolicy = new RetryPolicy();
        if (retryPolicy.shouldRetry(task)) {
            retryPolicy.calculateBackoff(task.getRetryCount());
            dagScheduler.schedule(task);
            task.incrementRetryCount();
        }
    }
}
