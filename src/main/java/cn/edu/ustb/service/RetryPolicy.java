package cn.edu.ustb.service;

import cn.edu.ustb.component.task.TaskWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetryPolicy {
    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_BASE = 1000L;
    // 新增熔断机制
    private final CircuitBreaker breaker = new CircuitBreaker();

    public boolean shouldRetry(TaskWrapper<?> task) {
        return breaker.allowRetry(task.getTaskId()) &&
                task.getRetryCount() < MAX_RETRIES;
    }

    public long calculateBackoff(int retryCount) {
        return BACKOFF_BASE * (long) Math.pow(2, retryCount);
    }

    static class CircuitBreaker {
        private final Map<String, FailureRecord> failureStats = new ConcurrentHashMap<>();

        boolean allowRetry(String taskId) {
            FailureRecord record = failureStats.getOrDefault(taskId, new FailureRecord());
            return record.consecutiveFailures < 5; // 连续失败5次后熔断
        }

        void recordFailure(String taskId) {
            failureStats.compute(taskId, (k, v) -> {
                if (v == null) v = new FailureRecord();
                v.consecutiveFailures++;
                return v;
            });
        }

        private class FailureRecord {
            int consecutiveFailures = 0;
        }
    }
}
