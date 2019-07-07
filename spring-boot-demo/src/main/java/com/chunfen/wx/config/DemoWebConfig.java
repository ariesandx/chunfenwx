package com.chunfen.wx.config;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.chunfen.wx.filter.DemoFilter;
import com.chunfen.wx.interceptor.SqlInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xi.w on 2018-10-20.
 */
@Configuration
public class DemoWebConfig {

    // 注册过滤器
    @Bean
    FilterRegistrationBean<DemoFilter> demoFilter(){
        FilterRegistrationBean<DemoFilter> bean = new FilterRegistrationBean<DemoFilter>();
        bean.setFilter(new DemoFilter());
        bean.addUrlPatterns("/hello/*");
        return bean;
    }

    @Bean
    public SqlInterceptor performanceInterceptor(){
        SqlInterceptor performanceInterceptor = new SqlInterceptor();
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}
