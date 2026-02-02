import { Table, Tag, Statistic, Row, Col, Empty, Badge, Tabs } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { 
  BookOutlined, 
  TeamOutlined, 
  TrophyOutlined,
  CheckCircleOutlined 
} from '@ant-design/icons';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useGetTeacherProfileQuery,
  useGetTeacherSubjectsQuery,
  useGetStudentsByTeachingLevelsQuery,
} from '../api/teacherDashboardApi';
import { SubjectResponse } from '../../../api/response-dto/subject.dto';
import { studentResDto } from '../../../api/reponse-dto/user.res.dto';
import { DashboardSkeleton } from '../../../components/Skeletons';
import { Card, GradeBadge } from '../../../components';

const levelLabels: Record<string, string> = {
  LEVEL1: 'Licence 1',
  LEVEL2: 'Licence 2',
  LEVEL3: 'Licence 3',
  LEVEL4: 'Master 1',
  LEVEL5: 'Master 2',
};

export const TeacherDashboardPage = () => {
  usePageTitle('Teacher Dashboard');

  const { data: profile, isLoading: profileLoading } = useGetTeacherProfileQuery();
  const { data: subjects, isLoading: subjectsLoading } = useGetTeacherSubjectsQuery();
  const { data: studentsByLevel, isLoading: studentsLoading } = useGetStudentsByTeachingLevelsQuery();

  const isLoading = profileLoading || subjectsLoading || studentsLoading;

  const subjectColumns: ColumnsType<SubjectResponse> = [
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
      render: (code: string) => <Tag color="blue">{code}</Tag>,
    },
    {
      title: 'Subject Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Level',
      dataIndex: 'level',
      key: 'level',
      render: (level: string) => levelLabels[level] || level,
    },
    {
      title: 'Credits',
      dataIndex: 'credits',
      key: 'credits',
      render: (credits: number) => <Badge count={credits} style={{ backgroundColor: '#52c41a' }} />,
    },
    {
      title: 'Department',
      dataIndex: 'departmentName',
      key: 'departmentName',
    },
  ];

  const studentColumns: ColumnsType<studentResDto> = [
    {
      title: 'Student ID',
      dataIndex: 'matricule',
      key: 'matricule',
      render: (matricule: string) => <Tag>{matricule}</Tag>,
    },
    {
      title: 'Name',
      key: 'name',
      render: (_, record) => `${record.firstName} ${record.lastName}`,
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Speciality',
      dataIndex: 'speciality',
      key: 'speciality',
      render: (speciality: string) => speciality ? <Tag color="purple">{speciality}</Tag> : '-',
    },
  ];

  const totalStudents = studentsByLevel 
    ? Object.values(studentsByLevel).reduce((sum, students) => sum + students.length, 0)
    : 0;

  const levelTabs = studentsByLevel 
    ? Object.entries(studentsByLevel).map(([level, students]) => ({
        key: level,
        label: (
          <span className="font-mono">
            {levelLabels[level] || level} <Badge count={students.length} style={{ marginLeft: 8 }} />
          </span>
        ),
        children: (
          <Table
            columns={studentColumns}
            dataSource={students}
            rowKey="id"
            pagination={{ pageSize: 10 }}
            size="small"
            className="table"
          />
        ),
      }))
    : [];

  if (isLoading) {
    return <DashboardSkeleton />;
  }

  return (
    <div className="space-y-6 p-4">
      <div className="mb-6">
        <h1 className="text-2xl md:text-3xl font-mono font-bold">
          Welcome, {profile?.firstName} {profile?.lastName}
        </h1>
        <p className="opacity-70 mt-1">{profile?.email}</p>
      </div>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic
              title={<span className="font-mono">Assigned Subjects</span>}
              value={subjects?.length || 0}
              prefix={<BookOutlined />}
              valueStyle={{ color: 'var(--mn-color-primary)', fontFamily: 'IBM Plex Mono' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic
              title={<span className="font-mono">Total Students</span>}
              value={totalStudents}
              prefix={<TeamOutlined />}
              valueStyle={{ color: 'var(--mn-color-secondary)', fontFamily: 'IBM Plex Mono' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic
              title={<span className="font-mono">Teaching Levels</span>}
              value={studentsByLevel ? Object.keys(studentsByLevel).length : 0}
              prefix={<TrophyOutlined />}
              valueStyle={{ color: '#722ed1', fontFamily: 'IBM Plex Mono' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic
              title={<span className="font-mono">Status</span>}
              value="Active"
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: 'var(--mn-color-secondary)', fontFamily: 'IBM Plex Mono' }}
            />
          </Card>
        </Col>
      </Row>

      <Card className="mt-6" hoverable={false}>
        <h2 className="text-xl font-mono font-bold mb-4">My Subjects</h2>
        {subjects && subjects.length > 0 ? (
          <Table
            columns={subjectColumns}
            dataSource={subjects}
            rowKey="id"
            pagination={false}
            className="table"
          />
        ) : (
          <Empty description="No subjects assigned yet" />
        )}
      </Card>

      <Card className="mt-6" hoverable={false}>
        <h2 className="text-xl font-mono font-bold mb-4">My Students by Level</h2>
        {levelTabs.length > 0 ? (
          <Tabs items={levelTabs} />
        ) : (
          <Empty description="No students in your teaching levels" />
        )}
      </Card>
    </div>
  );
};
