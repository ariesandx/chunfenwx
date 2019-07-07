package com.chunfen.wx.mybatisplus.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    private static final Logger LOGGER = Logger.getLogger(DataSourceConfig.class);

    @Autowired
    private DataSourceProp dataSourceProp;

    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl(dataSourceProp.getUrl());
        dataSource.setUsername(dataSourceProp.getUsername());
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(dataSourceProp.getMinIdle());
        dataSource.setMaxWait(1000);
        dataSource.setMinIdle(dataSourceProp.getMinIdle());
        dataSource.setMaxActive(dataSourceProp.getMaxActive());
        dataSource.setTimeBetweenEvictionRunsMillis(3000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(false);

        try {
            dataSource.setFilters("stat,config");
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return dataSource;
    }
}