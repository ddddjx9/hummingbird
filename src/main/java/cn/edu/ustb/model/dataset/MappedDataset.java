package cn.edu.ustb.model.dataset;

import cn.edu.ustb.model.partition.Partition;

import java.util.Iterator;
import java.util.List;

public class MappedDataset<R> extends Dataset<R>{
    @Override
    public List<Partition<R>> getPartitions() {
        return null;
    }

    @Override
    public Iterator<R> iterator() {
        return null;
    }
}
