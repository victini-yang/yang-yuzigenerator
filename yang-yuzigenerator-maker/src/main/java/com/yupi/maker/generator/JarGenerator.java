package com.yupi.maker.generator;

import java.io.*;
import java.util.Map;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/3 9:35
 * 执行maven打包命令
 */
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {

        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String os = System.getProperty("os.name").toLowerCase();
        String mavenCommand = os.contains("win") ? "mvn.cmd clean package -DskipTests=true" :
                "mvn clean package -DskipTests=true";

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir.replace("\\","/")));
        Map<String, String> environment = processBuilder.environment();
        System.out.println("environment= " + environment);

        // 启动进程
        Process process = processBuilder.start();

        // 读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码：" + exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("F:\\it\\yupi\\yang-yuzigenerator\\yang-yuzigenerator-maker\\generated\\acm-template-pro-generator");
    }
}