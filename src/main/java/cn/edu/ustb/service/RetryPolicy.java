package cn.edu.ustb.service;

import cn.edu.ustb.task.TaskWrapper;

public class RetryPolicy {
    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_BASE = 1000L;

    public boolean shouldRetry(TaskWrapper<?> task) {
        return task.getRetryCount() < MAX_RETRIES;
    }

    public long calculateBackoff(int retryCount) {
        return BACKOFF_BASE * (long) Math.pow(2, retryCount);
    }
}
