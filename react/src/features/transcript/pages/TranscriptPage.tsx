import { Table, Card, Typography, Tag, Statistic, Row, Col, Divider, Button, Empty } from 'antd';
import { FileTextOutlined, TrophyOutlined, BookOutlined, DownloadOutlined, PrinterOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { useGetStudentTranscriptQuery } from '../api/transcriptApi';
import type { TranscriptGrade, TranscriptSemester } from '../../../api/response-dto/transcript.dto';
import { TranscriptSkeleton } from '../../../components/Skeletons';

const { Title, Text } = Typography;

const LEVEL_LABELS: Record<string, string> = {
  'LEVEL1': 'Licence 1',
  'LEVEL2': 'Licence 2',
  'LEVEL3': 'Licence 3',
  'LEVEL4': 'Master 1',
  'LEVEL5': 'Master 2',
};

const CYCLE_LABELS: Record<string, string> = {
  'BACHELOR': 'Bachelor (Licence)',
  'MASTER': 'Master',
  'PHD': 'PhD',
};

const getGpaColor = (gpa: number) => {
  if (gpa >= 16) return 'text-green-600';
  if (gpa >= 14) return 'text-blue-600';
  if (gpa >= 12) return 'text-cyan-600';
  if (gpa >= 10) return 'text-orange-500';
  return 'text-red-500';
};

const getGradeTag = (grade: number, passed: boolean) => {
  if (passed) {
    if (grade >= 16) return <Tag color="green">{grade}/20</Tag>;
    if (grade >= 14) return <Tag color="blue">{grade}/20</Tag>;
    return <Tag color="cyan">{grade}/20</Tag>;
  }
  return <Tag color="red">{grade}/20</Tag>;
};

export const TranscriptPage = () => {
  usePageTitle('Academic Transcript');

  const { data: transcript, isLoading, error } = useGetStudentTranscriptQuery();

  if (isLoading) {
    return <TranscriptSkeleton />;
  }

  if (error || !transcript) {
    return (
      <div className="p-4">
        <Card>
          <Empty description="Unable to load transcript. Please try again later." />
        </Card>
      </div>
    );
  }

  const gradeColumns: ColumnsType<TranscriptGrade> = [
    {
      title: 'Code',
      dataIndex: 'subjectCode',
      key: 'subjectCode',
      width: 100,
    },
    {
      title: 'Subject',
      dataIndex: 'subjectName',
      key: 'subjectName',
    },
    {
      title: 'Credits',
      dataIndex: 'credits',
      key: 'credits',
      width: 80,
      align: 'center',
    },
    {
      title: 'Grade',
      dataIndex: 'grade',
      key: 'grade',
      width: 100,
      align: 'center',
      render: (grade: number, record) => getGradeTag(grade, record.passed),
    },
    {
      title: 'Status',
      dataIndex: 'passed',
      key: 'passed',
      width: 100,
      align: 'center',
      render: (passed: boolean) => (
        <Tag color={passed ? 'success' : 'error'}>
          {passed ? 'Passed' : 'Failed'}
        </Tag>
      ),
    },
  ];

  const renderSemesterCard = (semester: TranscriptSemester, index: number) => (
    <Card 
      key={index} 
      className="mb-4"
      title={
        <div className="flex justify-between items-center">
          <span>
            <BookOutlined className="mr-2" />
            {semester.semesterName}
          </span>
          <div className="flex gap-4">
            <Text type="secondary">
              Credits: {semester.earnedCredits}/{semester.totalCredits}
            </Text>
            <Text strong className={getGpaColor(semester.gpa)}>
              GPA: {semester.gpa.toFixed(2)}/20
            </Text>
          </div>
        </div>
      }
    >
      <Table
        columns={gradeColumns}
        dataSource={semester.grades}
        rowKey="subjectCode"
        pagination={false}
        size="small"
      />
    </Card>
  );

  const progressPercent = Math.round((transcript.totalCreditsEarned / transcript.totalCreditsRequired) * 100);

  return (
    <div className="p-4">
      <Card className="mb-4">
        <div className="flex justify-between items-start mb-4">
          <div>
            <Title level={3} className="!mb-1">
              <FileTextOutlined className="mr-2" />
              Academic Transcript
            </Title>
            <Text type="secondary">
              Generated on {dayjs(transcript.generatedAt).format('MMMM DD, YYYY')}
            </Text>
          </div>
          <div className="flex gap-2">
            <Button icon={<PrinterOutlined />}>Print</Button>
            <Button type="primary" icon={<DownloadOutlined />}>Download PDF</Button>
          </div>
        </div>

        <Divider />

        <div className="bg-gray-50 rounded-lg p-4 mb-6">
          <Row gutter={[24, 16]}>
            <Col xs={24} sm={12} md={6}>
              <div>
                <Text type="secondary">Student Name</Text>
                <div><Text strong className="text-lg">{transcript.studentName}</Text></div>
              </div>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <div>
                <Text type="secondary">Student ID</Text>
                <div><Text strong className="text-lg">{transcript.matricule}</Text></div>
              </div>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <div>
                <Text type="secondary">Current Level</Text>
                <div>
                  <Tag color="blue">{LEVEL_LABELS[transcript.level] || transcript.level}</Tag>
                </div>
              </div>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <div>
                <Text type="secondary">Cycle</Text>
                <div>
                  <Tag color="purple">{CYCLE_LABELS[transcript.cycle] || transcript.cycle}</Tag>
                </div>
              </div>
            </Col>
            {transcript.speciality && (
              <Col xs={24} sm={12} md={6}>
                <div>
                  <Text type="secondary">Speciality</Text>
                  <div><Text>{transcript.speciality}</Text></div>
                </div>
              </Col>
            )}
          </Row>
        </div>

        <Row gutter={[24, 24]} className="mb-6">
          <Col xs={24} sm={8}>
            <Card className="text-center bg-blue-50">
              <Statistic
                title={<span className="text-blue-800">Cumulative GPA</span>}
                value={transcript.cumulativeGPA}
                precision={2}
                suffix="/20"
                valueStyle={{ color: transcript.cumulativeGPA >= 10 ? '#52c41a' : '#ff4d4f' }}
                prefix={<TrophyOutlined />}
              />
            </Card>
          </Col>
          <Col xs={24} sm={8}>
            <Card className="text-center bg-green-50">
              <Statistic
                title={<span className="text-green-800">Credits Earned</span>}
                value={transcript.totalCreditsEarned}
                suffix={`/ ${transcript.totalCreditsRequired}`}
                valueStyle={{ color: '#52c41a' }}
              />
            </Card>
          </Col>
          <Col xs={24} sm={8}>
            <Card className="text-center bg-purple-50">
              <Statistic
                title={<span className="text-purple-800">Progress</span>}
                value={progressPercent}
                suffix="%"
                valueStyle={{ color: 'var(--accent)' }}
              />
            </Card>
          </Col>
        </Row>

        <Divider orientation="left">
          <BookOutlined className="mr-2" />
          Semester Details
        </Divider>

        {transcript.semesters.length > 0 ? (
          transcript.semesters.map(renderSemesterCard)
        ) : (
          <Empty description="No semester data available" />
        )}
      </Card>
    </div>
  );
};

export default TranscriptPage;
