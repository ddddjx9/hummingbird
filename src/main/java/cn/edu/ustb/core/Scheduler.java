package cn.edu.ustb.core;

import cn.edu.ustb.service.RetryPolicy;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 任务的调度器
 */
public class Scheduler {
    private final ResourceManager rm;
    private final DAGExecutor dagExecutor = new DAGExecutor();

    public Scheduler(ResourceManager rm) {
        this.rm = rm;
    }

    public void submit(Task<?> rootTask) {
        TaskWrapper<?> rootWrapper = new TaskWrapper<>((TaskWrapper<?>) rootTask);
        dagExecutor.schedule(rootWrapper);
    }

    private class DAGExecutor {
        private final Map<TaskWrapper<?>, List<TaskWrapper<?>>> dag = new ConcurrentHashMap<>();
        private final ExecutorService executor = Executors.newCachedThreadPool();

        public void schedule(TaskWrapper<?> task) {
            // 构建DAG
            dag.put(task, task.getDependencies());

            // 拓扑排序后提交
            List<TaskWrapper<?>> orderedTasks = topologicalSort();
            orderedTasks.forEach(t -> executor.submit(() -> {
                try {
                    Worker worker = rm.allocateWorker(1, TimeUnit.MINUTES);
                    worker.submitTask(t);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        // 基于Kahn算法的拓扑排序
        private List<TaskWrapper<?>> topologicalSort() {
            // 处理入度表
            return new ArrayList<>();
        }
    }

    // 在Scheduler中实现重试提交
    public void resubmit(TaskWrapper<?> task) {
        RetryPolicy retryPolicy = new RetryPolicy();
        if (retryPolicy.shouldRetry(task)) {
//            long delay = retryPolicy.calculateBackoff(task.getRetryCount());
//            scheduledExecutor.schedule(() -> submit(task), delay, TimeUnit.MILLISECONDS);
//            task.incrementRetryCount();
        }
    }
}
