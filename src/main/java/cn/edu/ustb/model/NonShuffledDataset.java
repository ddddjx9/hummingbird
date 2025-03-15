package cn.edu.ustb.model;

import cn.edu.ustb.model.dependency.Dependency;
import cn.edu.ustb.model.partition.Partition;

import java.util.Iterator;
import java.util.List;

public class NonShuffledDataset<T,U> extends Dataset<U> {
    public NonShuffledDataset(T dataset, T func) {

    }

    @Override
    public List<Partition<U>> getPartitions() {
        return null;
    }

    @Override
    public List<Dependency<?>> getDependencies() {
        return null;
    }

    @Override
    public Iterator<U> iterator() {
        return null;
    }
}
