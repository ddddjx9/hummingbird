package cn.edu.ustb.task.impl;

import cn.edu.ustb.model.transformation.Transformation;

import java.io.Serializable;
import java.util.List;
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

    // 新增方法，用于执行算子
    T executeTransformations(List<Transformation<?>> transformations);
}
