package cn.edu.ustb.service;

/**
 * 用于具体执行任务的工作器
 */
public class Worker {
    // 可用的slots
    private final int freeSlots;

    // 全局的资源管理者
    private final ResourceManager resourceManager;

    /**
     * 初始化worker
     * @param freeSlots 执行的工作槽数量
     * @param resourceManager 全局唯一的资源管理者
     */
    public Worker(int freeSlots, ResourceManager resourceManager) {
        this.freeSlots = freeSlots;
        this.resourceManager = resourceManager;
    }

    /**
     * 返回空闲可用的工作槽
     * @return 返回可用的工作槽的数量
     */
    public int getFreeSlots() {
        return freeSlots;
    }

    /**
     * 工作器在执行任务之前，会向rm进行注册
     */
    public void run() {
        resourceManager.registerWorker(this);
    }
}
