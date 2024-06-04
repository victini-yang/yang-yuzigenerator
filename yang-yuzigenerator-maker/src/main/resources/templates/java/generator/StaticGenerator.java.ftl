package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 * 静态文件生成器
 */
public class StaticGenerator {
    /**
     * 使用 Hutool 库实现文件复制操作，将输入目录的内容完整地复制到输出目录下。
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
