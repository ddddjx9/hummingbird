package cn.edu.ustb.core.task.service;

import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.resourceManager.worker.ExecutorManager;
import cn.edu.ustb.core.resourceManager.worker.Worker;
import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.enums.TaskStatus;
import cn.edu.ustb.model.dataset.Dataset;
import cn.edu.ustb.model.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 任务的调度器
 */
public class TaskScheduler {
    private final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
    private final DAGScheduler dagScheduler = new DAGScheduler();
    private final ResourceManager resourceManager = ResourceManager.getInstance();
    private final ExecutorManager executorManager = ExecutorManager.getInstance();
    // 维护正在运行的任务
    private final Map<String, Task<?, ?>> runningTasks = new ConcurrentHashMap<>();
    // 重试队列
    private final PriorityBlockingQueue<Task<?, ?>> retryQueue =
            new PriorityBlockingQueue<>(10, Comparator.comparingInt(Task::getPriority));

    protected TaskScheduler() {

    }

    /**
     * 提交任务列表到调度器
     */
    public <T> void submitStages(Dataset<T> dataset) {
        List<Stage> orderedStages = dagScheduler.schedule(dataset);
        orderedStages.forEach(stage -> {
            List<Task<?, ?>> tasks = createTasks();
            tasks.forEach(this::scheduleTask);
        });
    }

    /**
     * 创建分片任务（动态分片策略）
     */
    private List<Task<?, ?>> createTasks() {
        int numPartitions = calculateOptimalPartitions();
        return IntStream.range(0, numPartitions)
                .mapToObj(partitionId -> new Task<>(
                        null, null
                ))
                .collect(Collectors.toList());
    }

    /**
     * 动态计算分片数量（基于数据量和集群资源）
     */
    private int calculateOptimalPartitions() {
        int defaultPartitions = Runtime.getRuntime().availableProcessors() * 2;
        return Math.max(defaultPartitions, resourceManager.getAvailableWorkers());
    }

    /**
     * 提交单个任务到资源管理器
     */
    private void scheduleTask(Task<?, ?> task) {
        executorManager.submit(() -> {
            try {
                Worker worker = resourceManager.allocateWorker(1, TimeUnit.MINUTES);
                if (worker != null) {
                    worker.submitTask(task);
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }
}
