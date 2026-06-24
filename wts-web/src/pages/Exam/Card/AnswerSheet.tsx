import React from 'react';
import { Badge, Card, Typography } from 'antd';

const { Text } = Typography;

interface Subject {
  paperSubjectId: string;
  versionId: string;
  tiptype: string;
}

interface Props {
  subjects: { subject: Subject; chapterName: string }[];
  answeredSet: Set<string>;
  currentIndex: number;
  compact?: boolean;
  onJump: (index: number) => void;
}

const AnswerSheet: React.FC<Props> = ({ subjects, answeredSet, currentIndex, compact, onJump }) => {
  // Group by chapter
  const chapters: { name: string; items: { subject: Subject; globalIndex: number }[] }[] = [];
  let currentChapter = '';
  for (let i = 0; i < subjects.length; i++) {
    const { subject, chapterName } = subjects[i];
    if (chapterName !== currentChapter) {
      currentChapter = chapterName;
      chapters.push({ name: chapterName, items: [] });
    }
    chapters[chapters.length - 1].items.push({ subject, globalIndex: i });
  }

  const cellSize = compact ? 28 : 32;

  return (
    <Card title={compact ? undefined : '答题卡'} size="small" style={compact ? {} : { position: 'sticky', top: 16 }}>
      <div style={{ marginBottom: 8, fontSize: compact ? 12 : 14 }}>
        <Badge status="success" text="已答" />
        <Badge status="default" text="未答" style={{ marginLeft: 12 }} />
        <Badge color="blue" text="当前" style={{ marginLeft: 12 }} />
        {compact && (
          <span style={{ marginLeft: 12, color: '#999' }}>
            ({answeredSet.size}/{subjects.length})
          </span>
        )}
      </div>
      {chapters.map((ch) => (
        <div key={ch.name} style={{ marginBottom: compact ? 8 : 12 }}>
          <Text strong style={{ fontSize: compact ? 11 : 12, color: '#666' }}>{ch.name}</Text>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: compact ? 4 : 6, marginTop: 4 }}>
            {ch.items.map(({ subject, globalIndex }) => {
              const answered = answeredSet.has(subject.versionId);
              const isCurrent = globalIndex === currentIndex;
              return (
                <div
                  key={globalIndex}
                  onClick={() => onJump(globalIndex)}
                  className={`wts-answer-cell ${isCurrent ? 'wts-answer-cell-current' : answered ? 'wts-answer-cell-answered' : 'wts-answer-cell-default'}`}
                  style={{ width: cellSize, height: cellSize, fontSize: compact ? 11 : 12 }}
                >
                  {globalIndex + 1}
                </div>
              );
            })}
          </div>
        </div>
      ))}
    </Card>
  );
};

export default AnswerSheet;
