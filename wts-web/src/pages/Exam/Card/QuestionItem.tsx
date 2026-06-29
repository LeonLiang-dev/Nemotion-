import React from 'react';
import { Radio, Checkbox, Input, Tag, Space, Button, Image as AntImage, message } from 'antd';
import { DeleteOutlined, PictureOutlined } from '@ant-design/icons';
import { isImageAnswerValue } from './AnswerValueView';

const TIPTYPE_LABELS: Record<string, { label: string; color: string }> = {
  '1': { label: '填空题', color: 'blue' },
  '2': { label: '单选题', color: 'green' },
  '3': { label: '多选题', color: 'orange' },
  '4': { label: '判断题', color: 'purple' },
  '5': { label: '问答题', color: 'cyan' },
  '6': { label: '附件题', color: 'magenta' },
};

interface AnswerOption {
  id: string;
  answer: string;
  sort: number;
  pcontent?: string;
}

interface Subject {
  paperSubjectId: string;
  subjectId: string;
  versionId: string;
  point: number;
  introduction?: string;
  tiptype: string;
  tipstr?: string;
  tipnote?: string;
  answers: AnswerOption[];
}

interface Props {
  index: number;
  subject: Subject;
  value?: any;
  onChange?: (value: any) => void;
  mobile?: boolean;
}

const MAX_IMAGE_DATA_URL_LENGTH = 8 * 1024 * 1024;
const MAX_IMAGE_SIDE = 1400;

const compressImageToDataUrl = (file: File): Promise<string> => new Promise((resolve, reject) => {
  const reader = new FileReader();
  reader.onerror = () => reject(new Error('读取图片失败'));
  reader.onload = () => {
    const img = new window.Image();
    img.onerror = () => reject(new Error('图片解析失败'));
    img.onload = () => {
      const scale = Math.min(1, MAX_IMAGE_SIDE / img.width, MAX_IMAGE_SIDE / img.height);
      const width = Math.max(1, Math.round(img.width * scale));
      const height = Math.max(1, Math.round(img.height * scale));
      const canvas = document.createElement('canvas');
      canvas.width = width;
      canvas.height = height;
      const ctx = canvas.getContext('2d');
      if (!ctx) {
        reject(new Error('浏览器不支持图片处理'));
        return;
      }
      ctx.fillStyle = '#fff';
      ctx.fillRect(0, 0, width, height);
      ctx.drawImage(img, 0, 0, width, height);
      const dataUrl = canvas.toDataURL('image/jpeg', 0.82);
      if (dataUrl.length > MAX_IMAGE_DATA_URL_LENGTH) {
        reject(new Error('图片过大，请裁剪或压缩后再粘贴'));
        return;
      }
      resolve(dataUrl);
    };
    img.src = String(reader.result || '');
  };
  reader.readAsDataURL(file);
});

const getPastedImageFile = (event: React.ClipboardEvent): File | null => {
  const items = Array.from(event.clipboardData?.items || []);
  for (const item of items) {
    if (item.type.startsWith('image/')) {
      return item.getAsFile();
    }
  }
  return null;
};

