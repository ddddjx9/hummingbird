package cn.edu.ustb.service;

import cn.edu.ustb.task.impl.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定的资源分配管理者
 */
public class ResourceManager {
    // 拥有的工作器数量
    private final List<Worker> workers = new ArrayList<>();

    /**
     * 注册工作器方法
     * @param worker 为指定的工作器进行注册
     */
    public void registerWorker(Worker worker) {
        workers.add(worker);
    }

    /**
     * 获取特定的工作器
     * @return 返回可用的工作器
     */
    public Worker getWorker() {
        return workers.stream().filter(worker -> worker.getFreeSlots() > 0).toList().get(0);
    }

    /**
     * 释放任务执行完毕的工作器
     */
    public void releaseWorker() {
        workers.removeIf(worker -> worker.getFreeSlots() == 0);
    }

    /**
     * 规划task的执行顺序和所用的资源
     * @param task 所给定的task
     * @param <T> task所能够返回的结果
     */
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
