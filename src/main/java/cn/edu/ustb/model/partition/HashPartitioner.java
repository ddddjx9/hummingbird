package cn.edu.ustb.model.partition;

import cn.edu.ustb.model.partition.impl.Partitioner;

public class HashPartitioner<K> implements Partitioner<K> {
    private final int numPartitions = 3;

    @Override
    public int numPartitions() {
        return numPartitions;
    }

    @Override
    public int getPartition(K key) {
        return (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}
