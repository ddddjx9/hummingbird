package cn.edu.ustb.core.task.scheduler;

import cn.edu.ustb.core.task.Task;
import cn.edu.ustb.core.task.scheduler.impl.TaskScheduler;
import cn.edu.ustb.service.transformation.CollectionSourceTransformation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultTaskScheduler implements TaskScheduler {
    private final ExecutorService executor;

    public DefaultTaskScheduler(int parallelism) {
        this.executor = Executors.newFixedThreadPool(parallelism);
    }

    @Override
    public <IN,OUT> void submit(Task<IN,OUT> task) {
        executor.submit(() -> {
            // 启动数据源
            if (task.getTransformation() instanceof CollectionSourceTransformation) {
                ((CollectionSourceTransformation<IN>) task.getTransformation())
                        .runSource((BlockingQueue<IN>) task.getInputChannel());
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