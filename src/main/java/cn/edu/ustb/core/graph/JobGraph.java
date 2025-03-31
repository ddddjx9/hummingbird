package cn.edu.ustb.core.graph;

import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.core.task.scheduler.DefaultTaskScheduler;
import cn.edu.ustb.core.task.scheduler.impl.TaskScheduler;
import cn.edu.ustb.service.transformation.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobGraph {
    private final List<Transformation<?, ?>> transformations;
    private final Map<String, List<Task<?, ?>>> tasksByStage = new HashMap<>();
    private final TaskScheduler taskScheduler;

    // 正确接收Transformation列表和任务映射
    public JobGraph(List<Transformation<?, ?>> transformations,
                    Map<String, List<Task<?, ?>>> tasksMap) {
        this.transformations = transformations;
        this.tasksByStage.putAll(tasksMap);
        this.taskScheduler = new DefaultTaskScheduler(
                Runtime.getRuntime().availableProcessors()
        );
    }

    public void execute() {
        // 调度Task到Worker执行
        tasksByStage.values().forEach(tasks ->
                tasks.forEach(task -> taskScheduler.submit(task))
        );
    }

    public <T> T getResult() {
        return null;
    }
}
