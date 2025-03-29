package cn.edu.ustb.model.transformation.function.impl;

public interface Function<IN,OUT> {
    public OUT apply(IN in);
}
