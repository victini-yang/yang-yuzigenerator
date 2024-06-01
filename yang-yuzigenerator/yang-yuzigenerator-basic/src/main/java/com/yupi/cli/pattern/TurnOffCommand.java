package com.yupi.cli.pattern;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 14:32
 * 相当于遥控器的某个操作按钮
 */
public class TurnOffCommand implements Command{

    private Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }
}
