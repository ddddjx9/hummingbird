package cn.edu.ustb.model.transformation;

import cn.edu.ustb.model.dataset.Dataset;
import cn.edu.ustb.model.transformation.function.FilterFunction;
import cn.edu.ustb.model.transformation.function.MapFunction;

public class FilterTransformation<T,R> extends Transformation<T, R> {
    private final FilterFunction<T, R> filter;

    public FilterTransformation(FilterFunction<T, R> filter) {
        this.filter = filter;
    }

    @Override
    public Dataset<T> buildDataset() {
        return null;
    }

    @Override
    public R apply(T input) {
        return filter.apply(input);
    }
}
