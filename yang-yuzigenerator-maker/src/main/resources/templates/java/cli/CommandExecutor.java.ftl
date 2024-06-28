package ${basePackage}.cli;

import ${basePackage}.cli.command.GenerateCommand;
import ${basePackage}.cli.command.ListCommand;
import ${basePackage}.cli.command.ConfigCommand;
import ${basePackage}.cli.command.JsonGeneratorCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 * 调用子命令 命令执行器
 */
@Command(name = "${name}", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {

    private final CommandLine commandLine;

//    执行顺序
//    静态代码块 (Static Block)
//    实例初始化代码块 (Instance Initialization Block)
//    构造函数 (Constructor)
//    普通方法 (Method)

    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand())
                .addSubcommand(new JsonGeneratorCommand());
    }

    @Override
    public void run() {
    // 不输入子命令时，给出友好提示
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }

/**
* 执行命令
*
* @param args
* @return
*/
    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }
}