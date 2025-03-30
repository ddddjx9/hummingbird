package cn.edu.ustb.core.task.service;

import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.core.task.service.impl.TaskScheduler;
import cn.edu.ustb.model.dataset.CollectionSourceTransformation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultTaskScheduler implements TaskScheduler {
    private final ExecutorService executor;

    public DefaultTaskScheduler(int parallelism) {
        this.executor = Executors.newFixedThreadPool(parallelism);
    }

    @Override
    public void submit(Task<?, ?> task) {
        executor.submit(() -> {
            // 启动数据源
            if (task.getTransformation() instanceof CollectionSourceTransformation) {
                ((CollectionSourceTransformation<?>) task.getTransformation())
                        .runSource(task.getInputChannel());
            }
            // 执行Task
            task.run();
        });
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}