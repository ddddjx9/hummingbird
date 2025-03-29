package cn.edu.ustb.core.dag;

import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.resourceManager.worker.ExecutorManager;
import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.model.dataset.Dataset;
import cn.edu.ustb.model.function.FilterFunction;
import cn.edu.ustb.model.function.MapFunction;
import cn.edu.ustb.model.stage.Stage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DAGScheduler {
    // 维护Stage依赖关系 (父Stage -> 子Stage)
    private final Map<Stage, List<Stage>> stageDependencies = new ConcurrentHashMap<>();
    // Stage到Task的映射 (Stage -> 该Stage的所有Task)
    private final Map<Stage, List<Task<?, ?>>> stageTasks = new ConcurrentHashMap<>();
    private final ResourceManager rm = ResourceManager.getInstance();
    private final ExecutorManager executor = ExecutorManager.getInstance();

    public <T> List<Stage> schedule(Dataset<T> dataset) {
        // 解析算子链，构建Stage
        List<Stage> stages = buildStages(dataset.getOperations());

        // 构建Stage依赖关系
        buildStageDependencies(stages);

        // 拓扑排序并提交Stage
        return topologicalSort(stages);
    }

    /**
     * 将算子链划分为Stage（基于宽窄依赖）
     */
    private List<Stage> buildStages(List<Function<?, ?>> operations) {
        List<Stage> stages = new ArrayList<>();
        int stageIndex = 0;
        Stage currentStage = new Stage(String.valueOf(stageIndex++));

        for (Function<?, ?> op : operations) {
            // 检查是否为宽依赖操作
            if (isWideDependency(op)) {
                stages.add(currentStage);
                currentStage = new Stage(String.valueOf(stageIndex++));
            }
            currentStage.addOperation(op);
        }

        stages.add(currentStage);
        return stages;
    }

    /**
     * 判断是否为宽依赖
     */
    private boolean isWideDependency(Function<?, ?> operation) {
        if (operation instanceof FilterFunction) {
            return "WIDE".equals(((FilterFunction<?, ?>) operation).getOperationName());
        } else if (operation instanceof MapFunction) {
            return "WIDE".equals(((MapFunction<?, ?>) operation).getOperationName());
        }
        return false;
    }

    /**
     * 构建Stage依赖（链式顺序依赖）
     */
    private void buildStageDependencies(List<Stage> stages) {
        for (int i = 0; i < stages.size() - 1; i++) {
            Stage parent = stages.get(i);
            Stage child = stages.get(i + 1);
            stageDependencies.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        }
    }

    /**
     * 拓扑排序Stage
     */
    public List<Stage> topologicalSort(List<Stage> stages) {
        Map<Stage, Integer> inDegree = new HashMap<>();
        Queue<Stage> queue = new LinkedList<>();

        // 初始化入度
        stages.forEach(stage -> inDegree.put(stage, 0));
        stageDependencies.forEach((parent, children) ->
                children.forEach(child -> inDegree.merge(child, 1, Integer::sum))
        );

        // 入队入度为0的Stage
        inDegree.forEach((stage, degree) -> {
            if (degree == 0) queue.add(stage);
        });

        // 执行拓扑排序
        List<Stage> orderedStages = new ArrayList<>();
        while (!queue.isEmpty()) {
            Stage stage = queue.poll();
            orderedStages.add(stage);

            stageDependencies.getOrDefault(stage, Collections.emptyList())
                    .forEach(child -> {
                        int newDegree = inDegree.get(child) - 1;
                        inDegree.put(child, newDegree);
                        if (newDegree == 0) queue.add(child);
                    });
        }

        return orderedStages;
    }
}
