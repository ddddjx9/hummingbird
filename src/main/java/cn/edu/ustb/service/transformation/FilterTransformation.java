package cn.edu.ustb.service.transformation;

import cn.edu.ustb.service.dataset.Dataset;
import cn.edu.ustb.service.function.Function;

public class FilterTransformation<T> extends Transformation<T, T> {
    private final Function<T, Boolean> predicate;

    public FilterTransformation(Function<T, Boolean> predicate) {
        super("Filter");
        this.predicate = predicate;
    }

    @Override
    public Dataset<T> apply(Dataset<T> input) {
        return null;
    }
}
