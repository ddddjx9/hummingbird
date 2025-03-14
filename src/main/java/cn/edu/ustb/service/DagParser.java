package cn.edu.ustb.service;

import cn.edu.ustb.task.TaskWrapper;

import java.util.ArrayList;
import java.util.List;

public class DagParser {
    public List<TaskWrapper<?>> flattenDependencies(TaskWrapper<?> task) {
        List<TaskWrapper<?>> tasks = new ArrayList<>();
        tasks.add(task);

        for (TaskWrapper<?> dep : task.getDependencies()) {
            TaskWrapper<?> depWrapper = new TaskWrapper<>(dep);
            tasks.addAll(flattenDependencies(depWrapper));
        }
        return tasks;
    }
}
