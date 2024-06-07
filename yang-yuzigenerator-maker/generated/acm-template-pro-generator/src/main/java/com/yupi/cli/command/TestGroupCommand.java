package com.yupi.cli.command;

import com.yupi.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/7 15:19
 */
@CommandLine.Command(name = "test", mixinStandardHelpOptions = true)
public class TestGroupCommand implements Runnable {
    @CommandLine.Option(names = {"--needGit"}, arity = "0..1", description = "是否生成·gitignore 文件", interactive = true, echo = true)
    private boolean needGit = true;

    @CommandLine.Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否生成循环", interactive = true, echo = true)
    private boolean loop = false;

    static DataModel.MainTemplate mainTemplate = new DataModel.MainTemplate();

    @Override
    public void run() {
        System.out.println(needGit);
        System.out.println(loop);
        if (true) {
            System.out.println("输入核心模板配置：");
            CommandLine commandLine = new CommandLine(MainTemplateCommand.class);
            commandLine.execute("-a", "-o");
            System.out.println(mainTemplate);
        }
    }

    @CommandLine.Command(name = "mainTemplate")
    @Data
    public static class MainTemplateCommand implements Runnable {
        @CommandLine.Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
        private String author = "Victiny";

        @CommandLine.Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
        private String outputText = "sum = ";

        @Override
        public void run() {
            mainTemplate.author = author;
            mainTemplate.outputText = outputText;
        }
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(TestGroupCommand.class);
        commandLine.execute("-l");
//        commandLine.execute("--help");
    }
}
