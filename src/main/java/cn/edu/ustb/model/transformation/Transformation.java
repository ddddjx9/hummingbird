package cn.edu.ustb.model.transformation;

import cn.edu.ustb.model.dataset.Dataset;

import java.util.List;

public abstract class Transformation<T,R> {
    protected List<Transformation<T,R>> transformations;
    protected String name;

    /**
     * 构建dataset
     * @return 返回构建好的dataset
     */
    public abstract Dataset<T> buildDataset();

    /**
     * 执行数据转换的核心方法（需子类具体实现）
     * @param input 上游数据结果
     * @return 转换后的数据结果
     */
    public abstract R apply(T input);
}
