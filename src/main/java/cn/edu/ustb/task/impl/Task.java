package cn.edu.ustb.task.impl;

import java.util.UUID;

public interface Task<T> {
    T execute();

    default String getTaskId() {
        return UUID.randomUUID().toString();
    }
}
