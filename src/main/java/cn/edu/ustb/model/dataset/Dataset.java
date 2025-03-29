package cn.edu.ustb.model.dataset;

import cn.edu.ustb.model.partition.Partition;

import java.util.List;

public abstract class Dataset<T> implements Iterable<T> {
    // 获取数据分区列表
    public abstract List<Partition<T>> getPartitions();
}
