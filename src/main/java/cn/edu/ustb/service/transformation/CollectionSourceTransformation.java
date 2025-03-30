package cn.edu.ustb.service.transformation;

import cn.edu.ustb.service.dataset.Dataset;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CollectionSourceTransformation<T> extends Transformation<Void, T> {
    private final List<T> data;

    public CollectionSourceTransformation(List<T> data) {
        super("CollectionSource");
        this.data = data;
    }

    @Override
    public Dataset<T> apply(Dataset<Void> input) {
        throw new UnsupportedOperationException("Source cannot have input");
    }

    public void runSource(BlockingQueue<T> outputChannel) {
        new Thread(() -> {
            for (T item : data) {
                outputChannel.offer(item);
            }
        }).start();
    }
}