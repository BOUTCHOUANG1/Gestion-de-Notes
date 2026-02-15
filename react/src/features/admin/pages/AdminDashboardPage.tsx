import { Card, Col, Row, Statistic, Table, Tag, Typography, Spin, Alert } from 'antd';
import {
  UserOutlined,
  TeamOutlined,
  BookOutlined,
  BankOutlined,
  FileTextOutlined,
  ExclamationCircleOutlined,
  CalendarOutlined,
  TrophyOutlined,
} from '@ant-design/icons';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { 
  useGetDashboardStatsQuery, 
  useGetStudentsByLevelQuery,
  useGetRecentActivityQuery 
} from '../api/dashboardApi';
import type { RecentActivity } from '../api/dashboardApi';
import type { ColumnsType } from 'antd/es/table';

const { Title, Text } = Typography;

// Stat Card Component
interface StatCardProps {
  title: string;
  value: number;
  icon: React.ReactNode;
  color: string;
  loading?: boolean;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, icon, color, loading }) => (
  <Card bordered={false} className="shadow-sm hover:shadow-md transition-shadow">
    <Statistic
      title={<Text className="text-gray-600">{title}</Text>}
      value={value}
      prefix={
        <span style={{ color }} className="text-2xl mr-2">
          {icon}
        </span>
      }
      loading={loading}
      valueStyle={{ color, fontSize: '24px', fontWeight: 600 }}
    />
  </Card>
);

export const AdminDashboardPage = () => {
  usePageTitle('Admin Dashboard');

  const { data: stats, isLoading: statsLoading, error: statsError } = useGetDashboardStatsQuery();
  const { data: studentsByLevel, isLoading: studentsLoading } = useGetStudentsByLevelQuery();
  const { data: recentActivity, isLoading: activityLoading } = useGetRecentActivityQuery(15);

  const activityColumns: ColumnsType<RecentActivity> = [
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
      width: 100,
      render: (type: string) => {
        const config = {
          grade: { color: 'blue', label: 'Grade' },
          claim: { color: 'orange', label: 'Claim' },
          user: { color: 'green', label: 'User' },
        };
        const { color, label } = config[type as keyof typeof config] || { color: 'default', label: type };
        return <Tag color={color}>{label}</Tag>;
      },
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true,
    },
    {
      title: 'User',
      dataIndex: 'user',
      key: 'user',
      width: 150,
      render: (user?: string) => user || <Text type="secondary">System</Text>,
    },
    {
      title: 'Time',
      dataIndex: 'timestamp',
      key: 'timestamp',
      width: 180,
      render: (timestamp: string) => new Date(timestamp).toLocaleString(),
    },
  ];

  const levelColumns: ColumnsType<{ level: string; count: number }> = [
    {
      title: 'Level',
      dataIndex: 'level',
      key: 'level',
      render: (level: string) => {
        const levelLabels: Record<string, string> = {
          LEVEL1: 'Licence 1',
          LEVEL2: 'Licence 2',
          LEVEL3: 'Licence 3',
          LEVEL4: 'Master 1',
          LEVEL5: 'Master 2',
        };
        return <Tag color="purple">{levelLabels[level] || level}</Tag>;
      },
    },
    {
      title: 'Students',
      dataIndex: 'count',
      key: 'count',
      align: 'right',
      render: (count: number) => <Text strong>{count}</Text>,
    },
  ];

  if (statsError) {
    return (
      <div className="p-4">
        <Alert
          message="Error Loading Dashboard"
          description="Failed to load dashboard statistics. Please try again later."
          type="error"
          showIcon
        />
      </div>
    );
  }

  return (
    <div className="p-4 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <Title level={2} className="!mb-0">
          <TrophyOutlined className="mr-2" />
          Admin Dashboard
        </Title>
        <Text type="secondary">
          Real-time overview of your institution
        </Text>
      </div>

      {/* Statistics Cards */}
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Total Students"
            value={stats?.totalStudents || 0}
            icon={<UserOutlined />}
            color="#1890ff"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Total Teachers"
            value={stats?.totalTeachers || 0}
            icon={<TeamOutlined />}
            color="#52c41a"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Total Subjects"
            value={stats?.totalSubjects || 0}
            icon={<BookOutlined />}
            color="var(--accent)"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Departments"
            value={stats?.totalDepartments || 0}
            icon={<BankOutlined />}
            color="#fa8c16"
            loading={statsLoading}
          />
        </Col>
      </Row>

      {/* Secondary Stats */}
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Total Grades"
            value={stats?.totalGrades || 0}
            icon={<FileTextOutlined />}
            color="#13c2c2"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Pending Claims"
            value={stats?.pendingClaims || 0}
            icon={<ExclamationCircleOutlined />}
            color="#faad14"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Total Claims"
            value={stats?.totalClaims || 0}
            icon={<ExclamationCircleOutlined />}
            color="#eb2f96"
            loading={statsLoading}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatCard
            title="Active Semesters"
            value={stats?.activeSemesters || 0}
            icon={<CalendarOutlined />}
            color="#2f54eb"
            loading={statsLoading}
          />
        </Col>
      </Row>

      {/* Students by Level & Recent Activity */}
      <Row gutter={[16, 16]}>
        <Col xs={24} lg={8}>
          <Card
            title={
              <span>
                <UserOutlined className="mr-2" />
                Students by Level
              </span>
            }
            bordered={false}
            className="shadow-sm"
          >
            {studentsLoading ? (
              <div className="flex justify-center items-center h-48">
                <Spin size="large" />
              </div>
            ) : (
              <Table
                columns={levelColumns}
                dataSource={studentsByLevel}
                rowKey="level"
                pagination={false}
                size="small"
                showHeader={true}
              />
            )}
          </Card>
        </Col>

        <Col xs={24} lg={16}>
          <Card
            title={
              <span>
                <FileTextOutlined className="mr-2" />
                Recent Activity
              </span>
            }
            bordered={false}
            className="shadow-sm"
          >
            {activityLoading ? (
              <div className="flex justify-center items-center h-48">
                <Spin size="large" />
              </div>
            ) : (
              <Table
                columns={activityColumns}
                dataSource={recentActivity}
                rowKey="id"
                pagination={{ pageSize: 10, size: 'small' }}
                size="small"
                scroll={{ x: 600 }}
              />
            )}
          </Card>
        </Col>
      </Row>

      {/* Quick Stats Summary */}
      <Card bordered={false} className="shadow-sm bg-gradient-to-r from-blue-50 to-purple-50">
        <Row gutter={16} className="text-center">
          <Col xs={12} sm={6}>
            <Statistic
              title="Average Grades per Student"
              value={stats?.totalStudents ? Math.round((stats.totalGrades / stats.totalStudents) * 10) / 10 : 0}
              precision={1}
              suffix="grades"
            />
          </Col>
          <Col xs={12} sm={6}>
            <Statistic
              title="Claim Rate"
              value={stats?.totalGrades ? Math.round((stats.totalClaims / stats.totalGrades) * 1000) / 10 : 0}
              precision={1}
              suffix="%"
            />
          </Col>
          <Col xs={12} sm={6}>
            <Statistic
              title="Subjects per Department"
              value={stats?.totalDepartments ? Math.round((stats.totalSubjects / stats.totalDepartments) * 10) / 10 : 0}
              precision={1}
            />
          </Col>
          <Col xs={12} sm={6}>
            <Statistic
              title="Students per Teacher"
              value={stats?.totalTeachers ? Math.round((stats.totalStudents / stats.totalTeachers) * 10) / 10 : 0}
              precision={1}
            />
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default AdminDashboardPage;
