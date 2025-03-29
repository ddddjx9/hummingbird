package cn.edu.ustb.core.task.service;

import cn.edu.ustb.core.task.service.impl.TaskSchedulerFactory;

public class DefaultSchedulerFactory implements TaskSchedulerFactory {
    @Override
    public TaskScheduler createTaskScheduler() {
        return new TaskScheduler();
    }
}
