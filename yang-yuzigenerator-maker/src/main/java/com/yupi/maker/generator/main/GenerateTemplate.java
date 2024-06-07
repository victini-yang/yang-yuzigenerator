package com.yupi.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.yupi.maker.generator.JarGenerator;
import com.yupi.maker.generator.ScriptGenerator;
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
public class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {

        Meta meta = MetaManager.getMeta();
        System.out.println(meta);
//        生成代码、文件方式
//        一、反射,是在程序运行中，生成代码或者类，让java加载，然后释放掉
//        二、freemaker生成

//        0.输出路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();

        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

//        1.复制原始文件
        String sourceCopyDestPath = copySource(meta, outputPath);

//        2.代码生成
        generateCode(meta, outputPath);

//        3.构建jar包
        String jarPath = buildJar(meta,outputPath);

//        4.封装脚本
        String shellOutputFilePath = buildScript(outputPath, jarPath);

//        5.生成精简版程序
        buildDist(outputPath,sourceCopyDestPath, jarPath, shellOutputFilePath);
    }

    protected String copySource(Meta meta, String outputPath) {
        //        1.复制原始文件、从原始模板文件路径复制到生成的代码包中
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    protected void buildDist(String outputPath, String sourceCopyDestPath,String jarPath, String shellOutputFilePath) {
        //        生成精简版本的程序
        String distOutputPath = outputPath + "-dist";
//        -拷贝jar包
//        String targetAbsolutePath = distOutputPath + File.separator + "target";
        String targetAbsolutePath = outputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
//        String jarAbsolutePath = outputPath + File.separator + jarPath;
        String jarAbsolutePath = System.getProperty("user.dir") + File.separator + "target";
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
//        拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        FileUtil.copy(shellOutputFilePath + ".bat", distOutputPath, true);
//        拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }

    //        封装脚本
    protected String buildScript(String outputPath, String jarPath) {
        String shellOutputFilePath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    protected String buildJar(Meta meta , String outputPath) throws IOException, InterruptedException {
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target" + File.separator + jarName;
        return jarPath;
    }

    /**
     * 代码生成
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        //        读取resource目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
//        java包的基础路径
        String outputBasePackage = meta.getBasePackage();
        System.out.println("outputBasePackage: " + outputBasePackage);
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

//        model.DataModel
        inputFilePath = inputResourcePath + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "model/DataModel.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.ConfigCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/MainGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/MainGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.GeneratorCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/GeneratorCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/GeneratorCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

//        cli.command.ListCommand
        inputFilePath = inputResourcePath + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        cli.command.CommandExecutor
        inputFilePath = inputResourcePath + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/DynamicGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        generator/StaticGenerator.java
        inputFilePath = inputResourcePath + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        Main.java
        inputFilePath = inputResourcePath + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        pom.xml
        inputFilePath = inputResourcePath + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //        README.md 项目介绍文件
        inputFilePath = inputResourcePath + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

    }


}