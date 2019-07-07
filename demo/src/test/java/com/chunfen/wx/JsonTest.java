package com.chunfen.wx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * Gson对字符串null的字段转换为空字符串输出
 * @author chunfen.wx
 */
public class JsonTest {

        private static final TypeAdapter STRING = new TypeAdapter() {
        @Override
        public void write(JsonWriter jsonWriter, Object o) throws IOException {
            if (o == null) {
                // 在这里处理null改为空字符串
                jsonWriter.value("");
                return;
            }
            jsonWriter.value(o.toString());
        }

        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }
    };

    @Test
    public void gson() throws Exception{
        GsonBuilder gsonBuilder = new GsonBuilder();

        //注册自定义String的适配器
//        https://blog.csdn.net/kuailebuzhidao/article/details/61936301
//        Gson对字符串null的字段转换为空字符串输出
        gsonBuilder.registerTypeAdapter(String.class, STRING);

        Gson gson = gsonBuilder.create();

        System.out.println(gson.toJson(new JsonDomain()));
    }
}
