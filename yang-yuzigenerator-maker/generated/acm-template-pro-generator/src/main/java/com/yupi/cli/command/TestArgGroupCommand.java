package com.yupi.cli.command;

import com.yupi.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/7 15:01
 */
@CommandLine.Command(name = "test", mixinStandardHelpOptions = true)
public class TestArgGroupCommand implements Runnable {
    @CommandLine.Option(names = {"--needGit"}, arity = "0..1", description = "是否生成·gitignore 文件", interactive = true, echo = true)
    private boolean needGit = true;

    @CommandLine.Option(names = {"-1", "--loop"}, arity = "0..1", description = "是否生成循环", interactive = true, echo = true)
    private boolean loop = false;

    @CommandLine.ArgGroup(exclusive = false, heading = "核心模板%n")
    DataModel.MainTemplate mainTemplate;

    @Override
    public void run() {
        System.out.println(needGit);
        System.out.println(loop);
        System.out.println(mainTemplate);
    }

    @Data
    static class MainTemplate {
        @CommandLine.Option(names = {"-mainTemplate.a", "--mainTemplate.author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
        private String author = "Victiny";

        @CommandLine.Option(names = {" - mainTemplate.o", "--mainTemplate.outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
        private String outputText = "sum= ";

        public static void main(String[] args) {
            CommandLine commandLine = new CommandLine(TestArgGroupCommand.class);
            commandLine.execute(" - l", "-mainTemplate.a", "--mainTemplate.outputText");
//            commandLine.execute("--help");
        }
    }
}
