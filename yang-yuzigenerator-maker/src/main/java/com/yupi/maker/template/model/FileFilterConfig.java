package com.yupi.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/9 11:33
 * 文件过滤配置
 */
@Data
@Builder
public class FileFilterConfig {

    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
