package com.chunfen.tomcat.nio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message<T> {
    private Integer code;
    private T data;
    private String message;
}
