package ${basePackage};

import ${basePackage}.cli.CommandExecutor;

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 */
public class Main {
    public static void main(String[] args) {
//        调用命令执行器
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
