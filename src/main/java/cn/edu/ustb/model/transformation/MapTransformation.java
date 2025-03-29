package cn.edu.ustb.model.transformation;

import cn.edu.ustb.model.dataset.Dataset;
import cn.edu.ustb.model.dataset.MappedDataset;
import cn.edu.ustb.model.transformation.function.MapFunction;

public class MapTransformation<T, R> extends Transformation<T, R> {
    private final MapFunction<T, R> mapper;

    public MapTransformation(MapFunction<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override
    public R apply(T input) {
        return mapper.apply(input);
    }

    @Override
    public Dataset<T> buildDataset() {
        return new MappedDataset<>();
    }
}
