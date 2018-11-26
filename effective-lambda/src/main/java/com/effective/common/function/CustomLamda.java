package com.effective.common.function;

import java.util.function.Consumer;

/**
 * Created by yanweiqi on 2017/9/13.
 */
@FunctionalInterface
public interface CustomLamda<T> {

    T testCustomFunction(Consumer<T> cunsumer);
}
