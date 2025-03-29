package cn.edu.ustb.component;

import cn.edu.ustb.component.impl.TaskSchedulerFactory;

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
