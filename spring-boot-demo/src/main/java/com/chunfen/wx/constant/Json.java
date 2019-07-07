package com.chunfen.wx.constant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by xi.w on 2019/5/2.
 */
public class Json {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
