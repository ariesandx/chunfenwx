package com.chunfen.wx.layout;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;

/**
 * @author chunfen.wx
 */
@Plugin(name = "DemoLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class DemoLayout extends AbstractStringLayout {

    public DemoLayout(Charset charset) {
        super(charset);
    }

    public DemoLayout(Charset aCharset, byte[] header, byte[] footer) {
        super(aCharset, header, footer);
    }

    public DemoLayout(Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
        super(config, aCharset, headerSerializer, footerSerializer);
    }

    @Override
    public String toSerializable(LogEvent event) {
        return "{这个是自定义 layout " + event.getMessage().getFormattedMessage() + "}\n";
    }

    @PluginFactory
    public static DemoLayout createAppender(@PluginAttribute("name") String name,
                                                  @PluginAttribute("encode") final String encode) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }

        Charset charset = Charset.defaultCharset();
        if (encode != null) {
            charset = Charset.forName(encode);
        }
        return new DemoLayout(charset);
    }
}
