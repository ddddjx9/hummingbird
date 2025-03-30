package cn.edu.ustb.service.partition.impl;

public interface Partitioner<K> {
    int numPartitions();
    int getPartition(K key);
}