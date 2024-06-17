package com.yupi.generator;

import freemarker.template.TemplateException;
import com.yupi.model.DataModel;

import java.io.File;
import java.io.IOException;


/**
* @Author Victiny
* @Version 1.0
* @Date create in 2024-6-2
* 动静结合
*/
public class MainGenerator {
    public static void doGenerator(DataModel model) throws TemplateException, IOException {

        String inputRootPath = ".source/acm-template-pro";
        String outputRootPath = "generated";


        String inputPath;
        String outputPath;

        boolean needGit = model.needGit;
        boolean loop = model.loop;
        String author = model.mainTemplate.author;
        String outputText = model.mainTemplate.outputText;

    // groupKey = git
    if(needGit){
                inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
                outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
                StaticGenerator.copyFilesByHutool(inputPath, outputPath);
                inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
                outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
                StaticGenerator.copyFilesByHutool(inputPath, outputPath);
    }
        inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicGenerator.doGenerator(inputPath, outputPath, model);
    }
}
