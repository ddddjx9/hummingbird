package cn.edu.ustb.core.env;

import cn.edu.ustb.core.graph.JobGraph;
import cn.edu.ustb.core.graph.JobGraphBuilder;
import cn.edu.ustb.service.dataset.Dataset;
import cn.edu.ustb.service.transformation.Transformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExecutionEnvironment {
    private final int parallelism;

    // 创建Dataset的入口
    public <T> Dataset<T> fromCollection(List<T> data) {
        return Dataset.fromCollection(this, data);
    }

    private final TaskScheduler taskScheduler;
    private final List<Worker> workers = new ArrayList<>();

    public ExecutionEnvironment(int parallelism) {
        DefaultSchedulerFactory factory = new DefaultSchedulerFactory();
        this.taskScheduler = factory.createTaskScheduler();
        this.parallelism = parallelism;
        // 初始化Worker线程池
        for (int i = 0; i < parallelism; i++) {
            workers.add(new Worker());
        }
    }

    public JobGraph createJobGraph(Dataset<?> dataset) {
        JobGraphBuilder builder = new JobGraphBuilder();
        // 遍历transformations构建DAG
        for (Transformation<?, ?> trans : dataset.transformations) {
            builder.addTransformation(trans);
        }
        return builder.build();
    }

    public void execute(JobGraph jobGraph) {
        taskScheduler.submit(jobGraph, workers);
    }
}
