package cn.edu.ustb.application;

import cn.edu.ustb.component.DefaultSchedulerFactory;
import cn.edu.ustb.component.Driver;
import cn.edu.ustb.component.ResourceManager;
import cn.edu.ustb.component.TaskScheduler;
import cn.edu.ustb.model.transformation.Transformation;
import cn.edu.ustb.component.DAGScheduler;
import cn.edu.ustb.component.task.impl.Task;

import java.util.List;
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

            @Override
            public Integer executeTransformations(List<Transformation<?>> transformations) {
                return 0;
            }
        };

        driver.submit(rootTask);
        // 异步获取结果
        CompletableFuture<Integer> future = driver.getResultFuture(rootTask);
        future.thenAccept(result ->
                System.out.println("Final result: " + result));
    }
}
