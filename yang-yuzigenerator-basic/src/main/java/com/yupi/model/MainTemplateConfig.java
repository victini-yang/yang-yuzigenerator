package com.yupi.model;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/30 10:47
 */
public class MainTemplateConfig {

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

    @Override
    public String toString() {
        return "MainTemplateConfig{" +
                "author='" + author + '\'' +
                ", outputText='" + outputText + '\'' +
                ", loop=" + loop +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public MainTemplateConfig() {
    }

    public MainTemplateConfig(String author, String outputText, boolean loop) {
        this.author = author;
        this.outputText = outputText;
        this.loop = loop;
    }
}
