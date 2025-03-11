package cn.edu.ustb.client;

import cn.edu.ustb.scheduler.Scheduler;
import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

import java.util.concurrent.Future;

public class HummingbirdClient {
    private final Scheduler scheduler;

    public HummingbirdClient(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public <T> Future<T> submitTask(Task<T> task) {
        TaskWrapper<T> taskWrapper = new TaskWrapper<>(task);
        scheduler.submit(taskWrapper);
        return taskWrapper.getFuture();
    }
}
