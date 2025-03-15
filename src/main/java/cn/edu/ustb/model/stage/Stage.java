package cn.edu.ustb.model.stage;

import cn.edu.ustb.task.impl.Task;

import java.util.List;

public class Stage {
    private String stageName;

    public Stage(String stageName) {
        this.stageName = stageName;
    }

    public void addTasks(List<Task<?>> tasks) {

    }

    public List<Task<?>> getTasks() {
        return null;
    }
}
