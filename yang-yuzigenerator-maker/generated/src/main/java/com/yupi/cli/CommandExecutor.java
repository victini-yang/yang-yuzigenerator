package com.yupi.cli;

import com.yupi.cli.command.ConfigCommand;
import com.yupi.cli.command.GeneratorCommand;
import com.yupi.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024-6-2
 * 调用子命令 命令执行器
 */
@Command(name = "acm-template-pro-generator",mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{

    private final CommandLine commandLine;

//    执行顺序
//    静态代码块 (Static Block)
//    实例初始化代码块 (Instance Initialization Block)
//    构造函数 (Constructor)
//    普通方法 (Method)
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GeneratorCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }


    @Override
    public void run() {
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }

    public Integer doExecute(String[] args){
        return commandLine.execute(args);
    }
}
