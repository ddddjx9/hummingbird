package cn.edu.ustb.application;

import cn.edu.ustb.core.driver.Driver;
import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.resourceManager.ResourceManager;
import cn.edu.ustb.core.task.TaskWrapper;
import cn.edu.ustb.core.task.impl.Task;
import cn.edu.ustb.core.task.service.DefaultSchedulerFactory;
import cn.edu.ustb.core.task.service.TaskScheduler;

import java.util.concurrent.CompletableFuture;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        DefaultSchedulerFactory factory = new DefaultSchedulerFactory();
        TaskScheduler taskScheduler = factory.createTaskScheduler(ResourceManager.getInstance());
        DAGScheduler dagScheduler = new DAGScheduler();
        Driver driver = new Driver(taskScheduler, dagScheduler);

        Task<Integer> rootTask = new Task<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }

            @Override
            public Integer execute() {
                return 42;
            }
        };

        TaskWrapper<Integer, Integer> rootWrapper = new TaskWrapper<>(rootTask, "111");
        driver.submit(rootWrapper);
        // 异步获取结果
        CompletableFuture<Integer> future = driver.getResultFuture(rootWrapper);
        future.thenAccept(result ->
                System.out.println("Final result: " + result));
    }
}
