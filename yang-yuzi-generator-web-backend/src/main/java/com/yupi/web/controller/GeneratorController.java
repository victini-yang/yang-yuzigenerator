package com.yupi.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.yupi.maker.generator.main.GenerateTemplate;
import com.yupi.maker.generator.main.ZipGenerator;
import com.yupi.maker.meta.MetaValidator;
import com.yupi.web.annotation.AuthCheck;
import com.yupi.web.common.BaseResponse;
import com.yupi.web.common.DeleteRequest;
import com.yupi.web.common.ErrorCode;
import com.yupi.web.common.ResultUtils;
import com.yupi.web.constant.UserConstant;
import com.yupi.web.exception.BusinessException;
import com.yupi.web.exception.ThrowUtils;
import com.yupi.web.manager.CosManager;
import com.yupi.maker.meta.Meta;
import com.yupi.web.model.dto.generator.*;
import com.yupi.web.model.entity.Generator;
import com.yupi.web.model.entity.User;
import com.yupi.web.model.vo.GeneratorVO;
import com.yupi.web.service.GeneratorService;
import com.yupi.web.service.UserService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/generator")
@Slf4j
public class GeneratorController {

    @Resource
    private GeneratorService generatorService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    // region 增删改查

    /**
     * 创建
     *
     * @param generatorAddRequest
     * @param request
     */
    @PostMapping("/add")
    public BaseResponse<Long> addGenerator(@RequestBody GeneratorAddRequest generatorAddRequest, HttpServletRequest request) {
        if (generatorAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorAddRequest, generator);
        List<String> tags = generatorAddRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));
        Meta.FileConfig fileConfig = generatorAddRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        Meta.ModelConfig modelConfig = generatorAddRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        generatorService.validGenerator(generator, true);
        User loginUser = userService.getLoginUser(request);
        generator.setUserId(loginUser.getId());
        generator.setStatus(0);
        boolean result = generatorService.save(generator);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newGeneratorId = generator.getId();
        return ResultUtils.success(newGeneratorId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteGenerator(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldGenerator.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = generatorService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param generatorUpdateRequest
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateGenerator(@RequestBody GeneratorUpdateRequest generatorUpdateRequest) {
        if (generatorUpdateRequest == null || generatorUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorUpdateRequest, generator);
        List<String> tags = generatorUpdateRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));
        Meta.FileConfig fileConfig = generatorUpdateRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        Meta.ModelConfig modelConfig = generatorUpdateRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        generatorService.validGenerator(generator, false);
        long id = generatorUpdateRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     */
    @GetMapping("/get/vo")
    public BaseResponse<GeneratorVO> getGeneratorVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(generatorService.getGeneratorVO(generator, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param generatorQueryRequest
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Generator>> listGeneratorByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        return ResultUtils.success(generatorPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param generatorQueryRequest
     * @param request
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                 HttpServletRequest request) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        return ResultUtils.success(generatorService.getGeneratorVOPage(generatorPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param generatorQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listMyGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                   HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        generatorQueryRequest.setUserId(loginUser.getId());
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        return ResultUtils.success(generatorService.getGeneratorVOPage(generatorPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param generatorEditRequest
     * @param request
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editGenerator(@RequestBody GeneratorEditRequest generatorEditRequest, HttpServletRequest request) {
        if (generatorEditRequest == null || generatorEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorEditRequest, generator);
        List<String> tags = generatorEditRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));
        Meta.FileConfig fileConfig = generatorEditRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        Meta.ModelConfig modelConfig = generatorEditRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        generatorService.validGenerator(generator, false);
        User loginUser = userService.getLoginUser(request);
        long id = generatorEditRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldGenerator.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据id下载
     *
     * @param id
     * @param response
     * @param request
     */
    @GetMapping("/download")
    public void downloadGeneratorById(long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String filepath = generator.getDistPath();
        if (StrUtil.isBlank(filepath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产物包不存在");
        }

//        追踪事件，记录用户下载信息，防止别人恶意上传下载
        log.info("用户{} 下载了 {}", loginUser, filepath);
//        下载
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
//            处理到下载到的流
//            设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
//            写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInput != null) {
                // 用完流之后一定要调用 close()
                cosObjectInput.close();
            }
        }
    }

    /**
     * 使用代码生成器
     *
     * @param generatorUseRequest
     * @param response
     * @param request
     */
    @PostMapping("/use")
    public void useGenerator(@RequestBody GeneratorUseRequest generatorUseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        获取用户输入的请求参数
        Long id = generatorUseRequest.getId();
        Map<String, Object> dataModel = generatorUseRequest.getDataModel();

//        需要用户登录
        User loginUser = userService.getLoginUser(request);
        log.info("userId = {} 使用了生成器 id = {}", loginUser.getId(), id);
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

//        生成器的储存路径
        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产物包不存在");
        }

//        定义独立的工作空间
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/use/%s", projectPath, id);
        String zipFilePath = tempDirPath + "/dist.zip";

        if (!FileUtil.exist(zipFilePath)) {
            FileUtil.touch(zipFilePath);
        }

//        从对象储存下载生成器的压缩包
        try {
            cosManager.download(distPath, zipFilePath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成器下载失败");
        }

//        解压压缩包，得到脚本文件
        File unzipDistDir = ZipUtil.unzip(zipFilePath);

//        将用户输入的参数写到json文件中
        String dataModelFilePath = tempDirPath + "/dataModel.json";
        String jsonStr = JSONUtil.toJsonStr(dataModel);
        FileUtil.writeUtf8String(jsonStr, dataModelFilePath);

//        执行脚本
        File scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
                .stream()
                .filter(file -> file.isFile()
                        && "generator.bat".equals(file.getName()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
//        添加可执行权限
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            System.out.println("Running on Windows, skipping setting POSIX file permissions.");
        } else {
            System.out.println("Running on Unix/Linux, setting POSIX file permissions.");
            try {
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
                Files.setPosixFilePermissions(scriptFile.toPath(), permissions);
                System.out.println("POSIX file permissions set successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        调用脚本文件
//        1.构造命令
        File scriptDir = scriptFile.getParentFile();
        String scriptAbsolutePath = scriptFile.getAbsolutePath().replace("\\", "/");
        String[] commands = new String[]{scriptAbsolutePath, "json-generate", "--file=" + dataModelFilePath};
//        2.拆分
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(scriptDir);
        try {
//        3.执行
            Process process = processBuilder.start();
//        4.读取命令输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String Line;
            while ((Line = reader.readLine()) != null) {
                System.out.println(Line);
            }
//        5.等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("命令执行结束" + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "执行生成器脚本错误");
        }


//        压缩得到的生成结果，返回给前端
//        1.定义解压路径、压缩包路径
        String generatedPath = scriptDir.getAbsolutePath() + "/generated";
        String resultPath = tempDirPath + "/result.zip";
//        2.压缩
        File resultFile = ZipUtil.zip(generatedPath, resultPath);
//        3.设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
//        4.将文件返回给响应流
        Files.copy(resultFile.toPath(), response.getOutputStream());
//        5.清理文件，异步清理，不用等待清理完毕就返回
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }


    /**
     * 制作代码生成器
     *
     * @param generatorMakeRequest
     * @param response
     * @throws IOException
     */
    @PostMapping("/make")
    public void makeGenerator(@RequestBody GeneratorMakeRequest generatorMakeRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        1.输入参数
        Meta meta = generatorMakeRequest.getMeta();
        String zipFilePath = generatorMakeRequest.getZipFilePath();

//        需要用户登录
        userService.getLoginUser(request);
        log.info("userId = {} 使用了生成器", userService.getLoginUser(request).getId());

//        2.创建独立的工作空间，下载压缩包到本地
        String projectPath = System.getProperty("user.dir");
        String id = IdUtil.getSnowflakeNextId() + RandomUtil.randomString(6);
        String tempDirPath = String.format("%s/.temp/make/%s", projectPath, id);
        String localZipFilePath = tempDirPath + "/project.zip";

        if (!FileUtil.exist(localZipFilePath)) {
            FileUtil.touch(localZipFilePath);
        }

        try {
            cosManager.download(zipFilePath, localZipFilePath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩包下载失败");
        }
//        3.解压，得到脚本文件
        File unzipDistDir = ZipUtil.unzip(localZipFilePath);

//        4.构造meta对象和生成器的输出路径
        String sourceRootPath = unzipDistDir.getAbsolutePath();
        meta.getFileConfig().setSourceRootPath(sourceRootPath);
//        校验和处理默认值
        MetaValidator.doValidAndFill(meta);
        String outputPath = tempDirPath + "/generated/" + meta.getName();

//        5.调用maker方法制作生成器
        GenerateTemplate generateTemplate = new ZipGenerator();
        try {
            generateTemplate.doGenerate(meta, outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "制作失败");
        }

//        6.下载制作好的生成器压缩包
        String suffix = "-dist.zip";
        String zipFileName = meta.getName() + suffix;
//        生成器压缩包的绝对路径
        String distZipFilePath = outputPath + suffix;
//        设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
        Files.copy(Paths.get(distZipFilePath), response.getOutputStream());

//        7.清理工作空间的文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });

    }
}
