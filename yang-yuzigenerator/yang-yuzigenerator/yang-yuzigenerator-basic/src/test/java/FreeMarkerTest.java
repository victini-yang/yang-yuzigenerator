import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/5/30 9:47
 * src/test/java/FreeMarkerTest.java
 */

public class FreeMarkerTest {
//    全局的配置对象、指定模板文件路径、字符集、
    @Test
    public void test() throws IOException, TemplateException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        //指定模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
//        设置指定不用数字逗号分隔符
        configuration.setNumberFormat("0.######");
//        创建模板对象,加载指定模板
        Template template = configuration.getTemplate("myweb.html.ftl");

//        创建数据模型
        HashMap<String , Object> dataModel = new HashMap<>();
        dataModel.put("currentyear",2024);

        List<Map<String,Object>> menuItems = new ArrayList<>();
        HashMap<String, Object> menuItem1 = new HashMap<>();
        menuItem1.put("url","https://codefather.cn");
        menuItem1.put("label","编程导航");

        HashMap<String , Object> menuItem2 = new HashMap<>();
        menuItem2.put("url","https://laoyupi.com");
        menuItem2.put("label","老鱼简历");

        menuItems.add(menuItem1);
        menuItems.add(menuItem2);

        dataModel.put("menuItems",menuItems);
//        指定生成的文件  yang-yuzigenerator-basic目录下
        Writer out = new FileWriter("myweb.html");

//        生成文件
        template.process(dataModel,out);
        out.close();
    }
}
