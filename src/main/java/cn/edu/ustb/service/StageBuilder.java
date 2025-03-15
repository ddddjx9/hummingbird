package cn.edu.ustb.service;

import cn.edu.ustb.model.Dataset;
import cn.edu.ustb.model.dependency.Dependency;
import cn.edu.ustb.model.dependency.ShuffleDependency;
import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.task.impl.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StageBuilder {
    public List<Stage> buildStages(Dataset<?> finalRDD) {
        List<Stage> stages = new ArrayList<>();
        Stack<Dataset<?>> stack = new Stack<>();
        stack.push(finalRDD);

        Stage currentStage = new Stage("stage-0");
        while (!stack.isEmpty()) {
            Dataset<?> dataset = stack.pop();
            for (Dependency<?> dep : dataset.getDependencies()) {
                if (dep instanceof ShuffleDependency) {
                    // 宽依赖切分阶段
                    stages.add(currentStage);
                    currentStage = new Stage("stage-" + stages.size());
                }
                stack.push(dep.getParent());
            }
            currentStage.addTasks(createTasks(dataset));
        }
        stages.add(currentStage);
        return stages;
    }

    public List<Task<?>> createTasks(Dataset<?> dataset) {
        return null;
    }
}