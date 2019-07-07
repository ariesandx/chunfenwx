package com.chunfen.wx.appender;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * @author chunfen.wx
 */
@Plugin(name = "DemoAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class DemoAppender extends AbstractAppender {

    protected DemoAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    public DemoAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    public static class Builder extends AbstractAppender.Builder
            implements org.apache.logging.log4j.core.util.Builder<DemoAppender>{

        @Override
        public DemoAppender build() {
            if (getName() == null) {
                LOGGER.error("No name provided for MyCustomAppenderImpl");
                return null;
            }
            if (getLayout() == null) {
                withLayout(PatternLayout.createDefaultLayout());
            }
            return new DemoAppender(getName(), getFilter(), getLayout());
        }

    }

    @Override
    public void append(LogEvent event) {
        byte[] bytes = getLayout().toByteArray(event);
        String msg = new String(bytes);
//        msg = "nihao";
        System.out.println("这个是自定义 appender " + msg);
    }

    // 下面这个方法可以接收配置文件中的参数信息
    // 下面是 直接得到 appender
//    @PluginFactory
//    public static DemoAppender createAppender(@PluginAttribute("name") String name,
//                                                  @PluginElement("Filter") final Filter filter,
//                                                  @PluginElement("Layout") Layout<? extends Serializable> layout,
//                                                  @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
//        if (name == null) {
//            LOGGER.error("No name provided for MyCustomAppenderImpl");
//            return null;
//        }
//        if (layout == null) {
//            layout = PatternLayout.createDefaultLayout();
//        }
//        return new DemoAppender(name, filter, layout, ignoreExceptions);
//    }

    // 下面是 通过 builder  的方式获取 appender
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
}
