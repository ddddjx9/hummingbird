package cn.edu.ustb.task;

import cn.edu.ustb.task.impl.Task;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskWrapper<T> implements Task<T>,Future<T> {
    private Task<T> task;
    
    private final List<Task<T>> dependencies;

    public TaskWrapper(Task<T> task) {
        this.task = task;
        this.dependencies = task.getDependencies();
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

    public Future<T> getFuture() {
        return this;
    }

    @Override
    public List<Task<T>> getDependencies() {
        // 未实现的获取任务依赖的方法
        return dependencies;
    }
}
