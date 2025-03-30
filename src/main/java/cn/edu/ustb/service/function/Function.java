package cn.edu.ustb.service.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function<IN, OUT> extends Serializable {
    OUT apply(IN value);
}