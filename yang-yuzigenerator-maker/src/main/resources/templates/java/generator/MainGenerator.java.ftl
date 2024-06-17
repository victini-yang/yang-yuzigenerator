package ${basePackage}.generator;

import freemarker.template.TemplateException;
import ${basePackage}.model.DataModel;

import java.io.File;
import java.io.IOException;

<#macro generateFile indent fileInfo>
${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
<#if fileInfo.generateType == "static">
// 添加日志打印路径
System.out.println("Generated Input Path: " + inputPath);
${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
<#else>
${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
</#if>
</#macro>

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 * 动静结合
 */
public class MainGenerator {
    public static void doGenerate(DataModel model) throws TemplateException, IOException {

        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        String inputPath;
        String outputPath;

    <#-- 获取模型变量 -->
    <#list modelConfig.models as modelInfo>
        <#-- 有分组 -->
        <#if modelInfo.groupKey??>
        <#list modelInfo.models as subModelInfo>
        ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
        </#list>
        <#else>
        ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
    </#if>
</#list>

<#list fileConfig.files as fileInfo>
    <#if fileInfo.groupKey??>
    // groupKey = ${fileInfo.groupKey}
    <#if fileInfo.condition??>
    if(${fileInfo.condition}){
    <#list fileInfo.files as fileInfo>
    <@generateFile indent="        " fileInfo=fileInfo />
    </#list>
    }
    <#else>
    <#list fileInfo.files as fileInfo>
    <@generateFile fileInfo=fileInfo indent="        "/>
    </#list>
    </#if>
    <#else>
    <#if fileInfo.condition??>
    if(${fileInfo.condition}){
    <@generateFile indent="        "  fileInfo=fileInfo />
    }
    <#else>
    <@generateFile indent="        " fileInfo=fileInfo />
    </#if>
    </#if>
</#list>
    }
}
