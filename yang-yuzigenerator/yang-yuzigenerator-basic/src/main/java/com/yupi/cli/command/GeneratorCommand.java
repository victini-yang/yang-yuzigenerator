package com.yupi.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.MainGenerator;
import com.yupi.model.MainTemplateConfig;
import freemarker.template.TemplateException;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 8:39
 */
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GeneratorCommand implements Callable {

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

    @Override
    public Object call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        MainGenerator.doGenerator(mainTemplateConfig);
        return 0;
    }


    @Override
    public String toString() {
        return "GeneratorCommand{" +
                "author='" + author + '\'' +
                ", outputText='" + outputText + '\'' +
                ", loop=" + loop +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public GeneratorCommand() {
    }

    public GeneratorCommand(String author, String outputText, boolean loop) {
        this.author = author;
        this.outputText = outputText;
        this.loop = loop;
    }
}
