package cn.edu.ustb.model.function;

import cn.edu.ustb.model.dataset.Dataset;

import java.util.function.Function;

public class FilterFunction<IN, OUT> implements Function<Dataset<IN>, Dataset<OUT>> {
    @Override
    public Dataset<OUT> apply(Dataset<IN> in) {
        return null;
    }

    public String getOperationName() {
        return "NARROW";
    }
}
