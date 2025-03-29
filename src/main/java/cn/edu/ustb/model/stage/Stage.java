package cn.edu.ustb.model.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Stage {
    private final String stageName;


    public Stage(String stageName) {
        this.stageName = stageName;
    }

    private final List<Function<?, ?>> operations = new ArrayList<>();

    public void addOperation(Function<?, ?> operation) {
        operations.add(operation);
    }

    public List<Function<?, ?>> getOperations() {
        return operations;
    }
}
