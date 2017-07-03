package com.effective.common.druid.service;

import com.effective.common.druid.BaseSpringJunit;
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
public class EmployeeServiceTest extends BaseSpringJunit{
    
    private static Logger logger = LoggerFactory.getLogger(EmployeeServiceTest.class);
    
    @Autowired
    private EmployeeService employeeService;
    
    @Test
    public void testFindAll() throws Exception {
        logger.info(">>> DynamicDataSourceHolder.getDbType {}", DynamicDataSourceHolder.getRouteKey());
        List<Employee> employees = employeeService.findAll();
        logger.debug( "employee size {}",employees.size());
        employees = employees.subList(0,10);
        employees.forEach(employee -> {
            logger.info(employee.toString());
        });
    }
    
    @Test
    public void testUpdate(){
        employeeService.updateByName();
    }

}