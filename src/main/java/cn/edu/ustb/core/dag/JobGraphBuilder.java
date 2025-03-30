package cn.edu.ustb.core.dag;

import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.model.dataset.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JobGraphBuilder {
    private final Map<Transformation<?, ?>, List<Task<?, ?>>> transformationTasks = new HashMap<>();

    public void addTransformation(Transformation<?, ?> trans) {
        // 为每个Transformation生成并行Task
        List<Task<?, ?>> tasks = IntStream.range(0, trans.getParallelism())
                .mapToObj(i -> new Task<>(trans, i))
                .collect(Collectors.toList());

        transformationTasks.put(trans, tasks);

        // 建立Task间数据通道
        for (Transformation<?, ?> input : trans.getInputs()) {
            List<Task<?, ?>> upstreamTasks = transformationTasks.get(input);
            tasks.forEach(task ->
                    upstreamTasks.forEach(upstream ->
                            upstream.addOutputChannel(task.getInputChannel())
                    )
            );
        }
    }

    public JobGraph build() {
        Map<String, List<Task<?, ?>>> tasksMap = new HashMap<>();
        transformationTasks.forEach((trans, tasks) ->
                tasksMap.put(trans.getId(), tasks)
        );
        return new JobGraph(new ArrayList<>(transformationTasks.keySet()), tasksMap);
    }
}