package com.mars.counter;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class DbUtil {
    public static DbUtil dbUtil = null;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
    /**
     * 初始化方法，为静态成员变量赋予动态属性
     */
    @PostConstruct
    public void init(){
        dbUtil = new DbUtil();
        dbUtil.setSqlSessionTemplate(this.sqlSessionTemplate);
    }

    public static long getId(){
        //从数据库得到的对象一定要判空
        Long id = dbUtil.getSqlSessionTemplate().selectOne(
                "testMapper.queryBalance");
        Long res = Optional.ofNullable(id).orElse(new Long(-1));
        return res;
    }
}
