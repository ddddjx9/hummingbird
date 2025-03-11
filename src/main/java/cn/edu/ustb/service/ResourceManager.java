package cn.edu.ustb.service;

import java.util.Comparator;
import java.util.PriorityQueue;

import cn.edu.ustb.task.impl.Task;

public class ResourceManager {
    private final PriorityQueue<Worker> workerQueue;

    private final Driver driver;

    public ResourceManager(Driver driver) {
        this.driver = driver;
        this.workerQueue = new PriorityQueue<>(Comparator.comparingInt(Worker::getFreeSlots));
    }

    public void registerWorker(Worker worker) {
        workerQueue.add(worker);
    }

    public Worker getWorker() {
        return workerQueue.poll();
    }

    public void releaseWorker(Worker worker) {
        workerQueue.remove(worker);
    }

    public <T> void scheduleTask(Task<T> task) {
        Worker worker = getWorker();
        // TODO 调度任务 && 提交任务 && 生成任务图的DAG
    }
}
