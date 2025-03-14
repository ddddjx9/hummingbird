package cn.edu.ustb.service;

import cn.edu.ustb.task.TaskWrapper;

import java.util.List;

/**
 * 客户端提交任务的进程
 */
public class Driver {

    /**
     * 向rm提交任务
     * @param taskWrapper task包装之后的类
     * @param <T> 任务类型
     */
    public <T> void submitTask(List<TaskWrapper<T>> taskWrapper) {
        // TODO 提交任务
        //  之后可以向客户端汇报任务是否提交成功之类的信息
    }

    /**
     * 划分任务
     * @param taskWrapper 给定的包装任务
     * @return 返回划分好的任务
     * @param <T> 任务类型
     */
    public <T> List<TaskWrapper<T>> splitTasks(TaskWrapper<Void> taskWrapper) {
        // TODO 这里对用户提交的任务进行切分
        return null;
    }
}
