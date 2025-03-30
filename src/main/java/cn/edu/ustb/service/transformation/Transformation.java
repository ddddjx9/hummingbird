package cn.edu.ustb.service.transformation;

import cn.edu.ustb.service.dataset.Dataset;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public abstract class Transformation<IN, OUT> {
    private final String name;
    private final Map<String, String> config = new HashMap<>();
    private int parallelism = 1;
    private final List<Transformation<?, ?>> inputs = new ArrayList<>();
    private final List<Transformation<?, ?>> outputs = new ArrayList<>();
    private final String uid = UUID.randomUUID().toString(); // 添加唯一ID

    public String getId() {
        return uid;
    }

    public void addInput(Transformation<?, ?> input) {
        inputs.add(input);
        input.outputs.add(this);
    }

    public Transformation(String name) {
        this.name = name;
    }

    public abstract Dataset<OUT> apply(Dataset<IN> input);
}
