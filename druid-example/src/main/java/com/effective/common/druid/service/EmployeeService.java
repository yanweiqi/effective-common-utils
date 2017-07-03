package com.effective.common.druid.service;

import com.effective.common.druid.annotation.ReadOnlyConnection;
import com.effective.common.druid.annotation.WriteConnection;
import com.effective.common.druid.entity.Employee;
import com.effective.common.druid.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */
@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @ReadOnlyConnection
    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }
    
    @WriteConnection
    public void updateByName(){
        Employee employee =  employeeRepository.findOne(10001);
        employee.setFirstName("weiqi");
        employee.setLastName("yan");
        employeeRepository.save(employee);
    }
    
}
