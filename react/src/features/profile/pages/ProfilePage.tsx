import { Card, Typography, Avatar, Tag, Descriptions, Spin, Empty, Divider, Row, Col } from 'antd';
import { UserOutlined, MailOutlined, IdcardOutlined, BookOutlined, CalendarOutlined } from '@ant-design/icons';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { useGetProfileQuery } from '../../auth/api/authApi';
import { Role } from '../../../api/enums';

const { Title, Text } = Typography;

const ROLE_COLORS: Record<string, string> = {
  'ADMIN': 'red',
  'TEACHER': 'blue',
  'STUDENT': 'green',
};

const ROLE_LABELS: Record<string, string> = {
  'ADMIN': 'Administrator',
  'TEACHER': 'Teacher',
  'STUDENT': 'Student',
};

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

export const ProfilePage = () => {
  usePageTitle('My Profile');

  const { data: profile, isLoading, error } = useGetProfileQuery();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spin size="large" tip="Loading profile..." />
      </div>
    );
  }

  if (error || !profile) {
    return (
      <div className="p-4">
        <Card>
          <Empty description="Unable to load profile. Please try again later." />
        </Card>
      </div>
    );
  }

  const getInitials = () => {
    const first = profile.firstName?.charAt(0) || '';
    const last = profile.lastName?.charAt(0) || '';
    return (first + last).toUpperCase();
  };

  const getAvatarColor = () => {
    switch (profile.role) {
      case Role.ADMIN: return '#f56a00';
      case Role.TEACHER: return '#1890ff';
      case Role.STUDENT: return '#52c41a';
      default: return '#8c8c8c';
    }
  };

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <Card>
        <div className="flex flex-col items-center mb-6">
          <Avatar 
            size={100} 
            style={{ backgroundColor: getAvatarColor(), fontSize: '36px' }}
          >
            {getInitials()}
          </Avatar>
          <Title level={3} className="!mt-4 !mb-1">
            {profile.firstName} {profile.lastName}
          </Title>
          <Tag color={ROLE_COLORS[profile.role] || 'default'} className="text-base px-3 py-1">
            {ROLE_LABELS[profile.role] || profile.role}
          </Tag>
        </div>

        <Divider />

        <Row gutter={[24, 24]}>
          <Col xs={24} md={12}>
            <Card className="h-full bg-gray-50" bordered={false}>
              <Title level={5} className="!mb-4">
                <UserOutlined className="mr-2" />
                Personal Information
              </Title>
              <Descriptions column={1} size="small">
                <Descriptions.Item label="First Name">
                  <Text strong>{profile.firstName}</Text>
                </Descriptions.Item>
                <Descriptions.Item label="Last Name">
                  <Text strong>{profile.lastName}</Text>
                </Descriptions.Item>
                <Descriptions.Item label="Username">
                  <Text code>{profile.username}</Text>
                </Descriptions.Item>
              </Descriptions>
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card className="h-full bg-gray-50" bordered={false}>
              <Title level={5} className="!mb-4">
                <MailOutlined className="mr-2" />
                Contact Information
              </Title>
              <Descriptions column={1} size="small">
                <Descriptions.Item label="Email">
                  <Text copyable>{profile.email}</Text>
                </Descriptions.Item>
              </Descriptions>
            </Card>
          </Col>
        </Row>

        {profile.role === Role.STUDENT && (
          <>
            <Divider />
            <Card className="bg-blue-50" bordered={false}>
              <Title level={5} className="!mb-4">
                <BookOutlined className="mr-2" />
                Academic Information
              </Title>
              <Row gutter={[24, 16]}>
                <Col xs={24} sm={8}>
                  <div>
                    <Text type="secondary">Matricule</Text>
                    <div>
                      <Text strong className="text-lg">
                        {(profile as any).matricule || 'N/A'}
                      </Text>
                    </div>
                  </div>
                </Col>
                <Col xs={24} sm={8}>
                  <div>
                    <Text type="secondary">Current Level</Text>
                    <div>
                      <Tag color="blue">
                        {LEVEL_LABELS[(profile as any).studentLevel] || (profile as any).studentLevel || 'N/A'}
                      </Tag>
                    </div>
                  </div>
                </Col>
                <Col xs={24} sm={8}>
                  <div>
                    <Text type="secondary">Cycle</Text>
                    <div>
                      <Tag color="purple">
                        {CYCLE_LABELS[(profile as any).cycle] || (profile as any).cycle || 'N/A'}
                      </Tag>
                    </div>
                  </div>
                </Col>
                {(profile as any).speciality && (
                  <Col xs={24} sm={12}>
                    <div>
                      <Text type="secondary">Speciality</Text>
                      <div>
                        <Text>{(profile as any).speciality}</Text>
                      </div>
                    </div>
                  </Col>
                )}
              </Row>
            </Card>
          </>
        )}

        {profile.role === Role.TEACHER && (
          <>
            <Divider />
            <Card className="bg-blue-50" bordered={false}>
              <Title level={5} className="!mb-4">
                <IdcardOutlined className="mr-2" />
                Teaching Information
              </Title>
              <Row gutter={[24, 16]}>
                <Col xs={24}>
                  <div>
                    <Text type="secondary">Assigned Subjects</Text>
                    <div className="mt-2">
                      {(profile as any).subjects?.length > 0 ? (
                        (profile as any).subjects.map((subj: any, idx: number) => (
                          <Tag key={idx} color="blue" className="mb-1">
                            {subj.name || subj}
                          </Tag>
                        ))
                      ) : (
                        <Text type="secondary">No subjects assigned</Text>
                      )}
                    </div>
                  </div>
                </Col>
                <Col xs={24}>
                  <div>
                    <Text type="secondary">Teaching Levels</Text>
                    <div className="mt-2">
                      {(profile as any).levels?.length > 0 ? (
                        (profile as any).levels.map((level: string, idx: number) => (
                          <Tag key={idx} color="purple" className="mb-1">
                            {LEVEL_LABELS[level] || level}
                          </Tag>
                        ))
                      ) : (
                        <Text type="secondary">No levels assigned</Text>
                      )}
                    </div>
                  </div>
                </Col>
              </Row>
            </Card>
          </>
        )}

        {profile.role === Role.ADMIN && (
          <>
            <Divider />
            <Card className="bg-red-50" bordered={false}>
              <Title level={5} className="!mb-4">
                <IdcardOutlined className="mr-2" />
                Administrator Access
              </Title>
              <Text type="secondary">
                You have full administrative access to the system, including user management, 
                department configuration, subject management, and grade oversight.
              </Text>
            </Card>
          </>
        )}
      </Card>
    </div>
  );
};

export default ProfilePage;
