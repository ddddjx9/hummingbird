package cn.edu.ustb.core.task.service;

import cn.edu.ustb.core.resourceManager.worker.Worker;
import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.resourceManager.worker.ExecutorManager;
import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.task.TaskWrapper;
import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.model.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 任务的调度器
 */
public class TaskScheduler {
    private final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    protected TaskScheduler(DAGScheduler dagScheduler, ResourceManager rm, ExecutorManager executorManager) {

    }

    /**
     * 提交任务列表到调度器
     *
     * @param stages 要提交的任务列表
     */
    public void submitStages(List<Stage> stages) {
        for (Stage stage : stages) {
            List<TaskWrapper<?,?>> tasks = createTasks(stage);
            scheduleTasks(tasks);
        }
    }

    private List<TaskWrapper<?,?>> createTasks(Stage stage) {
        List<TaskWrapper<?,?>> tasks = new ArrayList<>();
        for (int partitionId = 0; partitionId < stage.getNumPartitions(); partitionId++) {
            TaskWrapper<?,?> task = new TaskWrapper<>(
                    stage.getStageId(),
                    partitionId,
                    stage.getTransformations(), // 传递该阶段的所有算子
                    stage.getDependencies()
            );
            tasks.add(task);
        }
        return tasks;
    }

    private void scheduleTasks(List<TaskWrapper<?,?>> tasks) {
        for (TaskWrapper<?,?> task : tasks) {
            Worker worker;
            try {
                worker = ResourceManager.getInstance().allocateWorker(6000, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            worker.submitTask(task);
        }
    }

    /**
     * 任务失败处理策略
     */
    private void handleTaskFailure(TaskWrapper<?,?> failedTask) {
        // 记录失败日志
        logger.error("Task {} failed, initiating recovery", failedTask.getTaskId());

        // 重试逻辑
        if (failedTask.getRetryCount() < 3) {
            resubmit(failedTask);
        } else {
            // 触发全局回滚
            rollbackDependentTasks(failedTask);
        }
    }

    private void rollbackDependentTasks(TaskWrapper<?,?> failedTask) {
        // 获取所有依赖任务
        List<? extends TaskWrapper<?, ?>> dependencies = failedTask.getDependencies();
        for (TaskWrapper<?,?> dep : dependencies) {
            // 如果依赖任务未完成，则重试
            if (dep.getStatus() != TaskStatus.SUCCESS) {
                resubmit(dep);
            }
        }
    }

    public void resubmit(TaskWrapper<?,?> task) {

    }
}
