package cn.edu.ustb.core.driver;

import cn.edu.ustb.core.task.scheduler.impl.TaskScheduler;
import cn.edu.ustb.service.dataset.Dataset;

/**
 * 客户端提交任务的进程
 */
public class Driver {
    private final TaskScheduler taskScheduler;

    public Driver(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * 接收原始任务并生成可执行DAG
     *
     * @param dataset 用于计算的数据集
     */
    public <T> void submit(Dataset<T> dataset) {

    }
}