import { uploadFileUsingPost } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { message, Upload, UploadFile, UploadProps } from 'antd';
import React, {useState} from 'react';

const { Dragger } = Upload;

interface Props {
  // 业务属性
  biz: string;
  // 回调函数
  onChange?: (fileList: UploadFile[]) => void;
  // 文件列表
  value?: UploadFile[];
  // 描述信息
  description?: string;
}

/**
 * 文件上传组件
 */
const FilesUploader: React.FC<Props> = (props) => {
  const { biz, value, description, onChange } = props;
  // 不让用户在上传未完成的情况下，又点击上传
  const [loading, setLoading] = useState<boolean>(false);

  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    // 展示文件列表的方式，头像图片
    listType: "text",
    maxCount: 1,
    // 让外层来控制文件列表
    fileList: value,
    // 如果文件正在上传就禁用
    disabled: loading,
    // 子组件中触发了外层的onchange事件
    onChange({ fileList }): void {
      onChange?.(fileList);
    },
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

  return (
    <Dragger {...uploadProps}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p className="ant-upload-text">点击或拖拽文件上传</p>
      <p className="ant-upload-hint">{description}</p>
    </Dragger>
  );
};

export default FilesUploader;
