package com.yupi.cli.pattern;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 14:46
 * 调用者
 */
public class RemoteControl {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton(){
        command.execute();
    }
}
