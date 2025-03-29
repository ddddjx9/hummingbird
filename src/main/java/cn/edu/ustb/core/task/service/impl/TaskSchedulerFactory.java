package cn.edu.ustb.core.task.service.impl;

import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.task.service.TaskScheduler;

public interface TaskSchedulerFactory {
    TaskScheduler createTaskScheduler(ResourceManager rm);
}
