package com.effective.common.druid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteConnection {
}
