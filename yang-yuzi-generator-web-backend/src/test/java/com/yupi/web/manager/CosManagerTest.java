package com.yupi.web.manager;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Author Victiny
 * @Version 1.0
 * @Date create in 2024/7/6 16:29
 */
@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    void deleteObject() {
        try {
            cosManager.deleteObject("/generator_make_template/1/L2kefqI5-src.zip");
        } catch (Exception e) {
            // 处理异常或者打印异常信息
            e.printStackTrace();
            System.out.println("Failed to delete object: " + e.getMessage());
        }
    }

    @Test
    void deleteObjects() {
        cosManager.deleteObjects(Arrays.asList("generator_make_template/1/M4Lo0TBn-src.zip","generator_make_template/1/XntzQZIv-src.zip"));
    }

    @Test
    void deleteDir() {
        cosManager.deleteDir("/generator_make_template/");
    }
}