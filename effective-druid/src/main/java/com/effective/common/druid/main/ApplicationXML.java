package com.effective.common.druid.main;

import com.effective.common.druid.annotation.ReadOnlyConnection;
import com.effective.common.druid.annotation.WriteConnection;
import com.effective.common.druid.repository.EmployeeRepository;
import com.effective.common.druid.routing.DynamicDataSourceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yanweiqi
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @date 2017/6/30 ${time}
 * @Description: TODO
 */

public class ApplicationXML {

    private static final Logger log = LoggerFactory.getLogger(ApplicationXML.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        ApplicationXML main = context.getBean(ApplicationXML.class);
        //main.testReplica();
        //main.testMaster();

        //main.testMasterUpdate();
        //main.testReplica();  // not touched
        //main.testMaster();   // updated

    }

    /**
    @Transactional
    @ReadOnlyConnection
    public void testReplica(){
        log.info(">>> DynamicDataSourceHolder.getDbType {}", DynamicDataSourceHolder.getDbType());
        log.info(">>> Replica: {}", employeeRepository.findAll());
    }

    @Transactional
    @WriteConnection
    public void testMaster(){
        log.info(">>> DynamicDataSourceHolder.getDbType {}", DynamicDataSourceHolder.getDbType());
        log.info(">>> Master: {}", employeeRepository.findAll());
    }

    @Transactional
    @WriteConnection
    public void testMasterUpdate(){
        log.info(">>> DynamicDataSourceHolder.getDbType {}", DynamicDataSourceHolder.getDbType());
        log.info(">>> Master: {}", employeeRepository.findAll());


        SomeData someData = new SomeData();
        someData.setId(2);
        someData.setName("name");
        someData.setValue("newData");
        repo.save(someData);
        log.info(">>> Master updated:{}", repo.findAll());
    }
     **/
}
