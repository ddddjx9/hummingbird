package cn.edu.ustb.service;

import cn.edu.ustb.task.impl.Task;

public class Scheduler {
    private final ResourceManager resourceManager;

    public Scheduler(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }   

    public <T> void submit(Task<T> task) {
        resourceManager.scheduleTask(task);
    }
}
