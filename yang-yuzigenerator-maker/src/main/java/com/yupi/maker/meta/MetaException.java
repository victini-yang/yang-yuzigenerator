package com.yupi.maker.meta;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/5 8:58
 * 元信息异常类
 */
public class MetaException extends RuntimeException{

    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
