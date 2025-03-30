package cn.edu.ustb.core.task.service.impl;

import cn.edu.ustb.core.task.Task;

public interface TaskScheduler {
    void submit(Task<?, ?> task);
    void shutdown();
}