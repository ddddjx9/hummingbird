package cn.edu.ustb.core.driver;

import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.task.service.TaskScheduler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端提交任务的进程
 */
public class Driver {
    private final TaskScheduler taskScheduler;
    private final Map<String, TaskWrapper<?>> taskRegistry = new ConcurrentHashMap<>();
    private final DAGScheduler dagScheduler;

    public Driver(TaskScheduler taskScheduler, DAGScheduler dagScheduler) {
        this.taskScheduler = taskScheduler;
        this.dagScheduler = dagScheduler;
    }

    /**
     * 接收原始任务并生成可执行DAG
     *
     * @param rootWrapper 用户提交的根任务
     */
    public <T> void submit(TaskWrapper<T> rootWrapper) {
        // 提交至调度器
        taskScheduler.submit(null);
    }

    public CompletableFuture<Integer> getResultFuture(TaskWrapper<Integer> rootTask) {
        // 异步获取任务执行结果
        return null;
    }
}