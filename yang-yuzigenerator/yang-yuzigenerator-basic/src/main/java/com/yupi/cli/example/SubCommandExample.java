package com.yupi.cli.example;

import org.apache.commons.collections4.sequence.DeleteCommand;
import picocli.CommandLine;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 10:45
 * 在主命令添加子命令，会对已选参数、方法执行
 */

@CommandLine.Command(name = "main", mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable {
    @Override
    public void run() {
        System.out.println("执行主命令");
    }

    @CommandLine.Command(name = "add", description = "增加", mixinStandardHelpOptions = true)
    static class AddCommand implements Runnable {
        public void run() {
            System.out.println("执行增加命令");
        }
    }

    @CommandLine.Command(name = "delete", description = "删除",mixinStandardHelpOptions = true)
    static class DeleteCommand implements Runnable {
        public void run() {
            System.out.println("执行删除命令");
        }
    }

    @CommandLine.Command(name = "query", description = "查洵", mixinStandardHelpOptions = true)
    static class QueryCommand implements Runnable {
        public void run() {
            System.out.println("执行查询命令");
        }
    }

    @CommandLine.Command(name = "update", description = "修改", mixinStandardHelpOptions = true)
    static class UpdateCommand implements Runnable {
        public void run() {
            System.out.println("执行修改命令");
        }
    }

    public static void main(String[] args) {
        // 执行主命令
//        String[] myArgs = new String[]{};
// 查看主命令的帮助手册
//                String[] myArgs = new String[]{"--help"};
// 执行增加命令
//                String[] myArgs = new String[]{"add"};
//执行增加命令的帮助手册
//                String[] myArgs = new String[]{"add", "--help"};
// 执行不存在的命令，会报错
                String[] myArgs = new String[]{"update"};
        int exitCode = new CommandLine(new SubCommandExample())
                .addSubcommand(new AddCommand())
                .addSubcommand(new DeleteCommand())
                .addSubcommand(new QueryCommand())
                .addSubcommand(new UpdateCommand())
                .execute(myArgs);
        System.exit(exitCode);
    }
}
