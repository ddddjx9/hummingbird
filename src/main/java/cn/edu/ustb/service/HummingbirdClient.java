package cn.edu.ustb.service;

import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

import java.util.List;

public class HummingbirdClient {
    public static void main(String[] args) {
        Driver driver = new Driver();
        List<TaskWrapper<Object>> tasks = driver.splitTasks(new TaskWrapper<>(new Task<Void>() {
            @Override
            public List<Task<Void>> getDependencies() {
                return List.of();
            }

            @Override
            public Void execute() {
                return null;
            }
        }));

        driver.submitTask(tasks);
    }
}
