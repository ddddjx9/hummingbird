package cn.edu.ustb.model.partition.impl;

public interface Partitioner<K> {
    int numPartitions();
    int getPartition(K key);
}