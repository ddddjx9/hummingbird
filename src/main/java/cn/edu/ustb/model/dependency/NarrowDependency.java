package cn.edu.ustb.model.dependency;

import cn.edu.ustb.model.Dataset;

public class NarrowDependency<T> extends Dependency<T> {
    private final Dataset dataset;

    public NarrowDependency(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public Dataset<T> getParent() {
        return null;
    }
}
