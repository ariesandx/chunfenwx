package com.chunfen.wx.filter;

import com.chunfen.wx.prop.DemoProp;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by xi.w on 2018-10-20.
 */
public class DemoFilter implements Filter {

    private DemoProp demoProp;

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());

        demoProp = (DemoProp) applicationContext.getBean("demoProp");
        System.out.println("this is filter");
        System.out.println("demo.prop.name:" + demoProp.getName());
        System.out.println("demo.prop.age:" + demoProp.getAge());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
