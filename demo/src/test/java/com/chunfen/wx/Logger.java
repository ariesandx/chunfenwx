package com.chunfen.wx;


import com.chunfen.wx.util.ReflectionUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 使用自己的 logger 输出 具体日志 发生的 行号
 * Created by xi.w on 2018-08-26.
 */
public class Logger {
    private static org.slf4j.Logger logger;
    private static final String FQCN = Logger.class.getName();

    public static void debug(String msg) {
        logger.debug(msg);
    }

    static {
        try {
//            Enhancer eh = new Enhancer();
//            eh.setSuperclass(org.apache.log4j.Logger.class);
//            eh.setCallback(new LogInterceptor());
//            org.apache.log4j.Logger loggerProxy= (org.apache.log4j.Logger) eh.create(new Class[]{String.class}, new Object[]{Logger.class.getName()});

            Enhancer eh = new Enhancer();
            eh.setSuperclass(org.apache.log4j.Logger.class);
            eh.setCallbackType(LogInterceptor.class);
            Class c = eh.createClass();
            Enhancer.registerCallbacks(c, new LogInterceptor[]{new LogInterceptor()});
            Constructor<org.apache.log4j.Logger> constructor = c.getConstructor(String.class);
            org.apache.log4j.Logger loggerProxy= constructor.newInstance(Logger.class.getName());

            LoggerRepository loggerRepository = LogManager.getLoggerRepository();
            org.apache.log4j.spi.LoggerFactory lf = ReflectionUtil.getFieldValue(loggerRepository, "defaultFactory");
            Object loggerFactoryProxy = Proxy.newProxyInstance(
                    LoggerFactory.class.getClassLoader(),
                    new Class[]{LoggerFactory.class},
                    new NewLoggerHandler(loggerProxy)
            );

            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", loggerFactoryProxy);
            logger = org.slf4j.LoggerFactory.getLogger(Logger.class.getName());
            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", lf);
        } catch (Exception e) {
            throw new RuntimeException("初始化Logger失败", e);
        }
    }

    private static class LogInterceptor implements MethodInterceptor {
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            // 只拦截log方法。
            if (objects.length != 4 || !method.getName().equals("log"))
                return methodProxy.invokeSuper(o, objects);
            // 替换传给log方法的第一个参数为我们自定义的FQCN
            objects[0] = FQCN;
            return methodProxy.invokeSuper(o, objects);
        }
    }

    private static class NewLoggerHandler implements InvocationHandler {
        private final org.apache.log4j.Logger proxyLogger;

        public NewLoggerHandler(org.apache.log4j.Logger proxyLogger) {
            this.proxyLogger = proxyLogger;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return proxyLogger;
        }
    }


}
