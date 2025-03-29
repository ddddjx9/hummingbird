package cn.edu.ustb.component;

import cn.edu.ustb.model.TransformationStack;
import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.model.transformation.Transformation;
import cn.edu.ustb.component.task.TaskWrapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DAGScheduler {
    private final Map<TaskWrapper<?>, List<TaskWrapper<?>>> dag = new ConcurrentHashMap<>();
    private final ResourceManager rm;
    private final ExecutorManager executor = ExecutorManager.getInstance();

    public DAGScheduler() {
        this.rm = ResourceManager.getInstance();
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

    private  <T> List<TaskWrapper<?>> flattenDependencies(TaskWrapper<T> rootWrapper) {
        // 先构建DAG
        buildDag(rootWrapper);
        
        // 从DAG中获取所有任务
        Set<TaskWrapper<?>> allTasks = new HashSet<>();
        dag.forEach((task, deps) -> {
            allTasks.add(task);
            allTasks.addAll(deps);
        });
        
        return new ArrayList<>(allTasks);
    }

    private List<Stage> buildStages(Transformation<?> finalTransformation) {
        TransformationStack stack = new TransformationStack();
        Queue<Transformation<?>> queue = new LinkedList<>();
        Set<Transformation<?>> visited = new HashSet<>();

        // 广度优先遍历算子链
        queue.add(finalTransformation);
        while (!queue.isEmpty()) {
            Transformation<?> current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            // 压栈处理当前算子
            stack.pushTransformation(current);

            // 将上游算子加入队列
            for (Transformation<?> parent : current.getInputs()) {
                queue.add(parent);
            }
        }

        // 返回阶段列表
        List<Stage> stages = stack.buildStages();
        Collections.reverse(stages);
        return stages;
    }

    // 增强依赖解析
    private void buildDag(TaskWrapper<?> task) {
        if (dag.containsKey(task)) return;

        // 获取完整依赖链（包括间接依赖）
        List<TaskWrapper<?>> allDependencies = new ArrayList<>();
        task.getDependencies().forEach(dep -> {
            allDependencies.add(dep);
            buildDag(dep); // 递归构建
            allDependencies.addAll(dag.get(dep)); // 添加间接依赖
        });

        dag.put(task, allDependencies.stream().distinct().collect(Collectors.toList()));
    }
}
