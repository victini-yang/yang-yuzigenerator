package ${basePackage}.model;

import lombok.Data;

/**
* @Author ${author}
* 数据模型
*/
@Data
public class DataModel {

<#list modelConfig.models as modelInfo>

    <#if modelInfo.description??>
    /**
     * ${modelInfo.description}
     */
    </#if>

    private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> =
    ${modelInfo.defaultValue?c}</#if>;

</#list>

}