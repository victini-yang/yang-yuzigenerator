package com.yupi.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.MainGenerator;
import com.yupi.model.DataModel;
import picocli.CommandLine;
import lombok.Data;

import java.util.concurrent.Callable;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024-6-2
 */
@Data
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GeneratorCommand implements Callable<Integer> {


        /**
        * 是否生成循环
        */
    @CommandLine.Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否生成循环", interactive = true, echo = true)
    private boolean loop = false;

        /**
        * 作者注释
        */
    @CommandLine.Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
    private String author = "Victiny";

        /**
        * 输出信息
        */
    @CommandLine.Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
    private String outputText = "sum = ";

    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        System.out.println("������Ϣ��" + dataModel);
        MainGenerator.doGenerator(dataModel);
        return 0;
    }
}