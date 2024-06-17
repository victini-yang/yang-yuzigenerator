package com.yupi.maker.template;

import cn.hutool.core.util.StrUtil;
import com.yupi.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
        * @Date create in 2024/6/13 9:55
        * 模板制作工具
 */
public class TemplateMakerUtils {

    /**
     * 从未分组文件中移除组内的同名文件
     *
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 先获取到所有分组
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        // 获取所有分组内的文件列表的
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());

//        将组内文件的输入路径变成集合， 获取inputPath字段和value 收集为set集合 去重
        Set<String> fileInputPathSet = groupInnerFileInfoList.stream()
                .map(Meta.FileConfig.FileInfo::getInputPath)
                .collect(Collectors.toSet());

//        移除所有在集合内的外层文件，只要文件名在集合中，就是已经去过重复的文件，不包含就是要去重复的
        return fileInfoList.stream()
                .filter(fileInfo -> !fileInputPathSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());
    }
}
