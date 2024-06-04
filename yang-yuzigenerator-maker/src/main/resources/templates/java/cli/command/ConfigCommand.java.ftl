package ${basePackage}.cli.command;

import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.model.DataModel;
import picocli.CommandLine;

import java.lang.reflect.Field;

/**
 * @Author ${author}
 * @Version 1.0
 * @Date create in ${createTime}
 */
@CommandLine.Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{
    @Override
    public void run() {

//        第二种通过hutool
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for (Field field : fields) {
            System.out.println("字段类型：" + field.getType());
            System.out.println("字段名称：" + field.getName());
        }
    }
}
