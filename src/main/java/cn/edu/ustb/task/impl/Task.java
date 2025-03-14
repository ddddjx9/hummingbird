package cn.edu.ustb.task.impl;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @param <T> 任务运行类型
 */
public interface Task<T> extends Serializable, Callable<T> {
    
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
