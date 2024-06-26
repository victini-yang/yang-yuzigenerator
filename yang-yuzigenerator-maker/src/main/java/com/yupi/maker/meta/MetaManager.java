package com.yupi.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import javax.swing.plaf.SeparatorUI;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/6/2 10:41
 * 业务层、帮我们读取json文件
 */
public class MetaManager {

//    可以使用单例模式 + 双检索，初始化的只读取一次json信息，创建对象，可以更加节省
//    volatile确保多线程环境下内存可见性，保证meta一旦被修改了，其他线程也能看见
    private static volatile Meta meta;

    private MetaManager() {
        // 私有构造函数，防止外部实例化
    }

    public static Meta getMetaObject() {
        if (meta == null) {
            //a b c
            synchronized (MetaManager.class) {
                //a
                if (meta == null) {
                    //a初始化
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
        //        读取json文件
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
//        String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");
        System.out.println(metaJson);
//        转成对象
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        String jsonStr = JSONUtil.toJsonStr(newMeta);
//        替换
        jsonStr = jsonStr.replace("\\\\","/");
//        转成对象
        newMeta = JSONUtil.toBean(jsonStr, Meta.class);

//        校验处理默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }
}
