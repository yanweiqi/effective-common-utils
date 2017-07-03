package com.effective.common.druid.aop;

import com.effective.common.druid.annotation.WriteConnection;
import com.effective.common.druid.routing.DataSourceEnum;
import com.effective.common.druid.routing.DynamicDataSourceHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */
@Aspect
@Component
public class WriteConnectionInterceptor implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(WriteConnectionInterceptor.class);

    private int order;

    @Value("100")
    public void setOrder(int order) {
        System.out.println("ReadOnlyConnectionInterceptor >>> order = 20");
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    //@Pointcut(value="execution(public * *(..))")
    @Pointcut("@annotation(com.effective.common.druid.annotation.WriteConnection)")
    public void anyPublicMethod() { }

    @Around("@annotation(writeConnection)")
    public Object proceed(ProceedingJoinPoint pjp, WriteConnection writeConnection) throws Throwable {
        try {
            DynamicDataSourceHolder.setRouteKey(DataSourceEnum.MASTER);
            log.info(">>> use db {}",DynamicDataSourceHolder.getRouteKey());
            Object result = pjp.proceed();
            return result;
        } finally {
            DynamicDataSourceHolder.removeRouteKey();
        }
    }
}