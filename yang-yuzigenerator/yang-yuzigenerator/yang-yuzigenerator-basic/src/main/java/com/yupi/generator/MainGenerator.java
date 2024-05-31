package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 8:17
 * 动静结合
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException {

//      静态代码生成
        // 获取整个项目的根目录
        String projectPath = System.getProperty("user.dir");

        // 获取项目根目录的父目录
        String parentFile = new File(projectPath).getAbsolutePath();

        // 构建输入路径：ACM示例代码模板目录
        String inputPath = parentFile + File.separator + "yang-yuzigenerator-demo-properties" +
                File.separator + "acm-template";
        System.out.println(inputPath);

        // 设置输出路径为项目根目录的父目录
        String outputPath = parentFile;
        StaticGenerator.copyFilesByRecursive(inputPath, outputPath);


//        动态代码生成器
        String DynamicInputPath = projectPath + File.separator + "yang-yuzigenerator-basic/src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicOutputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setAuthor("Victiny");
        mainTemplateConfig.setOutputText("输出信息");
        DynamicGenerator.doGenerator(DynamicInputPath,DynamicOutputPath,mainTemplateConfig);
    }
}
