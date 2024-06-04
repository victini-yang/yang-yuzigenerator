package com.yupi.maker.generator;

import cn.hutool.core.io.FileUtil; // 导入 hutool 文件工具类

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/3 12:27
 */
public class ScriptGenerator {
    // 生成脚本的方法，接受输出路径和 JAR 包路径作为参数
    public static void doGenerate(String outputPath, String jarPath) {

        // 为 Linux 脚本生成 StringBuilder
        StringBuilder sb = new StringBuilder();

        // 添加 Linux 脚本的头部，拼接字符串，写到jarPath
        //        #!bin/bash
        //        java -jar target/yang-yuzigenerator-basic-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"

        sb.append("#!/bin/bash").append("\n"); // 添加脚本解释器的声明行
        sb.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n"); // 添加运行 JAR 包的命令

        // 将生成的脚本内容写入指定文件（适用于 Linux）
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8), outputPath);

        // 添加可执行权限
        try {
            // 设置权限为 rwxrwxrwx（所有人都有读、写、执行权限）
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        } catch (Exception e) {
            // 异常处理（这里没有实际处理异常）
        }

        // 为 Windows 脚本生成新的 StringBuilder
        sb = new StringBuilder();

        // 添加 Windows 脚本的头部
        //        @echo off
        //        java -jar target/yang-yuzigenerator-1.0-SNAPSHOT-jar-with-dependencies.jar %*
        sb.append("@echo off").append("\n"); // 关闭批处理文件的命令回显
        sb.append(String.format("java -jar %s %%*", jarPath)).append("\n"); // 添加运行 JAR 包的命令

        // 将生成的脚本内容写入指定文件（适用于 Windows）
        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8), outputPath + ".bat");
    }

    public static void main(String[] args) {
        String outputPath = System.getProperty("user.dir") + File.separator + "generator";
        doGenerate(outputPath, "");
    }
}
