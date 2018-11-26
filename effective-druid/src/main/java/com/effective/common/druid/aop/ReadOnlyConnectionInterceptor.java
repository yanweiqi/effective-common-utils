package com.effective.common.druid.aop;

import com.effective.common.druid.annotation.ReadOnlyConnection;
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
 * @date 2017/6/29 ${time}
 * @Description: TODO
 */
@Aspect
@Component
public class ReadOnlyConnectionInterceptor implements Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(ReadOnlyConnectionInterceptor.class);

    private int order;

    @Value("20")
    public void setOrder(int order) {
        this.order = order;
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
    
    @Pointcut("@annotation(com.effective.common.druid.annotation.ReadOnlyConnection)")
    public void anyPublicMethod() { }


    @Around("@annotation(readOnlyConnection)")
    public Object proceed(ProceedingJoinPoint pjp, ReadOnlyConnection readOnlyConnection) throws Throwable {
        try {
            DynamicDataSourceHolder.setRouteKey(DataSourceEnum.SLAVE);
            logger.info("use DB {}",DynamicDataSourceHolder.getRouteKey());
            Object result = pjp.proceed();
            return result;
        } finally {
            DynamicDataSourceHolder.removeRouteKey();
        }
    }
}
