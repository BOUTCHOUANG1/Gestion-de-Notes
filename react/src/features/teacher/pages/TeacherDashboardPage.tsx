import { Card, Table, Tag, Statistic, Row, Col, Spin, Alert, Empty, Badge, Tabs } from 'antd';
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
      title: 'Matricule',
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
          <span>
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
          />
        ),
      }))
    : [];

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div className="space-y-6 p-4">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800">
          Welcome, {profile?.firstName} {profile?.lastName}
        </h1>
        <p className="text-gray-500">{profile?.email}</p>
      </div>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Assigned Subjects"
              value={subjects?.length || 0}
              prefix={<BookOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Students"
              value={totalStudents}
              prefix={<TeamOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Teaching Levels"
              value={studentsByLevel ? Object.keys(studentsByLevel).length : 0}
              prefix={<TrophyOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Status"
              value="Active"
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
      </Row>

      <Card title="My Subjects" className="mt-6">
        {subjects && subjects.length > 0 ? (
          <Table
            columns={subjectColumns}
            dataSource={subjects}
            rowKey="id"
            pagination={false}
          />
        ) : (
          <Empty description="No subjects assigned yet" />
        )}
      </Card>

      <Card title="My Students by Level" className="mt-6">
        {levelTabs.length > 0 ? (
          <Tabs items={levelTabs} />
        ) : (
          <Empty description="No students in your teaching levels" />
        )}
      </Card>

      {/* Department info removed - not available in TeacherResponse */}
      {/* {profile?.department && (
        <Alert
          message="Department Information"
          description={`You are assigned to the ${profile.department.name} department.`}
          type="info"
          showIcon
        />
      )} */}
    </div>
  );
};
