package cn.edu.ustb.model.function;

import cn.edu.ustb.model.dataset.Dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapFunction<IN, OUT> implements Function<Dataset<IN>, Dataset<OUT>> {

    @Override
    public Dataset<OUT> apply(Dataset<IN> in) {
        List<IN> datas = in.collect();
        List<OUT> collect = datas.stream().map((Function<IN, OUT>) in1 -> null).collect(Collectors.toList());

        return new Dataset<>((ArrayList<OUT>) collect);
    }

    public String getOperationName() {
        return "NARROW";
    }
}
