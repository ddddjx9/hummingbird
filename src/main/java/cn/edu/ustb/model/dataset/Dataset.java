package cn.edu.ustb.model.dataset;

import cn.edu.ustb.core.task.service.DefaultSchedulerFactory;
import cn.edu.ustb.core.task.service.TaskScheduler;
import cn.edu.ustb.model.function.FilterFunction;
import cn.edu.ustb.model.function.MapFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Dataset<T> {
    // 内部维护的算子链
    private final List<Function<?, ?>> operations = new ArrayList<>();
    private final ArrayList<T> datas;

    public Dataset(ArrayList<T> datas) {
        this.datas = datas;
    }

    // 每次转换操作生成新Dataset并记录算子链
    public <R> Dataset<R> map(Function<Dataset<T>, Dataset<R>> mapper) {
        MapFunction<T, R> mapFunction = (MapFunction<T, R>) mapper;
        operations.add(mapFunction);
        return mapper.apply(this);
    }

    public <R> Dataset<R> filter(Function<Dataset<T>, Dataset<R>> filter) {
        FilterFunction<T, R> filterFunction = (FilterFunction<T, R>) filter;
        operations.add(filterFunction);
        return filter.apply(this);
    }

    // 触发执行入口
    public List<T> collect() {
        // return plan.execute();
        DefaultSchedulerFactory defaultSchedulerFactory = new DefaultSchedulerFactory();
        TaskScheduler taskScheduler = defaultSchedulerFactory.createTaskScheduler();
        taskScheduler.submitStages(this);
        return datas;
    }

    public void forEach(Consumer<Dataset<T>> action) {
        this.collect().forEach(System.out::println);
    }

    public List<Function<?, ?>> getOperations() {
        return operations;
    }
}
