package cn.edu.ustb.model.stage;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class Stage {
    private final String stageName;
    private final List<Function<?, ?>> operations = new ArrayList<>();

    public Stage(String stageName) {
        this.stageName = stageName;
    }

    public void addOperation(Function<?, ?> operation) {
        operations.add(operation);
    }
}
