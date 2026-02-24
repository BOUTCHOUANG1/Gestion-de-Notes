import { useState } from 'react';
import { Table, Card, Typography, Tag, Statistic, Row, Col, Divider, Button, Empty } from 'antd';
import { FileTextOutlined, TrophyOutlined, BookOutlined, DownloadOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { useGetStudentTranscriptQuery } from '../api/transcriptApi';
import type { TranscriptGrade } from '../../../api/response-dto/transcript.dto';
import { TranscriptSkeleton } from '../../../components/Skeletons';

const { Title, Text } = Typography;

const LEVEL_LABELS: Record<string, string> = {
  LEVEL1: 'Licence 1', LEVEL2: 'Licence 2', LEVEL3: 'Licence 3',
  LEVEL4: 'Master 1', LEVEL5: 'Master 2',
};

const CYCLE_LABELS: Record<string, string> = {
  BACHELOR: 'Bachelor (Licence)', MASTER: 'Master', PHD: 'PhD',
};

const getGradeTag = (score: number | null, passed: boolean) => {
  if (score == null) return <Tag>—</Tag>;
  const color = passed ? (score >= 16 ? 'green' : score >= 14 ? 'blue' : 'cyan') : 'red';
  return <Tag color={color}>{score}/20</Tag>;
};

export const TranscriptPage = () => {
  usePageTitle('Academic Transcript');
  const { data: transcript, isLoading, error } = useGetStudentTranscriptQuery();

  if (isLoading) return <TranscriptSkeleton />;
  if (error || !transcript) {
    return (
      <div className="p-4">
        <Card><Empty description="Unable to load transcript. Please try again later." /></Card>
      </div>
    );
  }

  const [pdfLoading, setPdfLoading] = useState(false);

  const handleDownloadPDF = async () => {
    if (!transcript) return;
    setPdfLoading(true);
    try {
      const { generateTranscriptPDF } = await import('../../../utils/generateTranscriptPDF');
      generateTranscriptPDF(transcript);
    } catch (err) {
      console.error('PDF generation failed:', err);
    } finally {
      setPdfLoading(false);
    }
  };

  const level = transcript.studentLevel?.studentLevel || '';
  const cycle = transcript.studentCycle || '';
  const grades = transcript.studentGrades || [];

  // Group grades by semester
  const bySemester = new Map<string, TranscriptGrade[]>();
  for (const g of grades) {
    const sem = g.semester?.name || 'Unknown';
    if (!bySemester.has(sem)) bySemester.set(sem, []);
    bySemester.get(sem)!.push(g);
  }

  const gradeColumns: ColumnsType<TranscriptGrade> = [
    { title: 'Code', key: 'code', width: 100, render: (_, r) => r.subject?.subjectCode },
    { title: 'Matière', key: 'subject', render: (_, r) => r.subject?.subjectName },
    { title: 'Crédits', key: 'credits', width: 80, align: 'center', render: (_, r) => r.subject?.credits },
    { title: 'CC', dataIndex: 'ccScore', width: 70, align: 'center', render: (v: number | null) => v ?? '—' },
    { title: 'TP', dataIndex: 'tpScore', width: 70, align: 'center', render: (v: number | null) => v ?? '—' },
    { title: 'SN', dataIndex: 'snScore', width: 70, align: 'center', render: (v: number | null) => v ?? '—' },
    { title: 'Total', dataIndex: 'totalScore', width: 100, align: 'center', render: (v: number | null, r) => getGradeTag(v, r.hasPassed) },
    {
      title: 'Résultat', dataIndex: 'hasPassed', width: 100, align: 'center',
      render: (v: boolean) => <Tag color={v ? 'success' : 'error'}>{v ? 'Validé' : 'Non validé'}</Tag>,
    },
  ];

  const progressPercent = transcript.totalCreditsRequired
    ? Math.round(((transcript.creditsEarned || 0) / transcript.totalCreditsRequired) * 100)
    : 0;

  return (
    <div className="p-4">
      <Card className="mb-4">
        <div className="flex justify-between items-start mb-4">
          <div>
            <Title level={3} className="!mb-1">
              <FileTextOutlined className="mr-2" />
              Relevé de Notes
            </Title>
            <Text type="secondary">
              {transcript.studentFirstName} {transcript.studentLastName} — {transcript.studentMatricule}
            </Text>
          </div>
          <Button icon={<DownloadOutlined />} type="primary" onClick={handleDownloadPDF} loading={pdfLoading}>
            {pdfLoading ? 'Génération...' : 'Imprimer'}
          </Button>
        </div>

        <Divider />

        <div className="bg-gray-50 rounded-lg p-4 mb-6">
          <Row gutter={[24, 16]}>
            <Col xs={12} md={6}>
              <Text type="secondary">Nom</Text>
              <div><Text strong>{transcript.studentFirstName} {transcript.studentLastName}</Text></div>
            </Col>
            <Col xs={12} md={6}>
              <Text type="secondary">Matricule</Text>
              <div><Text strong>{transcript.studentMatricule}</Text></div>
            </Col>
            <Col xs={12} md={6}>
              <Text type="secondary">Niveau</Text>
              <div><Tag color="blue">{LEVEL_LABELS[level] || level}</Tag></div>
            </Col>
            <Col xs={12} md={6}>
              <Text type="secondary">Cycle</Text>
              <div><Tag color="purple">{CYCLE_LABELS[cycle] || cycle}</Tag></div>
            </Col>
          </Row>
        </div>

        <Row gutter={[24, 24]} className="mb-6">
          <Col xs={24} sm={8}>
            <Card className="text-center bg-blue-50">
              <Statistic
                title={<span className="text-blue-800">Moyenne Annuelle</span>}
                value={transcript.annualAverage ?? 0}
                precision={2}
                suffix="/20"
                valueStyle={{ color: (transcript.annualAverage ?? 0) >= 10 ? '#52c41a' : '#ff4d4f' }}
                prefix={<TrophyOutlined />}
              />
            </Card>
          </Col>
          <Col xs={24} sm={8}>
            <Card className="text-center bg-green-50">
              <Statistic
                title={<span className="text-green-800">Crédits Obtenus</span>}
                value={transcript.creditsEarned ?? 0}
                suffix={`/ ${transcript.totalCreditsRequired ?? '—'}`}
                valueStyle={{ color: '#52c41a' }}
              />
            </Card>
          </Col>
          <Col xs={24} sm={8}>
            <Card className="text-center bg-purple-50">
              <Statistic
                title={<span className="text-purple-800">Progression</span>}
                value={progressPercent}
                suffix="%"
                valueStyle={{ color: 'var(--accent)' }}
              />
            </Card>
          </Col>
        </Row>

        <Divider orientation="left">
          <BookOutlined className="mr-2" />
          Détails par Semestre
        </Divider>

        {bySemester.size > 0 ? (
          Array.from(bySemester.entries()).map(([semName, semGrades]) => (
            <Card key={semName} className="mb-4" title={<span><BookOutlined className="mr-2" />{semName}</span>}>
              <Table<TranscriptGrade>
                columns={gradeColumns}
                dataSource={semGrades}
                rowKey="gradeId"
                pagination={false}
                size="small"
              />
            </Card>
          ))
        ) : (
          <Empty description="Aucune donnée disponible" />
        )}
      </Card>
    </div>
  );
};

export default TranscriptPage;
