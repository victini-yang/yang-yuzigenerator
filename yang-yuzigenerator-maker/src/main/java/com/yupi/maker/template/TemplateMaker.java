package com.yupi.maker.template;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.maker.meta.Meta;
import com.yupi.maker.meta.enums.FileGenerateTypeEnum;
import com.yupi.maker.meta.enums.FileTypeEnum;
import com.yupi.maker.template.enums.FileFilterRangeEnum;
import com.yupi.maker.template.enums.FileFilterRuleEnum;
import com.yupi.maker.template.model.FileFilterConfig;
import com.yupi.maker.template.model.TemplateMakerFileConfig;
import com.yupi.maker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.util.stream.Collectors;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/8 8:26
 */
public class TemplateMaker {
    /**
     * 制作模板
     *
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */

    private static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id) {

//        没有id 就生成
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }

//        工作空间隔离
//        复制目录
        id = IdUtil.getSnowflakeNextId();
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        // 是否为首次制作模板
        // 目录不存在，则是首次制作
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(tempDirPath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

//        生成MainTemplate.java.ftl模板文件，以及在根目录下生成meta.json元信息文件。
//        要挖坑的项目根目录
        String sourceRootPath = templatePath + File.separator +
                FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
//        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");

        List<Meta.FileConfigDTO.FileInfo> newFileInfoList = new ArrayList<>();

        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : templateMakerFileConfig.getFiles()) {
            //        遍历得到文件
            String inputFilePath = fileInfoConfig.getPath();

            // 如果填的是相对路径，要改为绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

//            传入绝对路径
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFileFilterConfigList());
//            得到过滤后的文件列表
            for (File file : fileList) {
                Meta.FileConfigDTO.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

//        处理文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            Meta.FileConfigDTO.FileInfo groupFileInfo = createGroupFileInfo(fileGroupConfig, newFileInfoList);
//            变成列表对象、把文件列表添加进去
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

        // 处理模型信息
        List<Meta.ModelConfigDTO.ModelInfo> newModelInfoList = processModelInfo(templateMakerModelConfig);


//        生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

//        更新文件信息
//        已有Meta文件，不是第一次制作，则在Meta基础上修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

//            已有json文件，转成对象修改 Json->String->对象
//            1.追加配置参数 ，放到列表中
            List<Meta.FileConfigDTO.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);

            List<Meta.ModelConfigDTO.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);

//            配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));

        } else {
//            1.构造配置参数
//        fileConfig对象 设置FileConfig、sourceRootPath、fileInfoList
            Meta.FileConfigDTO fileConfig = new Meta.FileConfigDTO();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfigDTO.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
//        fileInfo对象
            fileInfoList.addAll(newFileInfoList);
//        modelConfig对象 设置modelInfoList
            Meta.ModelConfigDTO modelConfig = new Meta.ModelConfigDTO();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfigDTO.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);

        }

