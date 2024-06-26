package com.yupi.web.model.dto.generator;

import com.yupi.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/30 16:45
 */
@Data
public class GeneratorMakeRequest implements Serializable {

    /**
     * 元信息
     */
    private Meta meta;

//    模版文件压缩包路径
    private String zipFilePath;

    private static final long serialVersionUID = 1L;
}
