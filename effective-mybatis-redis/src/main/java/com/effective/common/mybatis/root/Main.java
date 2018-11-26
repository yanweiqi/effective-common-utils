package com.effective.common.mybatis.root;

import com.effective.common.mybatis.mapper.AccountMapper;
import com.effective.common.mybatis.service.MyBatisSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by yanweiqi on 2016/5/25.
 */
public class Main {
    private Logger logger = Logger.getLogger(this.getClass());

    public static void main(String[] args) throws Exception {
        new Main().testQuery();
    }

    private void testQuery() throws Exception {
        logger.debug("start ----");
        for (int i = 0; i < 10; i++) {
            SqlSession sqlSession = null;
            try {
                sqlSession = MyBatisSqlSessionFactory.getSqlSessionFactory().openSession(true);
                AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
                List<Map> maps = accountMapper.getAll();

                for (Map map : maps) {
                    logger.debug(map);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
            Thread.sleep(5000);
        }

        logger.debug("end ----");
    }
}
