package cn.edu.ustb.service.partition;

import java.util.List;

public class Partition<T> {
    private final String partitionId;
    private final T data;
    private final List<Partition<?>> parentPartitions;
    private final int index;

    public Partition(String partitionId, T data, List<Partition<?>> parents, int index) {
        this.partitionId = partitionId;
        this.data = data;
        this.parentPartitions = parents;
        this.index = index;
    }

    // 获取数据（可以是延迟加载）
    public T getData() {
        // 如果是文件路径，则在此处读取数据
        return data;
    }
}
