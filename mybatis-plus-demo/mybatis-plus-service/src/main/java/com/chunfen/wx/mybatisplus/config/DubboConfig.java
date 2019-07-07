package com.chunfen.wx.mybatisplus.config;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Autowired
    private DubboProp dubboProp;

    private <T> ReferenceBean<T> referenceBean(Class<T> clazz) {
        return referenceBean(clazz, null);
    }

    private <T> ReferenceBean<T> referenceBean(Class<T> clazz, String version) {
        return referenceBean(clazz, version, 3);
    }

    private <T> ReferenceBean<T> referenceBean(Class<T> clazz, String version, int retries) {
        ReferenceBean<T> referenceBean = new ReferenceBean<>();

        referenceBean.setInterface(clazz);
        referenceBean.setVersion(version);
        referenceBean.setRetries(retries < 0 ? 3 : retries);

        return referenceBeanInitAndCheck(referenceBean);
    }

    private <T> ReferenceBean<T> referenceBeanInitAndCheck(ReferenceBean<T> referenceBean) {
        referenceBean.setInit(dubboProp.isInit());
        referenceBean.setCheck(dubboProp.isCheck());
        return  referenceBean;
    }

    private <T> ServiceBean<T> serviceBean(T clazz, String version) {
        ServiceBean<T> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(clazz.getClass().getInterfaces()[0]);
        serviceBean.setVersion(version);
        serviceBean.setRef(clazz);
        serviceBean.setRetries(3);

        return serviceBean;
    }

    @Bean
    public ApplicationConfig application() {
        ApplicationConfig application = new ApplicationConfig();
        application.setName(dubboProp.getApplicationName());

        return application;
    }

    @Bean
    public RegistryConfig registry() {
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(dubboProp.getZkAddress());
        registry.setProtocol("zookeeper");
        registry.setRegister(false);
        return registry;
    }
}