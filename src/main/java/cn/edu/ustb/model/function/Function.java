package cn.edu.ustb.model.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function<IN, OUT> extends Serializable {
    OUT apply(IN value);
}