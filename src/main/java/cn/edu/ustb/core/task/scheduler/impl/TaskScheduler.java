package cn.edu.ustb.core.task.scheduler.impl;

import cn.edu.ustb.core.task.Task;

public interface TaskScheduler {
    <IN,OUT> void submit(Task<IN, OUT> task);
    void shutdown();
}