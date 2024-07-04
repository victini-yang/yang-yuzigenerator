package com.yupi.web.model.dto.generator;

import com.yupi.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Victiny
 * 缓存代码生成器请求
 * @Version 1.0
 * @Date create in 2024/6/30 16:45
 */
@Data
public class GeneratorCacheRequest implements Serializable {

    /**
     * 生成器的id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
