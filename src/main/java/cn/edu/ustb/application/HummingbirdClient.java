package cn.edu.ustb.application;

import cn.edu.ustb.core.driver.Driver;

/**
 * 用于用户的自定义任务提交，提交给driver
 */
public class HummingbirdClient {
    public static void main(String[] args) {
        DefaultSchedulerFactory factory = new DefaultSchedulerFactory();
        TaskScheduler taskScheduler = factory.createTaskScheduler();
        Driver driver = new Driver(taskScheduler);
    }
}
