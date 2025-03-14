package cn.edu.ustb.application;

import cn.edu.ustb.component.Driver;
import cn.edu.ustb.task.impl.Task;

import java.util.concurrent.CompletableFuture;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        Driver driver = new Driver();

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

        driver.submit(rootTask);
        // 异步获取结果
        CompletableFuture<Integer> future = driver.getResultFuture(rootTask);
        future.thenAccept(result ->
                System.out.println("Final result: " + result));
    }
}
