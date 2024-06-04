package com.yupi.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 8:39
 */
@CommandLine.Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{

    @Override
    public void run() {
        String projectPath = System.getProperty("user.dir");
//        整个项目的根路径
        File parentFile = new File(projectPath).getParentFile();
//        输入路径
        String  inputPath = new File(parentFile, "yang-yuzigenerator-demo-properties/acm-template").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }
    }
}
