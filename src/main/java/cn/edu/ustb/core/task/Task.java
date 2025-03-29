package cn.edu.ustb.core.task;

import cn.edu.ustb.model.dataset.Dataset;

import java.io.Serializable;
import java.util.function.Function;

public class Task<T, R> implements Serializable {
    private Dataset<T> dataset;
    private String taskId;

    private final Function<Dataset<T>, Dataset<R>> operation;

    public Task(Function<?, ?> operation,Dataset<T> dataset) {
        this.operation = (Function<Dataset<T>, Dataset<R>>) operation;
        this.dataset = dataset;
    }

    public Dataset<R> execute() {
        return operation.apply(dataset);
    }

    public Dataset<T> getDataset() {
        return new Dataset<T>(null);
    }

    public String getTaskId() {
        return taskId;
    }

    public int getPriority() {
        return 10;
    }
}
