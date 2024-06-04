package com.yupi.generator.file;

import com.yupi.model.DataModel;
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
    public static void doGenerator(Object model) throws TemplateException, IOException {

        String inputRootPath = "F:\\it\\yupi\\yang-yuzigenerator\\yang-yuzigenerator-demo-project\\acm-template-pro";
        String outputRootPath = "F:\\it\\yupi\\yang-yuzigenerator";

        String inputPath;
        String outputPath;

//        生成动态文件
        inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicFileGenerator.doGenerator(inputPath, outputPath, model);

//        生成静态文件
        inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
//        生成静态文件
        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);

    }

    public static void main(String[] args) throws TemplateException, IOException {
        DataModel dataModel = new DataModel();
        dataModel.setLoop(false);
        dataModel.setAuthor("Victiny");
        dataModel.setOutputText("输出信息");
        doGenerator(dataModel);
    }
}
