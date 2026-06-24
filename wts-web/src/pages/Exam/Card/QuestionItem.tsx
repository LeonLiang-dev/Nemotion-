import React from 'react';
import { Radio, Checkbox, Input, Tag, Space } from 'antd';

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

const QuestionItem: React.FC<Props> = ({ index, subject, value, onChange, mobile }) => {
  const typeInfo = TIPTYPE_LABELS[subject.tiptype] || { label: '未知', color: 'default' };

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
            {subject.answers.map((a) => (
              <div key={a.id} style={{ marginBottom: 8, display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ flexShrink: 0 }}>({a.sort})</span>
                <Input
                  style={{ flex: 1 }}
                  placeholder="请输入答案"
                  value={value?.[a.id] || ''}
                  onChange={(e) => {
                    const newValue = { ...(value || {}), [a.id]: e.target.value };
                    onChange?.(newValue);
                  }}
                />
              </div>
            ))}
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
