package com.yupi.maker.template.model;

import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/13 9:52
 */
@Data
public class TemplateMakerOutputConfig {

//    从未分组的文件中移除组内的同名文件
    private boolean isRemoveGroupFilesFromRoot = true;
}
