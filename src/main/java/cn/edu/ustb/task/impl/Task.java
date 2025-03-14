package cn.edu.ustb.task.impl;

import java.util.List;
import java.util.UUID;

/**
 * 实现任务的接口，用于向客户端提交任务
 * @param <T>
 */
public interface Task<T> {
    /**
     * 获取任务的依赖
     * @return 依赖
     */
    List<Task<T>> getDependencies();

    /**
     * 执行任务
     * @return 任务结果
     */
    T execute();

    /**
     * 获取任务的ID
     * @return 任务ID
     */
    default String getTaskId() {
        return UUID.randomUUID().toString();
    }
}
