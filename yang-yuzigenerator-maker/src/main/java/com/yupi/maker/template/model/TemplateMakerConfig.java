package com.yupi.maker.template.model;

import com.yupi.maker.meta.Meta;
import lombok.Data;

/**
 * 模板制作文件
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/12 10:49
 */
@Data
public class TemplateMakerConfig {

    private Long id;

    private Meta meta = new Meta();

    private String originProjectPath;

    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();

    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
