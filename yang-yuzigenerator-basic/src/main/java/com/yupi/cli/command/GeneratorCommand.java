package com.yupi.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.file.MainGenerator;
import com.yupi.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 8:39
 */
@Data
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GeneratorCommand implements Callable<Integer> {

    /**
     * 作者
     */
    @CommandLine.Option(names = {"-a", "--author"}, description = "作者名称",
            arity = "0..1", interactive = true, echo = true)
    private String author = "Victiny";
    /**
     * 输出信息
     */
    @CommandLine.Option(names = {"-o", "--outputText"}, description = "输出文本",
            arity = "0..1", interactive = true, echo = true)
    private String outputText = "输出文本";
    /**
     * 是否循环
     */
    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否循环",
            arity = "0..1", interactive = true, echo = true)
    private boolean loop = true;

    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        System.out.println("配置信息：" + dataModel);
        MainGenerator.doGenerator(dataModel);
        return 0;
    }
}
