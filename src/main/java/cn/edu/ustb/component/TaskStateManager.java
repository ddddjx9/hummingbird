package cn.edu.ustb.component;

import cn.edu.ustb.enums.TaskStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskStateManager {
    private final Map<String, TaskState> stateStore = new ConcurrentHashMap<>();
    private static volatile TaskStateManager instance;

    private TaskStateManager() {

    }

    // 双重检查锁定实现单例
    public static TaskStateManager getInstance() {
        if (instance == null) {
            synchronized (ResourceManager.class) {
                if (instance == null) {
                    instance = new TaskStateManager();
                }
            }
        }
        return instance;
    }

    public void updateState(String taskId, TaskStatus status) {
        stateStore.compute(taskId, (k, v) -> {
            if (v == null) v = new TaskState(taskId);
            v.setStatus(status);
            return v;
        });
    }

    public TaskState getState(String taskId) {
        return stateStore.get(taskId);
    }

    public static class TaskState {
        private final String taskId;
        private volatile TaskStatus status;
        private final AtomicInteger retryCount = new AtomicInteger(0);

        TaskState(String taskId) {
            this.taskId = taskId;
        }

        public void setStatus(TaskStatus status) {

        }

        public TaskStatus getStatus() {
            return status;
        }
    }
}
