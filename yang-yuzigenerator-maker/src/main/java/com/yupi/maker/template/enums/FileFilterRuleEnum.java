package com.yupi.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/9 15:43
 * 文件过滤规则枚举
 */
public enum FileFilterRuleEnum {

    CONTAINS("包含","contains"),
    STARTS_WITH("前缀匹配","startsWith"),
    ENDS_WITH("后缀匹配","endsWith"),
    REGEX("正则","regex"),
    EQUALS("相等","equals");

    private final String text;
    private final String value;

    private static final Map<String, FileFilterRuleEnum> ruleToEnumMap = new HashMap<>();

    static {
        for (FileFilterRuleEnum anEnum : FileFilterRuleEnum.values()) {
            ruleToEnumMap.put(anEnum.getValue(), anEnum);
        }
    }

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    // 根据 value 获取对应的枚举常量的方法
    public static FileFilterRuleEnum getEnumByValue(String rule) {
        // 使用 Hutool 库的 ObjectUtil 判断输入的 value 是否为空
        if (ObjectUtil.isEmpty(rule)) {
            return null;
        }
        // 使用 Map 进行快速查找
        return ruleToEnumMap.get(rule);
    }
}
