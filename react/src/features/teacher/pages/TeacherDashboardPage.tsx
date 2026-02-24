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
  useGetTeacherGradesQuery,
} from '../api/teacherDashboardApi';
import { DashboardSkeleton } from '../../../components/Skeletons';
import { Card, GradeBadge } from '../../../components';
import React from 'react';

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
  const { data: teacherGrades, isLoading: gradesLoading } = useGetTeacherGradesQuery();

  const isLoading = profileLoading || subjectsLoading || studentsLoading || gradesLoading;

  const totalStudents = studentsByLevel 
    ? Object.values(studentsByLevel).reduce((sum, students) => sum + students.length, 0)
    : 0;

  const subjectAverages = React.useMemo(() => {
    if (!teacherGrades || !subjects) return {} as Record<string, { sum: number; count: number; avg: number }>;
    const averages: Record<string, { sum: number; count: number; avg: number }> = {};
    teacherGrades.forEach((g: any) => {
      const code = g.subject?.subjectCode || g.subjectCode || '';
      if (!code) return;
      if (!averages[code]) averages[code] = { sum: 0, count: 0, avg: 0 };
      averages[code].sum += (g.totalScore ?? g.value ?? 0);
      averages[code].count += 1;
    });
    Object.keys(averages).forEach(code => {
      averages[code].avg = averages[code].sum / averages[code].count;
    });
    return averages;
  }, [teacherGrades, subjects]);

  const recentGrades = teacherGrades?.slice(0, 5) || [];

  const subjectColumns: ColumnsType<any> = [
    {
      title: 'Code',
      key: 'subjectCode',
      render: (_, r) => <Tag color="blue">{r.subjectCode}</Tag>,
    },
    {
      title: 'Subject Name',
      dataIndex: 'subjectName',
      key: 'subjectName',
    },
    {
      title: 'Level',
      key: 'level',
      render: (_, r) => {
        const levels = r.subjectsLevel;
        if (Array.isArray(levels)) {
          return levels.map((l: any) => (
            <Tag key={l.teachingLevelId}>{levelLabels[l.studentLevel] || l.studentLevel}</Tag>
          ));
        }
        return '-';
      },
    },
    {
      title: 'Credits',
      dataIndex: 'credits',
      key: 'credits',
      render: (credits: number) => <Badge count={credits} style={{ backgroundColor: '#52c41a' }} />,
    },
    {
      title: 'Average Grade',
      key: 'average',
      render: (_: unknown, record: any) => {
        const avg = subjectAverages[record.subjectCode];
        return avg ? (
          <span>{avg.avg.toFixed(1)} ({avg.count} grades)</span>
        ) : (
          <span className="text-gray-400">No grades</span>
        );
      },
    },
    {
      title: 'Cycle',
      key: 'studentCycle',
      render: (_, r) => r.studentCycle ? <Tag color="purple">{r.studentCycle}</Tag> : '-',
    },
  ];

  const studentColumns: ColumnsType<any> = [
    {
      title: 'Matricule',
      dataIndex: 'matricule',
      key: 'matricule',
      render: (m: string) => m ? <Tag>{m}</Tag> : '-',
    },
    {
      title: 'Name',
      key: 'name',
      render: (_, r) => `${r.firstName} ${r.lastName}`,
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
      render: (s: string) => s ? <Tag color="purple">{s}</Tag> : '-',
    },
  ];

  const levelTabs = studentsByLevel 
    ? Object.entries(studentsByLevel).map(([level, students]) => ({
        key: level,
        label: (
          <span className="font-sans">
            {levelLabels[level] || level} <Badge count={(students as any[]).length} style={{ marginLeft: 8 }} />
          </span>
        ),
        children: (
          <Table
            columns={studentColumns}
            dataSource={students as any[]}
            rowKey="id"
            pagination={{ pageSize: 10 }}
            size="small"
          />
        ),
      }))
    : [];

  if (isLoading) return <DashboardSkeleton />;

  return (
    <div className="space-y-6 p-4">
      <div className="mb-4">
        <h1 className="text-xl md:text-2xl font-semibold tracking-tight">
          Welcome, {profile?.firstName} {profile?.lastName}
        </h1>
        <p className="text-sm text-[var(--text-secondary)] mt-1">{profile?.email}</p>
      </div>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic title="Assigned Subjects" value={subjects?.length || 0} prefix={<BookOutlined />} valueStyle={{ color: 'var(--accent)' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic title="Total Students" value={totalStudents} prefix={<TeamOutlined />} valueStyle={{ color: 'var(--accent-hover)' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic title="Teaching Levels" value={studentsByLevel ? Object.keys(studentsByLevel).length : 0} prefix={<TrophyOutlined />} valueStyle={{ color: 'var(--accent)' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable>
            <Statistic title="Total Grades" value={teacherGrades?.length || 0} prefix={<CheckCircleOutlined />} valueStyle={{ color: 'var(--accent-hover)' }} />
          </Card>
        </Col>
      </Row>

      <Card className="mt-6" hoverable={false}>
        <h2 className="text-base font-semibold mb-3">Recent Grades Submitted</h2>
        {recentGrades.length > 0 ? (
          <Table
            columns={[
              {
                title: 'Student',
                key: 'student',
                render: (_: unknown, r: any) => r.student ? `${r.student.firstName} ${r.student.lastName}` : '-',
              },
              {
                title: 'Subject',
                key: 'subject',
                render: (_: unknown, r: any) => <Tag color="blue">{r.subject?.subjectCode || '-'}</Tag>,
              },
              {
                title: 'Grade',
                key: 'totalScore',
                render: (_: unknown, r: any) => <GradeBadge grade={r.totalScore ?? 0} />,
              },
              {
                title: 'Type',
                key: 'exam',
                render: (_: unknown, r: any) => <Tag>{r.exam || '-'}</Tag>,
              },
              {
                title: 'Date',
                key: 'createdDate',
                render: (_: unknown, r: any) => r.createdDate ? new Date(r.createdDate).toLocaleDateString() : '-',
              },
            ]}
            dataSource={recentGrades}
            rowKey="gradeId"
            pagination={false}
            size="small"
          />
        ) : (
          <Empty description="No grades submitted yet" />
        )}
      </Card>

      <Card className="mt-6" hoverable={false}>
        <h2 className="text-base font-semibold mb-3">My Subjects</h2>
        {subjects && subjects.length > 0 ? (
          <Table columns={subjectColumns} dataSource={subjects} rowKey="subjectId" pagination={false} />
        ) : (
          <Empty description="No subjects assigned yet" />
        )}
      </Card>

      <Card className="mt-6" hoverable={false}>
        <h2 className="text-base font-semibold mb-3">My Students by Level</h2>
        {levelTabs.length > 0 ? (
          <Tabs items={levelTabs} />
        ) : (
          <Empty description="No students in your teaching levels" />
        )}
      </Card>
    </div>
  );
};
