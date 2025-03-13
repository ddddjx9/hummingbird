package cn.edu.ustb.service;

import cn.edu.ustb.task.impl.Task;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private final List<Worker> workers = new ArrayList<>();

    public void registerWorker(Worker worker) {
        workers.add(worker);
    }

    public Worker getWorker() {
        return workers.stream().filter(worker -> worker.getFreeSlots() > 0).toList().get(0);
    }

    public void releaseWorker() {
        workers.removeIf(worker -> worker.getFreeSlots() == 0);
    }

    public <T> void scheduleTask(Task<T> task) {
        Worker worker = getWorker();
        // TODO 调度任务 && 提交任务 && 生成任务图的DAG
    }

    /**
     * 分配需要的slots
     * @param numSlots 需要的slots的数量
     * @return 返回可用的slots的数量
     */
    public List<Slot> allocateSlots(int numSlots) {
        return null;
    }
}
