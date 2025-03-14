package cn.edu.ustb.core;

import cn.edu.ustb.service.DagParser;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 客户端提交任务的进程
 */
public class Driver {
    private final Scheduler scheduler;
    private final Map<String, TaskWrapper<?>> taskRegistry = new ConcurrentHashMap<>();
    private final DagParser dagParser = new DagParser();
    private final ExecutorService taskMonitor = Executors.newCachedThreadPool();

    public Driver() {
        ResourceManager resourceManager = new ResourceManager();
        this.scheduler = new Scheduler(resourceManager);
        startHealthMonitor(); // 启动后台监控线程
    }

    /**
     * 接收原始任务并生成可执行DAG
     * @param rootTask 用户提交的根任务
     * @return 任务执行凭证（可用于查询进度）
     */
    public <T> String submit(Task<T> rootTask) {
        // 生成任务ID并构建Wrapper
        TaskWrapper<T> rootWrapper = new TaskWrapper<>(rootTask);
        taskRegistry.put(rootWrapper.getTaskId(), rootWrapper);

        // 递归拆解依赖
        List<TaskWrapper<?>> flattenedTasks = dagParser.flattenDependencies(rootWrapper);

        // 注册所有子任务
        flattenedTasks.forEach(task ->
                taskRegistry.put(task.getTaskId(), task)
        );

        // 提交至调度器
        scheduler.schedule(rootWrapper);

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
}
