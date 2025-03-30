package cn.edu.ustb.model.dataset;

import cn.edu.ustb.core.env.ExecutionEnvironment;
import cn.edu.ustb.core.dag.JobGraph;
import cn.edu.ustb.model.function.Function;

import java.util.ArrayList;
import java.util.List;

public class Dataset<T> {
    public final List<Transformation<?, ?>> transformations = new ArrayList<>();
    final ExecutionEnvironment env;

    // 用户入口：从集合创建Dataset
    public static <T> Dataset<T> fromCollection(ExecutionEnvironment env, List<T> data) {
        return new Dataset<>(env, new CollectionSourceTransformation<>(data));
    }

    Dataset(ExecutionEnvironment env, Transformation<?, T> source) {
        this.env = env;
        this.transformations.add(source);
    }

    // 转换操作
    public <R> Dataset<R> map(Function<T, R> mapper) {
        MapTransformation<T, R> mapTrans = new MapTransformation<>("Map", mapper);
        transformations.add(mapTrans);
        return new Dataset<>(env, mapTrans);
    }

    // 触发执行
    public List<T> collect() {
        JobGraph jobGraph = env.createJobGraph(this);
        env.execute(jobGraph);
        return jobGraph.getResult();
    }

    // 添加Filter转换
    public Dataset<T> filter(Function<T, Boolean> predicate) {
        FilterTransformation<T> filterTrans = new FilterTransformation<>(predicate);
        transformations.add(filterTrans);
        return new Dataset<>(env, filterTrans);
    }
}
