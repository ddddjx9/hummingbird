package cn.edu.ustb.service;

import cn.edu.ustb.task.impl.Task;

/**
 * 任务的调度器
 */
public class Scheduler {
    // 全局的资源管理者
    private final ResourceManager resourceManager;

    /**
     * 对用户提交的任务进行调度
     * @param resourceManager 全局的资源管理者
     */
    public Scheduler(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * 调度完毕任务后对任务进行提交
     * @param task 用户提交的任务
     * @param <T> 用户提交的任务所能够返回的结果
     */
    public <T> void submit(Task<T> task) {
        resourceManager.scheduleTask(task);
    }
}
