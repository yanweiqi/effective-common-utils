package com.effective.common.druid.repository;


import com.effective.common.druid.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
}