const QuestionItem: React.FC<Props> = ({ index, subject, value, onChange, mobile }) => {
  const typeInfo = TIPTYPE_LABELS[subject.tiptype] || { label: '未知', color: 'default' };

  const handleFillPaste = async (event: React.ClipboardEvent, answerId: string) => {
    const file = getPastedImageFile(event);
    if (!file) return;
    event.preventDefault();
    const hide = message.loading('正在处理粘贴的图片...', 0);
    try {
      const dataUrl = await compressImageToDataUrl(file);
      const newValue = { ...(value || {}), [answerId]: dataUrl };
      onChange?.(newValue);
      message.success('图片已粘贴');
    } catch (error: any) {
      message.error(error?.message || '图片粘贴失败');
    } finally {
      hide();
    }
  };

  const renderQuestionBody = () => {
    switch (subject.tiptype) {
      case '2': // Single choice
        return (
          <Radio.Group
            value={value}
            onChange={(e) => onChange?.(e.target.value)}
          >
            <Space direction="vertical" style={{ width: '100%' }}>
              {subject.answers.map((a) => (
                <Radio key={a.id} value={a.id} style={{ lineHeight: 2 }}>
                  {a.answer}
                </Radio>
              ))}
            </Space>
          </Radio.Group>
        );

      case '3': // Multiple choice
        return (
          <Checkbox.Group
            value={value || []}
            onChange={(checkedValues) => onChange?.(checkedValues)}
          >
            <Space direction="vertical" style={{ width: '100%' }}>
              {subject.answers.map((a) => (
                <Checkbox key={a.id} value={a.id} style={{ lineHeight: 2 }}>
                  {a.answer}
                </Checkbox>
              ))}
            </Space>
          </Checkbox.Group>
        );

      case '4': // True/False
        return (
          <Radio.Group
            value={value}
            onChange={(e) => onChange?.(e.target.value)}
          >
            <Space direction="vertical">
              {subject.answers.map((a) => (
                <Radio key={a.id} value={a.id} style={{ lineHeight: 2 }}>
                  {a.answer}
                </Radio>
              ))}
            </Space>
          </Radio.Group>
        );

      case '1': // Fill in the blank
        return (
          <Space direction="vertical" style={{ width: '100%' }}>
            {subject.answers.map((a) => {
              const currentValue = value?.[a.id] || '';
              const isImage = isImageAnswerValue(currentValue);
              return (
                <div key={a.id} style={{ marginBottom: 10, display: 'flex', alignItems: 'flex-start', gap: 8 }}>
                  <span style={{ flexShrink: 0, lineHeight: '32px' }}>({a.sort})</span>
                  <div style={{ flex: 1 }}>
                    {isImage ? (
                      <div
                        tabIndex={0}
                        onPaste={(event) => handleFillPaste(event, a.id)}
                        style={{
                          padding: 10,
                          border: '1px solid #d9d9d9',
                          borderRadius: 6,
                          background: '#fff',
                          outline: 'none',
                        }}
                      >
                        <AntImage
                          src={currentValue}
                          alt="填空答案图片"
                          style={{
                            maxWidth: mobile ? 220 : 320,
                            maxHeight: 220,
                            borderRadius: 4,
                            objectFit: 'contain',
                          }}
                        />
                        <div style={{ marginTop: 8, display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap' }}>
                          <span style={{ color: '#64748b', fontSize: 12 }}>可继续粘贴图片替换当前答案</span>
                          <Button
                            size="small"
                            danger
                            icon={<DeleteOutlined />}
                            onClick={() => {
                              const newValue = { ...(value || {}), [a.id]: '' };
                              onChange?.(newValue);
                            }}
                          >
                            删除图片
                          </Button>
                        </div>
                      </div>
                    ) : (
                      <Input
                        style={{ flex: 1 }}
                        placeholder="请输入答案，或直接粘贴图片"
                        value={currentValue}
                        prefix={<PictureOutlined style={{ color: '#94a3b8' }} />}
                        onPaste={(event) => handleFillPaste(event, a.id)}
                        onChange={(e) => {
                          const newValue = { ...(value || {}), [a.id]: e.target.value };
                          onChange?.(newValue);
                        }}
                      />
                    )}
                  </div>
                </div>
              );
            })}
          </Space>
        );

      case '5': // Essay
        return (
          <Input.TextArea
            rows={6}
            placeholder="请输入你的答案"
            value={value || ''}
            onChange={(e) => onChange?.(e.target.value)}
          />
        );

      case '6': // File upload
        return (
          <div style={{ padding: 16, background: '#fafafa', borderRadius: 4 }}>
            <p>附件题暂不支持在线作答，请联系管理员。</p>
          </div>
        );

      default:
        return <div>不支持的题型</div>;
    }
  };

  return (
    <div
      id={`question-${index}`}
      className="wts-question-item"
    >
      <div className="wts-question-header">
        <span className="wts-question-number">{index + 1}</span>
        <Tag color={typeInfo.color}>{typeInfo.label}</Tag>
        <span className="wts-question-text">
          {subject.tipstr || subject.introduction || ''}
        </span>
        <span className="wts-question-points">({subject.point}分)</span>
      </div>
      {subject.tipnote && (
        <div style={{ marginBottom: 12, color: '#666', fontSize: 13 }}>{subject.tipnote}</div>
      )}
      {renderQuestionBody()}
    </div>
  );
};

export default QuestionItem;
