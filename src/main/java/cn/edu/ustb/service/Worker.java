package cn.edu.ustb.service;

public class Worker {
    private final int freeSlots;

    private final ResourceManager resourceManager;

    public Worker(int freeSlots, ResourceManager resourceManager) {
        this.freeSlots = freeSlots;
        this.resourceManager = resourceManager;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public void run() {
        resourceManager.registerWorker(this);
    }
}
