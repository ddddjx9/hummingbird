package cn.edu.ustb.model.dependency;

import cn.edu.ustb.model.Dataset;

public abstract class Dependency<T> {
    public abstract Dataset<T> getParent();
}