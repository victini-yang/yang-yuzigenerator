package com.yupi.cli.example;

import java.util.concurrent.Callable;

import picocli.CommandLine;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 9:58
 * 引导用户交互式输入密码
 */
public class Login implements Callable<Integer> {

    @CommandLine.Option(names = {"-u", "--user"}, description = "User name")
    String user;

    @CommandLine.Option(names = {"-p", "--password"}, arity = "0..1",
            description = "Passphrase", interactive = true,prompt = "请输入密码：")
    String password;

    @CommandLine.Option(names = {"-cp", "--checkPassword"}, arity = "0..1",
             description = "check Password", interactive = true)
    String checkPassword;

    public Integer call() throws Exception {
        System.out.println("password =" + password);
        System.out.println("checkPassword =" + checkPassword);
        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new Login()).execute("-u", "user123", "-p","xxx", "-cp");
    }
}
