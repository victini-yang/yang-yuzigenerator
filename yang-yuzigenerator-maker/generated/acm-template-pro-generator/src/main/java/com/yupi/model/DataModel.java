package com.yupi.model;

import lombok.Data;


/**
* @Author Victiny
* 数据模型
*/
@Data
public class DataModel {

    /**
     * 是否生成.gitignore文件
     */
    public boolean needGit;
    /**
     * 是否生成循环
     */
    public boolean loop;
    /**
     * 核心模板
     */
    public MainTemplate mainTemplate = new MainTemplate();

    /**
    * 用于生成核心模板文件
    */
    @Data
    public static class MainTemplate {
        /**
         * 作者注释
         */
        public String author;
        /**
         * 输出信息
         */
        public String outputText;
    }

}