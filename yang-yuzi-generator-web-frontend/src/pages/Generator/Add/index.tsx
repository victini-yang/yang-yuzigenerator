import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import {
  addGeneratorUsingPost,
  editGeneratorUsingPost,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import { useSearchParams } from '@@/exports';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
  ProCard,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { ProFormItem } from '@ant-design/pro-form';
import { history } from '@umijs/max';
import { message, UploadFile } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 生成器创建页面
 * @constructor
 */
const GeneratorAddPage: React.FC = () => {
  const formRef = useRef<ProFormInstance>();

  const [searchParams] = useSearchParams();
  // 要修改生成器的id
  const id = searchParams.get('id');
  // 要修改的旧数据 编辑类型
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();

  // 根据id获取后端请求数据
  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    try {
      const res = await getGeneratorVoByIdUsingGet({
        // @ts-ignore
        id,
      });
      //   前端接收的是文件对象类型，后端返回的distPath是String，提交的时候文件对象转成url，回填的时候url转成文件对象
      if (res.data) {
        const { distPath } = res.data ?? {};
        if (distPath) {
          // @ts-ignore
          res.data.distPath = [
            {
              uid: id,
              name: '文件' + id,
              status: 'done',
              url: COS_HOST + distPath,
              response: distPath,
            } as UploadFile,
          ];
        }
        // 异步
        setOldData(res.data);
      }
    } catch (e: any) {
      message.error("加载数据失败" + e.message);
    }
  };

  // 当加载页面的时候，当id改变的时候，加载数据
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  /**
   * 创建
   */
  const doAdd = async (values: API.GeneratorAddRequest) => {
    //   调用接口的地方尽量都加try catch 调用上传代码生成器的接口
    try {
      const res = await addGeneratorUsingPost(values);
      // 如果存在结果
      if (res.data) {
        message.success('创建成功');
        history.push(`/generator/detail/${res.data}`);
      }
    } catch (e: any) {
      message.error('创建失败' + e.message);
    }
  };

  /**
   * 更新
   */
  const doUpdate = async (values: API.GeneratorEditRequest) => {
    //   调用接口的地方尽量都加try catch 调用上传代码生成器的接口
    try {
      const res = await editGeneratorUsingPost(values);
      // 如果存在结果
      if (res.data) {
        message.success('更新成功');
        history.push(`/generator/detail/${id}`);
      }
    } catch (e: any) {
      message.error('更新失败' + e.message);
    }
  };

  /**
   * 提交
   * @param values
   */
  const doSubmit = async (values: API.GeneratorAddRequest) => {
    // 数据转换
    if (!values.fileConfig) {
      values.fileConfig = {};
    }
    if (!values.modelConfig) {
      values.modelConfig = {};
    }
    // 文件列表转 url
    if (values.distPath && values.distPath.length > 0) {
      // @ts-ignore
      values.distPath = values.distPath[0].response;
    }

    //   调用接口的地方尽量都加try catch 调用上传代码生成器的接口
  //   如果id存在就表示要更新
    if (id){
      await doUpdate({
        // @ts-ignore
        id,
        ...values,
      });
    }else {
      await doAdd(values)
    }
  };

  return (
    <ProCard>
      {/* 创建或者已加载 需要更新时，才渲染表单，顺利填充默认值*/}
      {/*用户不传id或者oldData已存在才渲染表单*/}
      {(!id || oldData) && (
        <StepsForm<API.GeneratorAddRequest | API.GeneratorEditRequest>
          formRef={formRef}
          formProps={{
            initialValues: oldData,
          }}
          onFinish={doSubmit}
        >
          <StepsForm.StepForm name="base" title="基本信息">
            <ProFormText name="name" label="名称" placeholder="请输入名称" />
            <ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
            <ProFormText name="basePackage" label="基础包" placeholder="请输入基础包" />
            <ProFormText name="version" label="版本" placeholder="请输入版本" />
            <ProFormText name="author" label="作者" placeholder="请输入作者" />
            <ProFormSelect label="标签" mode="tags" name="tags" placeholder="请输入标签列表" />
            <ProFormItem label="图片" name="picture">
              <PictureUploader biz="generator_picture" />
            </ProFormItem>
          </StepsForm.StepForm>
          <StepsForm.StepForm name="fileConfig" title="文件配置">
            {/* todo 待补充 */}
          </StepsForm.StepForm>
          <StepsForm.StepForm name="modelConfig" title="模型配置">
            {/* todo 待补充 */}
          </StepsForm.StepForm>
          <StepsForm.StepForm name="dist" title="生成器文件">
            <ProFormItem label="产物包" name="distPath">
              <FileUploader biz="generator_dist" description="请上传生成器文件压缩包" />
            </ProFormItem>
          </StepsForm.StepForm>
        </StepsForm>
      )}
    </ProCard>
  );
};

export default GeneratorAddPage;
