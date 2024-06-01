package com.yupi.cli.pattern;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 14:50
 * 实际使用者
 */
public class Client {
    public static void main(String[] args) {
//        创建接受者对象
        Device tv = new Device("TV");
        Device stereo = new Device("Stereo");

//        创建具体命令对象，可以绑定不同设备
        TurnOnCommand turnOnCommand = new TurnOnCommand(tv);
        TurnOffCommand turnOffCommand = new TurnOffCommand(stereo);

//        创建调用者
        RemoteControl remote = new RemoteControl();

//        执行命令
        remote.setCommand(turnOnCommand);
        remote.pressButton();

        remote.setCommand(turnOffCommand);
        remote.pressButton();

    }
}
