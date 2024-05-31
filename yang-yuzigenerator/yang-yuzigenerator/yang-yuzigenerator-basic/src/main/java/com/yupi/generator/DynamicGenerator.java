package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/30 11:08
 * 动态文件生成器
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {

        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        String inputPath = projectPath + File.separator + "yang-yuzigenerator-basic/src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setAuthor("Victiny");
        mainTemplateConfig.setOutputText("输出信息");
        doGenerator(inputPath,outputPath,mainTemplateConfig);

    }

    public static void doGenerator(String inputPath, String outputPath,Object model) throws IOException, TemplateException {

        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        //指定模板文件所在的路径

        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
//        设置指定不用数字逗号分隔符
        configuration.setNumberFormat("0.######");
//        创建模板对象,加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

//        指定生成的文件  yang-yuzigenerator-basic目录下
        Writer out = new FileWriter(outputPath);

//        生成文件
        template.process(model,out);
        out.close();
    }
}
