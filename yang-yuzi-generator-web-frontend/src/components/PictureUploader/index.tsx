import { COS_HOST } from '@/constants';
import { uploadFileUsingPost } from '@/services/backend/fileController';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { message, Upload, UploadProps } from 'antd';
import React, { useState } from 'react';

interface Props {
  // 业务属性
  biz: string;
  // 回调函数
  onChange?: (url: string) => void;
  // 文件列表
  value?: string;
}

/**
 * 图片上传组件
 */
const PictureUploader: React.FC<Props> = (props) => {
  const { biz, value, onChange } = props;
  // 不让用户在上传未完成的情况下，又点击上传
  const [loading, setLoading] = useState<boolean>(false);

  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    // 展示文件列表的方式，头像图片
    listType: 'picture-card',
    showUploadList: false,
    maxCount: 1,
    // 如果文件正在上传就禁用
    disabled: loading,
    customRequest: async (fileObj: any): Promise<void> => {
      // 如果用户要上传
      setLoading(true);
      try {
        const res: API.BaseResponseString_ = await uploadFileUsingPost(
          {
            // 业务
            biz,
          },
          // 包类
          {},
          fileObj.file,
        );
        // 拼接完整图片路径
        const fullPath = COS_HOST + res.data;
        // 在自定义请求中回调 告诉父级组件路径改了
        onChange?.(fullPath ?? '');
        // 告诉组件成功了要展示，失败了要标红
        fileObj.onSuccess(res.data);
        // 成功了，把文件地址设置到value中
      } catch (e: any) {
        message.error('上传失败，' + e.message);
        fileObj.onError(e);
      }
      // 如果用户上传结束
      setLoading(false);
    },
  };

  /**
   * 上传按钮
   */
  const uploadButton = (
    <button style={{ border: 0, background: 'none' }} type="button">
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传</div>
    </button>
  );

  return (
    <Upload {...uploadProps}>
      {value ? <img src={value} alt="picture" style={{ width: '100%' }} /> : uploadButton}
    </Upload>
  );
};

export default PictureUploader;
