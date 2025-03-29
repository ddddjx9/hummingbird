package cn.edu.ustb.component.impl;

import cn.edu.ustb.component.ResourceManager;
import cn.edu.ustb.component.TaskScheduler;

public interface TaskSchedulerFactory {
    public TaskScheduler createTaskScheduler(ResourceManager rm);
}
