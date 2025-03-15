package cn.edu.ustb.model.dependency;

import cn.edu.ustb.model.Dataset;
import cn.edu.ustb.model.partition.impl.Partitioner;

public class ShuffleDependency<K,V> extends Dependency<V> {
    private final Dataset<V> parent;
    private final Partitioner<K> partitioner;

    public ShuffleDependency(Dataset<V> parent, Partitioner<K> partitioner) {
        this.parent = parent;
        this.partitioner = partitioner;
    }

    @Override
    public Dataset<V> getParent() {
        return parent;
    }
}
