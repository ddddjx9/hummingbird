package cn.edu.ustb.application;

import cn.edu.ustb.component.Driver;
import cn.edu.ustb.task.impl.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        Driver driver = new Driver();

        Task<Integer> rootTask = new Task<>() {
            @Override
            public Integer execute() {
                return 42;
            }

            @Override
            public List<Task<Integer>> getDependencies() {
                return new ArrayList<>();
            }
        };

        // 异步获取结果
        CompletableFuture<Integer> future = driver.getResultFuture(rootTask);
        future.thenAccept(result ->
                System.out.println("Final result: " + result));
    }
}
