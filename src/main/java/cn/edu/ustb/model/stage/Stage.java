package cn.edu.ustb.model.stage;

import cn.edu.ustb.core.task.TaskWrapper;
import cn.edu.ustb.model.transformation.Transformation;

import java.util.List;

public class Stage {
    private final String stageName;


    public Stage(String stageName) {
        this.stageName = stageName;
    }

    public String getStageId() {
        return stageName;
    }

    public <T,R> List<Transformation<T,R>> getTransformations() {
        return null;
    }

    public int getNumPartitions() {
        return 2;
    }

    public <T,R> List<TaskWrapper<T,R>> getDependencies() {
        return null;
    }
}
