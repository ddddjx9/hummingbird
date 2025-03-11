package cn.edu.ustb.task;

import cn.edu.ustb.task.impl.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskWrapper<T> implements Task<T>,Future<T> {
    private Task<T> task;

    public TaskWrapper(Task<T> task) {
        this.task = task;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return mayInterruptIfRunning;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public T execute() {
        return null;
    }

    public <T> Future<T> getFuture() {
        return this;
    }
}
