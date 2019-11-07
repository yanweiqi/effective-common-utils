package com.effective.common.akka;


import akka.actor.ActorSystem;
import org.junit.jupiter.api.*;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Dessciption TODO
 * @Author yanweiqi
 * @Date 2019-11-06 5:22 PM
 */

@Disabled
@RunWith(JUnitPlatform.class)
@DisplayName("Testing using JUnit 5")
public abstract class AbstractJavaTest {

    protected static final Logger log = LoggerFactory.getLogger(AbstractJavaTest.class);


    @BeforeAll
    public static void init() {
        log.info("@BeforeAll: init()");
    }

    @AfterAll
    public static void done() {
        log.info("@AfterAll: done()");
    }

    @BeforeEach
    public void setUp() throws Exception {
        log.info("@BeforeEach: setUp start ActorSystem");
    }

    @AfterEach
    public void tearDown() throws Exception {
        log.info("@AfterEach: tearDown()");
        //system.terminate();
        //system = null;
    }

}
