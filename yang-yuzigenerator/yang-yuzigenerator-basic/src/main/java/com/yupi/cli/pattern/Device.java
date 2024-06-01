package com.yupi.cli.pattern;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/31 14:32
 * 相当于被遥控的设备 写了有哪些功能
 */

public class Device {
    private String name;

    public Device(String name) {
        this.name = name;
    }

    public void turnOn() {
        System.out.println(name + " 设备打开 ");
    }

    public void turnOff() {
        System.out.println(name + " 设备关闭 ");
    }
}