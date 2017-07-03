package com.effective.common.druid.repository;

import com.effective.common.druid.BaseSpringJunit;
import com.effective.common.druid.annotation.ReadOnlyConnection;
import com.effective.common.druid.entity.Employee;
import com.effective.common.druid.routing.DynamicDataSourceHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */
public class EmployeeRepositoryTest extends BaseSpringJunit{
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryTest.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Test
    public void testFindAll(){
        List<Employee> employees = employeeRepository.findAll();
        logger.info(">>> DynamicDataSourceHolder.getDbType {}", DynamicDataSourceHolder.getRouteKey());
        logger.debug( "employee size {}",employees.size());
        employees = employees.subList(0,10);
        employees.forEach(employee -> {
                logger.info(employee.toString());
        });
    }
    
    

}