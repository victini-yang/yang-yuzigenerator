package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import picocli.CommandLine;
import lombok.Data;

import java.util.concurrent.Callable;

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 */
@Data
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GeneratorCommand implements Callable {

<#list modelConfig.models as modelInfo>

    <#if modelInfo.description??>
        /**
        * ${modelInfo.description}
        */
    </#if>
    @CommandLine.Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}", </#if>"--${modelInfo.fieldName}"}, arity = "0..1", <#if modelInfo.description??>description = "${modelInfo.description}", </#if>interactive = true, echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#list>

    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        System.out.println("≈‰÷√–≈œ¢£∫" + dataModel);
        MainGenerator.doGenerator(dataModel);
        return 0;
    }

}
