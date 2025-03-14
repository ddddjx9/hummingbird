package cn.edu.ustb.service;

import cn.edu.ustb.core.ResourceManager;
import cn.edu.ustb.core.Scheduler;
import cn.edu.ustb.core.Worker;
import cn.edu.ustb.core.Worker;
import cn.edu.ustb.task.TaskWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DAGExecutor {
    private final Map<TaskWrapper<?>, List<TaskWrapper<?>>> dag = new ConcurrentHashMap<>();
    private final ResourceManager rm;
    private final ExecutorManager executor = new ExecutorManager();

    public DAGExecutor(Scheduler scheduler) {
        this.rm = scheduler.getResourceManager();
    }

    public void schedule(TaskWrapper<?> task) {
        // 构建DAG
        buildDag(task);

        // 拓扑排序后提交
        List<TaskWrapper<?>> orderedTasks = topologicalSort();
        orderedTasks.forEach(t -> executor.submit(() -> {
            try {
                Worker worker = rm.allocateWorker(1, TimeUnit.MINUTES);
                if (worker != null) {
                    worker.submitTask(t);
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }));
    }

    private void buildDag(TaskWrapper<?> task) {
        if (dag.containsKey(task)) {
            return;
        }
        dag.put(task, task.getDependencies());
        task.getDependencies().forEach(this::buildDag);
    }

    // 基于Kahn算法的拓扑排序
    private List<TaskWrapper<?>> topologicalSort() {
        List<TaskWrapper<?>> result = new ArrayList<>();
        Map<TaskWrapper<?>, Integer> inDegree = new HashMap<>();
        Queue<TaskWrapper<?>> queue = new LinkedList<>();

        // 计算入度
        dag.forEach((task, deps) -> {
            inDegree.putIfAbsent(task, 0);
            deps.forEach(dep -> inDegree.merge(dep, 1, Integer::sum));
        });

        // 将入度为0的节点加入队列
        inDegree.forEach((task, degree) -> {
            if (degree == 0) {
                queue.offer(task);
            }
        });

        // 执行拓扑排序
        while (!queue.isEmpty()) {
            TaskWrapper<?> task = queue.poll();
            result.add(task);

            // 更新相邻节点的入度
            dag.get(task).forEach(dep -> {
                int newDegree = inDegree.get(dep) - 1;
                inDegree.put(dep, newDegree);
                if (newDegree == 0) {
                    queue.offer(dep);
                }
            });
        }

        if (result.size() != dag.size()) {
            throw new IllegalStateException("DAG contains cycle");
        }

        return result;
    }

    public <T> List<TaskWrapper<?>> flattenDependencies(TaskWrapper<T> rootWrapper) {
        return null;
    }
}
