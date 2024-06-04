package com.yupi;

import com.yupi.cli.CommandExecutor;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 10:27
 */
public class Main {
    public static void main(String[] args) {
        args = new String[] {"generate","-l","-a","-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
//        调用命令执行器
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
