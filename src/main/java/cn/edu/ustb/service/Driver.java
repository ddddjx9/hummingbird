package cn.edu.ustb.service;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import cn.edu.ustb.task.TaskWrapper;
import cn.edu.ustb.task.impl.Task;

public class Driver {
    private final HummingbirdClient hummingbirdClient;

    private final ResourceManager resourceManager;

    private PriorityQueue<Worker> workerQueue;

    public Driver(HummingbirdClient hummingbirdClient, ResourceManager resourceManager) {
        this.hummingbirdClient = hummingbirdClient;
        this.resourceManager = resourceManager;
        this.workerQueue = new PriorityQueue<>(Comparator.comparingInt(Worker::getFreeSlots));
    }

    public void run() {
        hummingbirdClient.submitTask(new TaskWrapper<>(new Task<Void>() {
            @Override
            public Void execute() {
                return null;
            }

            @Override
            public List<Task<Void>> getDependencies() {
                // 未实现的获取依赖的方法
                throw new UnsupportedOperationException("Unimplemented method 'getDependencies'");
            }
        }));
    }

    public void submitTask(TaskWrapper<Void> taskWrapper) {
        // TODO 提交任务
    }
}
