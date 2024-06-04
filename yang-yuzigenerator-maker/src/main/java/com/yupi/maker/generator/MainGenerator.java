package com.yupi.maker.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.yupi.maker.generator.file.DynamicFileGenerator;
import com.yupi.maker.meta.Meta;
import com.yupi.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/2 11:20
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {

        Meta meta = MetaManager.getMeta();
        System.out.println(meta);
//        生成代码、文件方式
//        一、反射,是在程序运行中，生成代码或者类，让java加载，然后释放掉
//        二、freemaker生成

//        输出路径
        String projectPath = System.getProperty("user.dir");
//        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        String outputPath = projectPath + File.separator + "generated";

        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

//        读取resource目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
        System.out.println("inputResourcePath: " + inputResourcePath);

//        java包的基础路径
//        com.yupi
        String outputBasePackage = meta.getBasePackage();
        System.out.println("outputBasePackage: " + outputBasePackage);
//        com/yupi
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        System.out.println("outputBasePackagePath: " + outputBasePackagePath);
//        F:\it\yupi\yang-yuzigenerator\yang-yuzigenerator-maker\generated\src/main/java/com/yupi
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;
        System.out.println("outputBaseJavaPackagePath: " + outputBaseJavaPackagePath);

        String inputFilePath;
        String outputFilePath;

//        model.DataModel
        inputFilePath = inputResourcePath + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "model/DataModel.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.ConfigCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ConfigCommand.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.GeneratorCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/GeneratorCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/GeneratorCommand.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.ListCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ListCommand.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        cli.command.CommandExecutor
        inputFilePath = inputResourcePath + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/CommandExecutor.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/MainGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/MainGenerator.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/DynamicGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/DynamicGenerator.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/StaticGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/StaticGenerator.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        Main.java
        inputFilePath = inputResourcePath + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        System.out.println("inputFilePath = " + inputFilePath);
        System.out.println("outputFilePath = " + outputFilePath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        pom.xml
        inputFilePath = inputResourcePath + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
//        -maker/target/classes/templates/pom.xml.ftl
        System.out.println("inputFilePath = " + inputFilePath);
//        -maker\generated\src/main/java/com/yupi\pom.xml
        System.out.println("pom.xml.outputFilePath = " + outputFilePath);
//        \generated\src/main/java/com/yupi
        System.out.println("outputPath = " + outputPath);
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        构建jar包，在你运行 Maven 命令的目录中要有 pom.xml 文件，所以不用带pom.xml
//        outputPath = F:\it\yupi\yang-yuzigenerator\yang-yuzigenerator-maker\generated
        JarGenerator.doGenerate(outputPath);

//        封装脚本
        String shellOutputPath = outputPath + File.separator + "generator";
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target" + File.separator + jarName;
        System.out.println("shellOutputPath = " + shellOutputPath);
        System.out.println("jarName = " + jarName);
        System.out.println("jarPath = " + jarPath);
        ScriptGenerator.doGenerate(shellOutputPath, jarPath);
    }
}