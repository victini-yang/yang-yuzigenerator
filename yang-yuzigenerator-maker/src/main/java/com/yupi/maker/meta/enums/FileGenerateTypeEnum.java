package com.yupi.maker.meta.enums;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/5 10:19
 * 生成类型枚举
 */
public enum FileGenerateTypeEnum {

    DYNAMIC("动态","dynamic"),
    STATIC("静态","static");

    private final String text;
    private final String value;

    FileGenerateTypeEnum(String text, String value) {
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
