package com.yupi.maker.generator.file;

import com.yupi.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 8:17
 * 动静结合
 */
public class FileGenerator {
    public static void doGenerator(Object model) throws TemplateException, IOException {
//      静态代码生成
        // 获取整个项目的根目录
        String projectPath = System.getProperty("user.dir");

        // 获取项目根目录的父目录
        File parentFile = new File(projectPath).getParentFile();

        // 构建输入路径：ACM示例代码模板目录
        String inputPath = new File(parentFile + File.separator + "yang-yuzigenerator-demo-project" +
                File.separator + "acm-template").getAbsolutePath();
        System.out.println(inputPath);

        // 设置输出路径为项目根目录的父目录
        String outputPath = projectPath;
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);


//        动态代码生成器
        String DynamicInputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicOutputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerator(DynamicInputPath,DynamicOutputPath,model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        DataModel dataModel = new DataModel();
        dataModel.setLoop(false);
        dataModel.setAuthor("Victiny");
        dataModel.setOutputText("输出信息");
        doGenerator(dataModel);
    }
}
