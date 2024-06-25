package com.yupi.maker;


import com.yupi.maker.generator.main.GenerateTemplate;
import com.yupi.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 10:27
 */
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();

    }
}
