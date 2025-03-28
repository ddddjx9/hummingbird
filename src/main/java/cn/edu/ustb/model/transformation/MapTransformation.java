package cn.edu.ustb.model.transformation;

import cn.edu.ustb.model.Dataset;
import cn.edu.ustb.model.DatasetContext;
import cn.edu.ustb.service.function.MapFunction;

import java.util.Collections;
import java.util.List;

public class MapTransformation<IN,OUT> extends Transformation<OUT>{
    private final MapFunction<IN, OUT> function;
    private final List<Transformation<IN>> inputs;

    public MapTransformation(Transformation<IN> input, MapFunction<IN, OUT> function) {
        this.inputs = Collections.singletonList(input);
        this.function = function;
    }

    @Override
    public Dataset<OUT> buildDataset(DatasetContext context) {
        Dataset<IN> inputDataset = input.buildDataset(context);
        // return inputDataset.map(function);
        return null;
    }
}
