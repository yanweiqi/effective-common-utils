package com.effective.common.netty.cluster.utils.url;

import java.util.function.Supplier;

/**
 * URL配置项，具有两个配置项名称
 *
 * @param <T>
 */
public class URLBiOption<T> extends Option<T> {
    /**
     * 配置项名称
     */
    protected String name;
    /**
     * 候选配置项名称
     */
    protected String candidate;
    /**
     * 选项提供者
     */
    protected Supplier<T> supplier;

    /**
     * 默认构造函数
     */
    public URLBiOption() {
    }

    /**
     * 构造函数
     *
     * @param name  名称
     * @param value 值
     */
    public URLBiOption(String name, String candidate, T value) {
        super(value);
        this.name = name;
        this.candidate = candidate;
    }

    /**
     * 构造函数
     *
     * @param name
     * @param candidate
     * @param supplier
     */
    public URLBiOption(String name, String candidate, Supplier<T> supplier) {
        this.name = name;
        this.candidate = candidate;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    @Override
    public T getValue() {
        if (value == null && supplier != null) {
            value = supplier.get();
        }
        return value;
    }
}
