package com.yupi.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.yupi.maker.template.enums.FileFilterRangeEnum;
import com.yupi.maker.template.enums.FileFilterRuleEnum;
import com.yupi.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/9 21:22
 * 文件过滤
 */
public class FileFilter {

    /**
     * 对某个文件或者目录进行过滤，返回文件列表
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath,List<FileFilterConfig> fileFilterConfigList){
//        根据路径获取所有文件
        List<File> fileList = FileUtil.loopFiles(filePath);
//        遍历
        return fileList.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList , file))
                .collect(Collectors.toList());
    }

    /**
     * 单个文件过滤
     * 读取文件过滤配置，通过for循环依次经过所有的过滤器，再根据指定的规则去过滤
     * @param fileFilterConfigList
     * @param file
     * @return
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
//        根据文件名称和文件内容进行过滤
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

//        所有过滤器校验结束后的结果
        boolean result = true;
//        如果没有过滤机制就返回true
        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }
//        有过滤机制就依次取出
        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
//            依次取出range、rule、value
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
//            取出value、是否为空
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }

//            要过滤的原内容 提前将content变量提出 再将其和各个规则匹配
            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

//            取出rule、是否为空
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }
            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
//            有一个不满足就返回
            if (!result) {
                return false;
            }
        }

//        都满足
        return true;
    }
}
