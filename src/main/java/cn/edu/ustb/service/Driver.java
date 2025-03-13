package cn.edu.ustb.service;

import cn.edu.ustb.task.TaskWrapper;

import java.util.List;

public class Driver {

    public <T> void submitTask(List<TaskWrapper<T>> taskWrapper) {
        // TODO 提交任务
        //  之后可以向客户端汇报任务是否提交成功之类的信息
    }

    public <T> List<TaskWrapper<T>> splitTasks(TaskWrapper<Void> taskWrapper) {
        // TODO 这里对用户提交的任务进行切分
        return null;
    }
}
