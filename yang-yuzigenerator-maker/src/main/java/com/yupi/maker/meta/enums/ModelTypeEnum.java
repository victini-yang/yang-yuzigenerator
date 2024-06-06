package com.yupi.maker.meta.enums;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/5 10:19
 * 模型类型枚举
 */
public enum ModelTypeEnum {

    STRING("动态","string"),
    BOOLEAN("静态","boolean");

    private final String text;
    private final String value;

    ModelTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
