package com.yupi.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.yupi.model.MainTemplateConfig;
import picocli.CommandLine;

import java.lang.reflect.Field;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/1 8:38
 */
@CommandLine.Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{
    @Override
    public void run() {
//        第一种方式反射获得类型字段
//        Field[] declaredFields = MainTemplateConfig.class.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            System.out.println("Field: " + declaredField.getName());
//        }
//        第二种通过hutool
        Field[] fields = ReflectUtil.getFields(MainTemplateConfig.class);
        for (Field field : fields) {
            System.out.println("字段类型：" + field.getType());
            System.out.println("字段名称：" + field.getName());
        }
    }
}
