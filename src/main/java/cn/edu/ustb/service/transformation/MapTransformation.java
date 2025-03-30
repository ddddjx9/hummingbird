package cn.edu.ustb.service.transformation;

import cn.edu.ustb.service.dataset.Dataset;
import cn.edu.ustb.service.function.Function;

public class MapTransformation<IN, OUT> extends Transformation<IN, OUT> {
    private final Function<IN, OUT> mapper;

    public MapTransformation(String name, Function<IN, OUT> mapper) {
        super(name);
        this.mapper = mapper;
    }

    @Override
    public Dataset<OUT> apply(Dataset<IN> input) {
        // 实际执行逻辑在Task中完成
        return new Dataset<>(input.env, this);
    }
}