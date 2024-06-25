import {downloadGeneratorByIdUsingGet, getGeneratorVoByIdUsingGet} from '@/services/backend/generatorController';
import {Link, useModel, useParams} from '@@/exports';
import {PageContainer} from '@ant-design/pro-components';
import {Button, Card, Col, Image, message, Row, Space, Tabs, Tag, Typography} from 'antd';
import React, { useEffect, useState } from 'react';
import moment from "moment";
import {DownloadOutlined, EditOutlined} from "@ant-design/icons";
import FileConfig from "@/pages/Generator/Detail/components/FileConfig";
import ModelConfig from "@/pages/Generator/Detail/components/ModelConfig";
import AuthorInfo from "@/pages/Generator/Detail/components/AuthorInfo";
import {COS_HOST} from "@/constants";
import {saveAs} from "file-saver";

/**
 * 生成器详情页面
 * @constructor
 */
const GeneratorDetailPage: React.FC = () => {
  // 之前是从?id=..获取id，现在是从路由中获取id
  const {id} = useParams();
  // 要修改的数据 单个生成器包装类型
  const [data, setData] = useState<API.GeneratorVO>({});
  // 页面是否加载状态
  const [loading, setLoading] = useState<boolean>(true);
  // 代码生成器状态
  const { initialState } = useModel('@@initialState');
  // 登录用户状态是否存在
  const { currentUser } = initialState ?? {};
  // 判断是否为管理员
  const my = data?.userId === currentUser?.id;

  // 根据id获取后端请求数据
  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    setLoading(true);
    try {
      const res = await getGeneratorVoByIdUsingGet({
        // @ts-ignore
        id,
      });
      // 异步
      setData(res.data ?? {});
    } catch (e: any) {
      message.error('获取数据失败' + e.message);
    }
    setLoading(false);
  };

  // 当加载页面的时候，当id改变的时候，加载数据
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  /**
   * 标签列表视图
   * @param tags
   */
  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }

    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag: string) => {
          return <Tag key={tag}>{tag}</Tag>;
        })}
      </div>
    );
  };

  /**
   * 下载按钮
   */
  // 如果代码生成器存在并且存在当前登录用户
  const downloadButton = data.distPath && currentUser && (
    <Button
      icon={<DownloadOutlined/>}
      onClick={async () => {
        // 转换blob流，同时下载npm i --save-dev @types/file-saver  npm install file-saver
        const blob = await downloadGeneratorByIdUsingGet(
          { id: data.id },
          {
            responseType: 'blob',
          },
        );
        //   使用file-saver 下载文件
        const fullPath:string = COS_HOST + data.distPath;
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf("/") + 1));
      }}
    >下载</Button>
  )

  /**
   * 编辑按钮
   */
  const editButton = my && (
  //   用户点击编辑要跳转到编辑页面
    <Link to={`/generator/update?id=${data.id}`}>

      <Button icon={<EditOutlined/>}>编辑</Button>
    </Link>
  )

  return <PageContainer title={<></>} loading={loading}>
    <Card>
      <Row justify="space-between" gutter={[32,32]}>
        <Col flex="auto">
          <Space size="large" align="center">
            <Typography.Title level={4}>{data.name}</Typography.Title>
            {tagListView(data.tags)}
          </Space>
          <Typography.Paragraph>{data.description}</Typography.Paragraph>
          <Typography.Paragraph type="secondary">
            创建时间：{moment(data.createTime).format('YYYY-MM-DD hh:mm:ss')}
          </Typography.Paragraph>
          <Typography.Paragraph type="secondary">基础包：{data.basePackage}</Typography.Paragraph>
          <Typography.Paragraph type="secondary">版本：{data.version}</Typography.Paragraph>
          <Typography.Paragraph type="secondary">作者：{data.author}</Typography.Paragraph>
          <div style={{ marginBottom:24 }}/>
          <Space size="middle">
            <Button type="primary">立即使用</Button>
            {downloadButton}
            {editButton}
          </Space>
        </Col>
        <Col flex="320px">
          <Image src={data.picture}/>
        </Col>
      </Row>
    </Card>
    <div style={{marginBottom: 24}}/>
    <Card>
      <Tabs
        size="large"
        defaultActiveKey={'fileConfig'}
        onChange={() => {}}
        items={[
          {
            key: 'fileConfig',
            label: '文件配置',
            children: <FileConfig data={data} />,
          },
          {
            key: 'modelConfig',
            label: '模型配置',
            children: <ModelConfig data={data} />,
          },
          {
            key: 'userInfo',
            label: '作者信息',
            children: <AuthorInfo data={data} />,
          },
        ]}
      />
    </Card>
  </PageContainer>


};

export default GeneratorDetailPage;
