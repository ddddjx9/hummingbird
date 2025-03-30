package cn.edu.ustb.core.task;

import cn.edu.ustb.model.dataset.MapTransformation;
import cn.edu.ustb.model.dataset.Transformation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Task<IN, OUT> implements Runnable {
    @Getter
    private final Transformation<IN, OUT> transformation;
    private final BlockingQueue<IN> inputQueue = new LinkedBlockingQueue<>();
    private final List<BlockingQueue<OUT>> outputChannels = new ArrayList<>();

    public Task(Transformation<IN, OUT> trans, int subtaskIndex) {
        this.transformation = trans;
    }

    public void addOutputChannel(BlockingQueue<?> channel) {
        if (!channel.getClass().equals(BlockingQueue.class)) {
            throw new IllegalArgumentException("Channel type mismatch");
        }
        outputChannels.add((BlockingQueue<OUT>) channel);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                IN input = inputQueue.poll(100, TimeUnit.MILLISECONDS);
                if (input != null) {
                    OUT output = process(input);
                    outputChannels.forEach(channel -> channel.offer(output));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private OUT process(IN input) {
        // 动态分派到具体Transformation处理
        if (transformation instanceof MapTransformation) {
            MapTransformation<IN, OUT> mapTrans = (MapTransformation<IN, OUT>) transformation;
            return mapTrans.apply(input);
        }
        throw new UnsupportedOperationException();
    }

    public BlockingQueue<?> getInputChannel() {
        return inputQueue;
    }
}