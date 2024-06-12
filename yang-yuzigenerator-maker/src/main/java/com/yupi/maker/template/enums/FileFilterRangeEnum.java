package com.yupi.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/9 15:53
 * 文件过滤范围枚举
 */
public enum FileFilterRangeEnum {

    FILE_NAME("文件名称","fileName"),

    FILE_CONTENT("文件内容","fileContent");

    private final String text;

    private final String value;

//    用map储存更快
//    valueToEnumMap = {fileName=FILE_NAME, fileContent=FILE_CONTENT}
    private static final Map<String, FileFilterRangeEnum> valueToEnumMap = new HashMap<>();

    static {
        for (FileFilterRangeEnum anEnum : FileFilterRangeEnum.values()) {
            valueToEnumMap.put(anEnum.getValue(), anEnum);
        }
    }

//    {
//        "fileName": FileFilterRangeEnum.file_name,
//        "fileContent": FileFilterRangeEnum.file_content
//    }
//    FileFilterRangeEnum.file_name是一个枚举常量

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    // 根据 value 获取对应的枚举常量的方法
    public static FileFilterRangeEnum getEnumByValue(String value) {
        // 使用 Hutool 库的 ObjectUtil 判断输入的 value 是否为空
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        // 使用 Map 进行快速查找
        return valueToEnumMap.get(value);
    }
}

