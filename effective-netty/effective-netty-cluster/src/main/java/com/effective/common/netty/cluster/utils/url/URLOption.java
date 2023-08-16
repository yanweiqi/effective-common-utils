package com.effective.common.netty.cluster.utils.url;

import java.util.function.Supplier;

/**
 * URL配置项
 *
 * @param <T>
 */
public class URLOption<T> extends Option<T> {
    /**
     * 配置项名称
     */
    protected String name;
    /**
     * 选项提供者
     */
    protected Supplier<T> supplier;

    /**
     * 默认构造函数
     */
    public URLOption() {
    }

    /**
     * 构造函数
     *
     * @param name  名称
     * @param value 值
     */
    public URLOption(String name, T value) {
        super(value);
        this.name = name;
    }

    /**
     * 构造函数
     *
     * @param name
     * @param supplier
     */
    public URLOption(String name, Supplier<T> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public T getValue() {
        if (value == null && supplier != null) {
            value = supplier.get();
        }
        return value;
    }
}
