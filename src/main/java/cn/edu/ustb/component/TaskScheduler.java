package cn.edu.ustb.component;

import cn.edu.ustb.model.stage.Stage;
import cn.edu.ustb.component.task.TaskWrapper;
import cn.edu.ustb.component.task.impl.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务的调度器
 */
public class TaskScheduler {
    private final DAGScheduler dagScheduler = new DAGScheduler();

    protected TaskScheduler(DAGScheduler dagScheduler, ResourceManager rm,ExecutorManager executorManager) {

    }

    /**
     * 提交任务列表到调度器
     *
     * @param stages 要提交的任务列表
     */
    public void submitStages(List<Stage> stages) {
        for (Stage stage : stages) {
            List<Task> tasks = createTasks(stage);
            scheduleTasks(tasks);
        }
    }

    private List<Task> createTasks(Stage stage) {
        List<Task> tasks = new ArrayList<>();
        for (int partitionId = 0; partitionId < stage.getNumPartitions(); partitionId++) {
            Task task = new TaskWrapper(
                    stage.getId(),
                    partitionId,
                    stage.getTransformations(), // 传递该阶段的所有算子
                    stage.getDependencies()
            );
            tasks.add(task);
        }
        return tasks;
    }

    private void scheduleTasks(List<Task> tasks) {
        for (Task task : tasks) {
            Worker worker = ResourceManager.assignWorker();
            worker.submitTask(task);
        }
    }
}
