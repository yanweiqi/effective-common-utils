package com.baomidou.mybatisplus.samples.crud;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.samples.crud.entity.App;
import com.baomidou.mybatisplus.samples.crud.mapper.AppMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class CrudAppTest {

    @Resource
    private AppMapper appMapper;

    @Test
    public void testInsert() {
        App app = App.builder()
                .appName("jdt")
                .appSwitch(1)
                .appVersion("1.0.0")
                .osSwitch(1)
                .osVersion("1.0.1")
                .pushProtocolVersion("2.0.1")
                .subscribeSwitch(1)
                .keywordId(1)
                .blackListId(1)
                .frequencyId(1)
                .whiteListId(1)
                .yn(0)
                .build();
        app.setErp("ywq");
        assertThat(appMapper.insert(app)).isGreaterThan(0);
        // 成功直接拿回写的 ID
        log.info("app : {}", app.toString());
        assertThat(app.getId()).isNotNull();

        List<App> list = appMapper.selectList(new QueryWrapper<App>().select());
        log.info(list.size() + "");
    }


}
