package cn.edu.ustb.model.transformation;

import cn.edu.ustb.model.Dataset;
import cn.edu.ustb.model.DatasetContext;

import java.util.List;

public abstract class Transformation<T> {
    protected List<Transformation<?>> transformations;
    protected String name;

    /**
     * 构建dataset
     * @param context 获取上下文信息
     * @return 返回构建好的dataset
     */
    public abstract Dataset<T> buildDataset(DatasetContext context);
}
