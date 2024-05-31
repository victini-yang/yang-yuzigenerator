package com.yupi.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/27 9:09
 * 静态文件生成器
 */
public class StaticGenerator {
    public static void main(String[] args) throws Exception {

        // 获取整个项目的根目录
        // 例如：F:\it\yupi\yang-yuzigenerator\yang-yuzigenerator-basic
        String projectPath = System.getProperty("user.dir");
//        System.out.println(projectPath);

        // 获取项目根目录的父目录
        // 例如：F:\it\yupi\yang-yuzigenerator
        String parentFile = new File(projectPath).getAbsolutePath();

        // 构建输入路径：ACM示例代码模板目录
        // 例如：F:\it\yupi\yang-yuzigenerator\yang-yuzigenerator-demo-properties\acm-template
        String inputPath = parentFile + File.separator + "yang-yuzigenerator-demo-properties" +
                File.separator + "acm-template";
        System.out.println(inputPath);

        // 设置输出路径为项目根目录的父目录
        String outputPath = parentFile;
        copyFilesByRecursive(inputPath, outputPath);
    }

    /**
     * 使用 Hutool 库实现文件复制操作，将输入目录的内容完整地复制到输出目录下。
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

    /**
     * 递归实现文件复制操作，处理输入路径下的所有文件和目录。
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByRecursive(String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        try {
            copyFileByRecursive(inputFile, outputFile);
        } catch (Exception e) {
            System.out.println("文件复制失败");
            e.printStackTrace();
        }
    }

    /**
     * 递归复制文件和目录：
     * 1. 文件 A 到目录 B：将文件 A 放在目录 B 下。
     * 2. 文件 A 到文件 B：将文件 A 覆盖文件 B。
     * 3. 目录 A 到目录 B：将目录 A 放在目录 B 下。
     * 核心思路：先创建目标目录，然后遍历源目录内的文件和子目录，逐个复制。
     *
     * @param source      源文件
     * @param destination 目标文件
     * @throws Exception
     */
    public static void copyFileByRecursive(File source, File destination) throws Exception {
//        检查源文件是否存在
        if (!source.exists()) {
            throw new IOException("Source file or directory does not exist: " + source.getAbsolutePath());
        }
//        检查是否为目录
        if (source.isDirectory()) {
//            destination = new File(destination, source.getName());
            File destOutputFile = new File(destination, source.getName());
//            如果目标目录不存在就创建
            if (!destOutputFile.exists()) {
//                如果创建不成功就报错，创建成功就ok  destination.getAbsolutePath()  F:\it\yupi\yang-yuzigenerator
                destOutputFile.mkdirs();
            }

//            获取原目录下的所有文件和子目录
            File[] files = source.listFiles();
//            目录下有子文件才进行递归复制  F:\it\yupi\yang-yuzigenerator\yang-yuzigenerator-demo-properties\acm-template\.gitignore
            if (files != null && files.length > 0) {
                for (File file : files) {
                    // 在递归调用中创建新的目标文件对象
                    copyFileByRecursive(file, destOutputFile);
                }
            } else {
                throw new IOException("Source file or directory does not exist: " + source.getAbsolutePath());
            }
        } else {
//            如果为源文件
//            执行文件复制操作
            Path destPath = destination.toPath().resolve(source.getName());
            Files.copy(source.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
