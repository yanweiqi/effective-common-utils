package com.effective.common.mybatis.mapper;

import com.effective.common.mybatis.domain.Account;

import java.util.*;

/**
 * Created by yanweiqi on 2016/5/25.
 */
public interface AccountMapper {
    void insert(Account account);
    List<Map> getAll();
}
