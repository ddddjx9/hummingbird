package cn.edu.ustb.model;

import cn.edu.ustb.model.dependency.Dependency;
import cn.edu.ustb.model.partition.Partition;

import java.util.List;

public abstract class Dataset<T> implements Iterable<T> {
    // 获取数据分区列表（核心抽象）
    public abstract List<Partition<T>> getPartitions();

    // 获取父 RDD 的依赖列表
    public abstract List<Dependency<?>> getDependencies();
}
