package cn.edu.ustb.model;

import cn.edu.ustb.model.dependency.Dependency;
import cn.edu.ustb.model.partition.Partition;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShuffledDataset<K, V> extends Dataset<Map.Entry<K, Iterable<V>>> {

    @Override
    public List<Partition<Map.Entry<K, Iterable<V>>>> getPartitions() {
        return null;
    }

    @Override
    public List<Dependency<?>> getDependencies() {
        return null;
    }

    @Override
    public Iterator<Map.Entry<K, Iterable<V>>> iterator() {
        return null;
    }
}
