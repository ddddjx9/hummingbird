package cn.edu.ustb.task;

import cn.edu.ustb.task.impl.Task;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 任务的包装类
 *
 * @param <T> 执行的任务类型
 */
public class TaskWrapper<T> implements Task<T>, Future<T> {

    // 任务执行过程中的依赖
    private final List<Task<T>> dependencies;

    // 初始化任务的包装类
    public TaskWrapper(Task<T> task) {
        this.dependencies = task.getDependencies();
    }

    /**
     * 返回是否要打断进程的布尔值
     *
     * @param mayInterruptIfRunning {@code true} if the thread
     *                              executing this task should be interrupted (if the thread is
     *                              known to the implementation); otherwise, in-progress tasks are
     *                              allowed to complete
     * @return 返回是否要打断进程的布尔值
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return mayInterruptIfRunning;
    }


    /**
     * 返回任务是否取消
     * @return 返回任务是否已经取消
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * 返回任务是否已经执行成功
     * @return 返回任务是否执行成功
     */
    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * 返回任务的执行结果
     * @return 返回任务执行结果
     */
    @Override
    public T get() {
        return null;
    }

    /**
     * 返回任务执行结果
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return 返回任务执行结果
     */
    @Override
    public T get(long timeout, @Nullable TimeUnit unit) {
        return null;
    }

    /**
     * 任务执行方法
     * @return 返回任务执行结果
     */
    @Override
    public T execute() {
        return null;
    }

    /**
     * 获取任务的依赖
     * @return 返回该任务依赖的上游任务
     */
    @Override
    public List<Task<T>> getDependencies() {
        // 未实现的获取任务依赖的方法
        return dependencies;
    }
}
