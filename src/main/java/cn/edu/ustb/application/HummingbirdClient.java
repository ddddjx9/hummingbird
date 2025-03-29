package cn.edu.ustb.application;

import cn.edu.ustb.core.dag.DAGScheduler;
import cn.edu.ustb.core.driver.Driver;
import cn.edu.ustb.core.task.service.DefaultSchedulerFactory;
import cn.edu.ustb.core.task.service.TaskScheduler;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        DefaultSchedulerFactory factory = new DefaultSchedulerFactory();
        TaskScheduler taskScheduler = factory.createTaskScheduler();
        DAGScheduler dagScheduler = new DAGScheduler();
        Driver driver = new Driver(taskScheduler, dagScheduler);
    }
}
