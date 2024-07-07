package com.yupi.web.job;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yupi.web.manager.CosManager;
import com.yupi.web.mapper.GeneratorMapper;
import com.yupi.web.model.entity.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/7 15:02
 */
@Slf4j
@Component
public class ClearCosJobHandler {

    @Resource
    private CosManager cosManager;

    @Resource
    private GeneratorMapper generatorMapper;

    @XxlJob("clearCosJobHandler")
    public void clearCosJobHandler() {
        log.info("clearCosJobHandler start");
//        编写业务逻辑
//        1.删除用户上传的模板制作文件
        cosManager.deleteDir("/generator_make_template");

//        2.已删除的代码生成器对应的产物包文件
        List<Generator> generatorList = generatorMapper.listDeletedGenerator();

        List<String> keyList = generatorList.stream().map(Generator::getDistPath)
                .filter(StrUtil::isNotBlank)
//                移除‘/’前缀
                .map(disPath -> disPath.substring(1))
                .collect(Collectors.toList());
        cosManager.deleteObjects(keyList);

        log.info("clearCosJobHandler end");
    }
}
