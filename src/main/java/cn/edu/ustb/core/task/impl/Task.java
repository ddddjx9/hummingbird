package cn.edu.ustb.core.task.impl;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @param <T> 任务运行类型
 */
public interface Task<T> extends Serializable, Callable<T> {

    T execute();

    default String getTaskId() {
        return UUID.randomUUID().toString();
    }
}
