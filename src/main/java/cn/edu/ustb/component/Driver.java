package cn.edu.ustb.component;

import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.service.DAGScheduler;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 客户端提交任务的进程
 */
public class Driver {
    private final Scheduler scheduler;
    private final Map<String, TaskWrapper<?>> taskRegistry = new ConcurrentHashMap<>();
    private final DAGScheduler dagScheduler;
    private final Logger logger = LoggerFactory.getLogger(Driver.class);

    public Driver() {
        this.scheduler = new Scheduler();
        dagScheduler = new DAGScheduler();
        startHealthMonitor();
    }

    /**
     * 接收原始任务并生成可执行DAG
     *
     * @param rootTask 用户提交的根任务
     * @return 任务执行凭证（可用于查询进度）
     */
    public <T> String submit(Task<T> rootTask) {
        // 生成任务ID并构建Wrapper
        TaskWrapper<T> rootWrapper = new TaskWrapper<T>(rootTask);
        taskRegistry.put(rootWrapper.getTaskId(), rootWrapper);

        // 递归拆解依赖
        List<TaskWrapper<?>> flattenedTasks = dagScheduler.flattenDependencies(rootWrapper);

        // 注册所有子任务
        flattenedTasks.forEach(task ->
                taskRegistry.put(task.getTaskId(), task)
        );

        // 提交至调度器
        scheduler.submit(null);

        return rootWrapper.getTaskId();
    }

    /**
     * 后台线程监控任务健康状态
     */
    private void startHealthMonitor() {
        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            taskRegistry.values().forEach(task -> {
                if (task.isTimedOut()) {
                    handleTaskFailure(task);
                }
            });
        }, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * 任务失败处理策略
     */
    private void handleTaskFailure(TaskWrapper<?> failedTask) {
        // 记录失败日志
        logger.error("Task {} failed, initiating recovery", failedTask.getTaskId());

        // 重试逻辑（最多3次）
        if (failedTask.getRetryCount() < 3) {
            scheduler.resubmit(failedTask);
        } else {
            // 触发全局回滚
            rollbackDependentTasks(failedTask);
        }
    }

    private void rollbackDependentTasks(TaskWrapper<?> failedTask) {
        // 获取所有依赖任务
        List<TaskWrapper<?>> dependencies = failedTask.getDependencies();
        for (TaskWrapper<?> dep : dependencies) {
            // 如果依赖任务未完成，则重试
            if (dep.getStatus() != TaskStatus.SUCCESS) {
                scheduler.resubmit(dep);
            }
        }
    }

    public CompletableFuture<Integer> getResultFuture(Task<Integer> rootTask) {
        // 异步获取任务执行结果
        TaskWrapper<Integer> rootWrapper = new TaskWrapper<Integer>(rootTask);
        taskRegistry.put(rootWrapper.getTaskId(), rootWrapper);
        return rootWrapper.getResultFuture();
    }
}