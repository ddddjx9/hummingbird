package cn.edu.ustb.model.dataset;

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

    public void runSource(BlockingQueue<?> outputChannel) {
        new Thread(() -> {
            for (T item : data) {
                outputChannel.offer(item);
            }
        }).start();
    }
}