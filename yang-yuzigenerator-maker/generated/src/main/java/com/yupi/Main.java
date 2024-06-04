package com.yupi;

import com.yupi.cli.CommandExecutor;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024-6-2
 */
public class Main {
    public static void main(String[] args) {
//        调用命令执行器
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
