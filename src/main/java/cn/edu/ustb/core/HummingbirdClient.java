package cn.edu.ustb.core;

import cn.edu.ustb.task.impl.Task;

import java.util.concurrent.CompletableFuture;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        Driver driver = new Driver();
        Scheduler scheduler = new Scheduler(driver.getResourceManager());

        Task<Integer> rootTask = new SampleTask();
        scheduler.submit(rootTask);

        // 异步获取结果
        CompletableFuture<Integer> future = driver.getResultFuture(rootTask);
        future.thenAccept(result ->
                System.out.println("Final result: " + result));
    }
}
