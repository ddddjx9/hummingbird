package cn.edu.ustb.core.task.service;

import cn.edu.ustb.core.resourceManager.worker.ExecutorManager;
import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.task.service.impl.TaskSchedulerFactory;

public class DefaultSchedulerFactory implements TaskSchedulerFactory {
    @Override
    public TaskScheduler createTaskScheduler(ResourceManager rm) {
        return new TaskScheduler(
                new DAGScheduler(),
                rm,
                ExecutorManager.getInstance()
        );
    }
}
