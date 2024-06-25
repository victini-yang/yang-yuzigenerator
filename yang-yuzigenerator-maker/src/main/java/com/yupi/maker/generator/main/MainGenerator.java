package com.yupi.maker.generator.main;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/5 15:40
 */
public class MainGenerator extends GenerateTemplate {

    //    已经有了模板类就可以让它继承模板类，通过方法重写覆盖实现不同操作，比如不要生成精简版程序
    @Override
    protected String buildDist(String outputPath, String shellOutputFilePath, String jarPath, String sourceCopyDestPath) {
        System.out.println("不要输出dist");
        return "";
    }
}
