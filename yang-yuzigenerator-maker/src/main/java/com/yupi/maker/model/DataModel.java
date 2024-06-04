package com.yupi.maker.model;

import lombok.Data;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/30 10:47
 * 我们需要生成这个文件，就需要判断有哪些信息，源文件以及usage哪个地方用到了
 */
@Data
public class DataModel {

    //我们先明确几个动态生成的需求:
    // 1.在代码开头增加作者 @Author 注释(增加代码)
    // 2.修改程序输出的信息提示(替换代码)
    // 3.将循环读取输入改为单次读取(可选代码)

    /**
     * 作者
     */
    private String author = "Victiny";
    /**
     * 输出信息
     */
    private String outputText = "输出信息";
    /**
     * 是否循环
     */
    private boolean loop = true;

}
