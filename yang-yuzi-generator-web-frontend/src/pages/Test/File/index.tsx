import { COS_HOST } from '@/constants';
import {
  testDownloadFileUsingGet,
  testUploadFileUsingPost,
} from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Button, Card, Divider, Flex, message, Upload, UploadProps } from 'antd';
import React, { useState } from 'react';
import {saveAs} from "file-saver";

const { Dragger } = Upload;
/**
 * 文件上传下载测试页面
 */
const TestFilePage: React.FC = () => {
  const [value, setValue] = useState<string>();

  const props: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    customRequest: async (fileObj: any): Promise<void> => {
      try {
        const res: API.BaseResponseString_ = await testUploadFileUsingPost({}, fileObj.file);
        // 告诉组件成功了要展示，失败了要标红
        fileObj.onSuccess(res.data);
        // 成功了，把文件地址设置到value中
        setValue(res.data);
      } catch (e: any) {
        message.error('上传失败，' + e.message);
        fileObj.onError(e);
      }
    },
    onRemove(): void {
      setValue(undefined);
    },
  };

  return (
    <Flex gap={16}>
      <Card title="文件上传">
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibited from uploading company data or
            other banned files.
          </p>
        </Dragger>
      </Card>
      <Card title="文件下载">
        <div>文件地址：{COS_HOST + value}</div>
        <Divider />
        <img src={COS_HOST + value} height={200} />
        <Divider />
        <Button
          onClick={async (): Promise<void> => {
            // 转换blob流，同时下载npm i --save-dev @types/file-saver  npm install file-saver
            const blob = await testDownloadFileUsingGet(
              { filepath: value },
              {
                responseType: 'blob',
              },
            );
          //   使用file-saver 下载文件
            const fullPath:string = COS_HOST + value;
            saveAs(blob, fullPath.substring(fullPath.lastIndexOf("/") + 1));
          }}
        >
          点击下载文件
        </Button>
      </Card>
    </Flex>
  );
};

export default TestFilePage;