//        输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    private static List<Meta.ModelConfigDTO.ModelInfo> processModelInfo(TemplateMakerModelConfig templateMakerModelConfig) {
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        // - 转换为配置接受的 ModelInfo 对象
        List<Meta.ModelConfigDTO.ModelInfo> inputModelInfoList = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfigDTO.ModelInfo modelInfo = new Meta.ModelConfigDTO.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());

        // - 本次新增的模型配置列表
        List<Meta.ModelConfigDTO.ModelInfo> newModelInfoList = new ArrayList<>();

        // - 如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            Meta.ModelConfigDTO.ModelInfo groupModelInfo = new Meta.ModelConfigDTO.ModelInfo();
            groupModelInfo.setGroupKey(modelGroupConfig.getGroupKey());
            groupModelInfo.setGroupName(modelGroupConfig.getGroupName());
            groupModelInfo.setCondition(modelGroupConfig.getCondition());

            // 模型全放到一个分组内
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        } else {
            // 不分组，添加所有的模型信息到列表
            newModelInfoList.addAll(inputModelInfoList);
        }
        return newModelInfoList;
    }

    private static Meta.FileConfigDTO.FileInfo createGroupFileInfo(TemplateMakerFileConfig.FileGroupConfig fileGroupConfig, List<Meta.FileConfigDTO.FileInfo> newFileInfoList) {
        // 新增分组配置
        Meta.FileConfigDTO.FileInfo groupFileInfo = new Meta.FileConfigDTO.FileInfo();
        groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
        groupFileInfo.setCondition(fileGroupConfig.getCondition());
        groupFileInfo.setGroupKey(fileGroupConfig.getGroupKey());
        groupFileInfo.setGroupName(fileGroupConfig.getGroupName());
//            文件全放到一个分组内
        groupFileInfo.setFiles(newFileInfoList);
        return groupFileInfo;
    }

    /**
     * 制作模板文件
     *
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfigDTO.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile) {
        //        要挖坑的文件  遍历文件得到的都是绝对路径，这里要将相对路径转成绝对路径
        String fileInputPath = inputFile.getAbsolutePath().replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";


//        二、使用字符串替换，生成模板文件  sourceRootPath存在频繁更换的可能性
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        String fileOutputAbsolutePath = inputFile.getAbsolutePath() + ".ftl";

        String fileContent;

//        如果已有模板文件，表示不是第一次制作，则在原有模板的基础上再挖坑
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

// 支持多个模型：对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            // 不是分组\
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                // 是分组
                String groupKey = modelGroupConfig.getGroupKey();
                // 注意挖坑要多一个层级template.author
                replacement = String.format("${%s.%s}", groupKey, modelInfoConfig.getFieldName());
            }
            // 多次替换
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        // 文件配置信息
        Meta.FileConfigDTO.FileInfo fileInfo = new Meta.FileConfigDTO.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());

        // 和原文件一致，没有挖坑，则为静态生成
        if (newFileContent.equals(fileContent)) {
            // 输出路径 = 输入路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        } else {
            // 生成模板文件
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }


    public static void main(String[] args) {

        //        输入信息、输入项目基本信息
        //        构造配置参数对象
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        //        指定原始项目路径
        String projectPath = System.getProperty("user.dir");
//        要挖坑的项目根目录
        String originProjectPath = new File(projectPath).getParent() + File.separator + "yang-yuzigenerator-demo-project/springboot-init";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";
        String fileInputPath2 = "src/main/resources/application.yml";
        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);
//        替换变量
        String searchStr = "BaseResponse";

//        文件过滤配置
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFileFilterConfigList(fileFilterConfigList);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));


//        分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1799318718086172672L);
        System.out.println(id);



    }

    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfigDTO.FileInfo> distinctFiles(List<Meta.FileConfigDTO.FileInfo> fileInfoList) {


        //1. 将所有文件配置（fileInfo）分为有分组的和无分组的

//        先处理有分组的文件,通过groupKey判断是否不为空 filter会保留不为空的
        //{"groupKey":"a"，files:[1,2]}，{"groupKey":"a"，files:[2，3]}，{"groupKey":"b"，files:[4，5]}
        //{"groupKey":"a", files:[[1, 2], [2,3]]}, {"groupKey":"b",files:[[4, 5]]}
//        以组为单位划分
        Map<String, List<Meta.FileConfigDTO.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfigDTO.FileInfo::getGroupKey)
                );
//        同组内配置合并  ，拆分最小单元然后去重
        // {"groupKey":"a",files:[[1,2],[2,3]]}
        // {"groupKey":"a",files:[[1,2,3]]}
//        定义合并后的对象map 原本是List<Meta.FileConfigDTO.FileInfo>合并后就是一个FileInfo了
        Map<String , Meta.FileConfigDTO.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfigDTO.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {

            //2．对于有分组的文件配置，如果有相同的分组，同分组内的文件进行合并（merge），不同分组可同时保留
            List<Meta.FileConfigDTO.FileInfo> tempFileInfoList = entry.getValue();
//            对多组文件进行合并files:[[1,2],[2,3]]
            List<Meta.FileConfigDTO.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
//                    拍平 {"groupKey":"a",files:[[1,2,2,3]]}
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
//                    去重
                    .collect(Collectors.toMap(Meta.FileConfigDTO.FileInfo::getInputPath, o -> o,(e,r) -> r)
                    ).values());

//            同组的配置信息的覆盖 新group 覆盖老
            Meta.FileConfigDTO.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
//            设置新的文件列表
            newFileInfo.setFiles(newFileInfoList);
//            得到合并后的对象就往map放
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);

        }

        //3．创建新的文件配置列表（结果列表），先将合并后的分组添加到结果列表
        List<Meta.FileConfigDTO.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        //4. 再将无分组的文件配置列表添加到结果列表 就是有分组的取反
        List<Meta.FileConfigDTO.FileInfo> noGroupFileInfoList = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
    //        列表转成map InputPath:fileInfo对象，文件信息不做处理作为value，遇到重复对象新值老值就替换为新值 r
                        Collectors.toMap(Meta.FileConfigDTO.FileInfo::getInputPath , o -> o ,(e,r) -> r)
                ).values()));
        return resultList;
//        先对分组的进行处理 对同组内的对象进行合并 再处理没有分组的 依次将有分组和没有分组的添加到结果列表
    }

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfigDTO.ModelInfo> distinctModels(List<Meta.ModelConfigDTO.ModelInfo> modelInfoList) {
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfigDTO.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfigDTO.ModelInfo::getGroupKey)
                );


        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfigDTO.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfigDTO.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfigDTO.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfigDTO.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfigDTO.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfigDTO.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfigDTO.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfigDTO.ModelInfo> noGroupModelInfoList = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList
                .stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfigDTO.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }
}
