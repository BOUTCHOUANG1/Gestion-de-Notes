import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, message, Card, Typography, Tag, Tabs, Empty, Badge, Tooltip } from 'antd';
import { CheckOutlined, CloseOutlined, EyeOutlined, ExclamationCircleOutlined, CheckCircleOutlined, CloseCircleOutlined, ClockCircleOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { useGetProfileQuery } from '../../auth/api/authApi';
import {
  useGetTeacherRevendicationsQuery,
  useGetAdminRevendicationsQuery,
  useApproveRevendicationMutation,
  useRejectRevendicationMutation,
} from '../api/revendicationApi';
import type { RevendicationResponse } from '../../../api/response-dto/revendication.dto';

const { Title, Text, Paragraph } = Typography;
const { TextArea } = Input;

const getStatusColor = (status: string) => {
  switch (status) {
    case 'APPROVED': return 'green';
    case 'REJECTED': return 'red';
    default: return 'orange';
  }
};

const getStatusIcon = (status: string) => {
  switch (status) {
    case 'APPROVED': return <CheckCircleOutlined />;
    case 'REJECTED': return <CloseCircleOutlined />;
    default: return <ClockCircleOutlined />;
  }
};

const getStudentName = (r: RevendicationResponse) =>
  r.student ? `${r.student.firstName} ${r.student.lastName}` : 'Unknown';

const getSubjectName = (r: RevendicationResponse) =>
  r.grade?.subject?.subjectName ?? '-';

const getSubjectCode = (r: RevendicationResponse) =>
  r.grade?.subject?.subjectCode ?? '';

const getCurrentScore = (r: RevendicationResponse) =>
  (r as any).grade?.totalScore ?? 0;

export const GradeClaimsReviewPage = () => {
  usePageTitle('Review Grade Claims');

  const { data: profile } = useGetProfileQuery();
  const isTeacher = profile?.role === 'TEACHER';
  const isAdmin = profile?.role === 'ADMIN';

  const { data: teacherData, isLoading: teacherLoading, error: teacherError } = useGetTeacherRevendicationsQuery(undefined, { skip: !isTeacher });
  const { data: adminData, isLoading: adminLoading, error: adminError } = useGetAdminRevendicationsQuery(undefined, { skip: !isAdmin });

  const revendications = isAdmin ? adminData : teacherData;
  const isLoading = isAdmin ? adminLoading : teacherLoading;
  const error = isAdmin ? adminError : teacherError;

  const [approveClaim, { isLoading: isApproving }] = useApproveRevendicationMutation();
  const [rejectClaim, { isLoading: isRejecting }] = useRejectRevendicationMutation();

  const [selectedClaim, setSelectedClaim] = useState<RevendicationResponse | null>(null);
  const [actionType, setActionType] = useState<'approve' | 'reject' | 'view' | null>(null);
  const [form] = Form.useForm();

  const pendingClaims = revendications?.filter(r => r.status === 'PENDING') || [];
  const resolvedClaims = revendications?.filter(r => r.status !== 'PENDING') || [];

  const handleOpenModal = (claim: RevendicationResponse, action: 'approve' | 'reject' | 'view') => {
    setSelectedClaim(claim);
    setActionType(action);
    form.resetFields();
  };

  const handleCloseModal = () => {
    setSelectedClaim(null);
    setActionType(null);
    form.resetFields();
  };

  const handleApprove = async (values: { comment?: string }) => {
    if (!selectedClaim) return;
    try {
      await approveClaim({ id: selectedClaim.revendicationId, comment: values.comment }).unwrap();
      message.success('Claim approved successfully.');
      handleCloseModal();
    } catch {
      message.error('Failed to approve claim');
    }
  };

  const handleReject = async (values: { reason?: string }) => {
    if (!selectedClaim) return;
    try {
      await rejectClaim({ id: selectedClaim.revendicationId, reason: values.reason }).unwrap();
      message.success('Claim rejected successfully.');
      handleCloseModal();
    } catch {
      message.error('Failed to reject claim');
    }
  };

  const columns: ColumnsType<RevendicationResponse> = [
    {
      title: 'Student',
      key: 'student',
      render: (_, r) => (
        <div>
          <Text strong>{getStudentName(r)}</Text>
          {r.student?.matricule && <><br /><Text type="secondary" className="text-xs">{r.student.matricule}</Text></>}
        </div>
      ),
    },
    {
      title: 'Subject',
      key: 'subject',
      render: (_, r) => (
        <div>
          <Text>{getSubjectName(r)}</Text>
          <br /><Text type="secondary" className="text-xs">{getSubjectCode(r)}</Text>
        </div>
      ),
    },
    {
      title: 'Current',
      key: 'currentScore',
      width: 80,
      render: (_, r) => <Text>{getCurrentScore(r)}/20</Text>,
    },
    {
      title: 'Requested',
      key: 'requestedScore',
      width: 90,
      render: (_, r) => <Text strong className="text-blue-600">{r.requestedScore}/20</Text>,
    },
    {
      title: 'Diff',
      key: 'diff',
      width: 70,
      render: (_, r) => {
        const diff = r.requestedScore - getCurrentScore(r);
        return <Tag color={diff > 0 ? 'green' : 'red'}>{diff > 0 ? '+' : ''}{diff.toFixed(1)}</Tag>;
      },
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 110,
      render: (status: string) => (
        <Tag color={getStatusColor(status)} icon={getStatusIcon(status)}>{status}</Tag>
      ),
      filters: [
        { text: 'Pending', value: 'PENDING' },
        { text: 'Approved', value: 'APPROVED' },
        { text: 'Rejected', value: 'REJECTED' },
      ],
      onFilter: (value, record) => record.status === value,
    },
    {
      title: 'Date',
      key: 'createdDate',
      width: 100,
      render: (_, r) => r.createdDate ? dayjs(r.createdDate).format('MMM DD') : '-',
      sorter: (a, b) => {
        if (!a.createdDate || !b.createdDate) return 0;
        return dayjs(a.createdDate).unix() - dayjs(b.createdDate).unix();
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 150,
      render: (_, record) => (
        <Space size="small">
          <Tooltip title="View Details">
            <Button type="text" icon={<EyeOutlined />} onClick={() => handleOpenModal(record, 'view')} />
          </Tooltip>
          {record.status === 'PENDING' && isTeacher && (
            <>
              <Tooltip title="Approve">
                <Button type="text" className="text-green-600" icon={<CheckOutlined />} onClick={() => handleOpenModal(record, 'approve')} />
              </Tooltip>
              <Tooltip title="Reject">
                <Button type="text" danger icon={<CloseOutlined />} onClick={() => handleOpenModal(record, 'reject')} />
              </Tooltip>
            </>
          )}
        </Space>
      ),
    },
  ];

  const tabItems = [
    {
      key: 'pending',
      label: (
        <Badge count={pendingClaims.length} offset={[10, 0]}>
          <span className="pr-4"><ClockCircleOutlined className="mr-1" />Pending</span>
        </Badge>
      ),
      children: pendingClaims.length > 0 ? (
        <Table columns={columns} dataSource={pendingClaims} rowKey="revendicationId" loading={isLoading} pagination={{ pageSize: 10 }} />
      ) : (
        <Empty description="No pending claims" />
      ),
    },
    {
      key: 'resolved',
      label: <span><CheckCircleOutlined className="mr-1" />Resolved ({resolvedClaims.length})</span>,
      children: resolvedClaims.length > 0 ? (
        <Table columns={columns} dataSource={resolvedClaims} rowKey="revendicationId" loading={isLoading} pagination={{ pageSize: 10 }} />
      ) : (
        <Empty description="No resolved claims" />
      ),
    },
    {
      key: 'all',
      label: `All (${revendications?.length ?? 0})`,
      children: revendications?.length ? (
        <Table columns={columns} dataSource={revendications} rowKey="revendicationId" loading={isLoading} pagination={{ pageSize: 10 }} />
      ) : (
        <Empty description="No claims" />
      ),
    },
  ];

  if (!isTeacher && !isAdmin) {
    return (
      <div className="p-4">
        <Card>
          <div className="text-center py-8">
            <ExclamationCircleOutlined className="text-5xl text-orange-500 mb-4" />
            <Title level={4}>Access Restricted</Title>
            <Text type="secondary">This page is only accessible to teachers and admins.</Text>
          </div>
        </Card>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-4">
        <Card>
          <div className="text-center py-8">
            <CloseCircleOutlined className="text-5xl text-red-500 mb-4" />
            <Title level={4}>Error Loading Claims</Title>
            <Text type="secondary">
              {(error as any)?.data?.message ?? 'Failed to load grade claims.'}
            </Text>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div className="p-4">
      <Card>
        <div className="mb-6">
          <Title level={3} className="!mb-1">
            <ExclamationCircleOutlined className="mr-2" />
            Grade Claims {isAdmin ? '(Admin View)' : 'Review'}
          </Title>
          <Text type="secondary">
            {isAdmin ? 'Overview of all student grade dispute requests' : 'Review and process student grade dispute requests'}
          </Text>
        </div>
        <Tabs items={tabItems} />
      </Card>

      <Modal
        title={actionType === 'view' ? 'Claim Details' : actionType === 'approve' ? 'Approve Claim' : 'Reject Claim'}
        open={!!selectedClaim && !!actionType}
        onCancel={handleCloseModal}
        footer={actionType === 'view' ? <Button onClick={handleCloseModal}>Close</Button> : null}
        destroyOnClose
        width={500}
      >
        {selectedClaim && (
          <div>
            <div className="bg-gray-50 rounded p-4 mb-4">
              <div className="grid grid-cols-2 gap-3">
                <div><Text type="secondary">Student</Text><div><Text strong>{getStudentName(selectedClaim)}</Text></div></div>
                <div><Text type="secondary">Subject</Text><div><Text strong>{getSubjectName(selectedClaim)}</Text></div></div>
                <div><Text type="secondary">Current Score</Text><div><Text>{getCurrentScore(selectedClaim)}/20</Text></div></div>
                <div><Text type="secondary">Requested Score</Text><div><Text strong className="text-blue-600">{selectedClaim.requestedScore}/20</Text></div></div>
                <div><Text type="secondary">Status</Text><div><Tag color={getStatusColor(selectedClaim.status)}>{selectedClaim.status}</Tag></div></div>
                <div><Text type="secondary">Semester</Text><div><Text>{selectedClaim.semester?.semesterName || 'N/A'}</Text></div></div>
              </div>
              {selectedClaim.description && (
                <div className="mt-3 pt-3 border-t">
                  <Text type="secondary">Description</Text>
                  <Paragraph className="!mb-0 mt-1">{selectedClaim.description}</Paragraph>
                </div>
              )}
              {selectedClaim.teacherComment && (
                <div className="mt-3 pt-3 border-t">
                  <Text type="secondary">Teacher Comment</Text>
                  <Paragraph className="!mb-0 mt-1">{selectedClaim.teacherComment}</Paragraph>
                </div>
              )}
            </div>

            {actionType === 'approve' && (
              <Form form={form} layout="vertical" onFinish={handleApprove}>
                <Form.Item name="comment" label="Comment (Optional)">
                  <TextArea rows={3} placeholder="Add a comment" />
                </Form.Item>
                <Form.Item className="mb-0 flex justify-end">
                  <Space>
                    <Button onClick={handleCloseModal}>Cancel</Button>
                    <Button type="primary" htmlType="submit" loading={isApproving} className="bg-green-600">Approve</Button>
                  </Space>
                </Form.Item>
              </Form>
            )}

            {actionType === 'reject' && (
              <Form form={form} layout="vertical" onFinish={handleReject}>
                <Form.Item name="reason" label="Rejection Reason" rules={[{ required: true, message: 'Please provide a reason' }]}>
                  <TextArea rows={3} placeholder="Explain why" />
                </Form.Item>
                <Form.Item className="mb-0 flex justify-end">
                  <Space>
                    <Button onClick={handleCloseModal}>Cancel</Button>
                    <Button type="primary" danger htmlType="submit" loading={isRejecting}>Reject</Button>
                  </Space>
                </Form.Item>
              </Form>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
};

export default GradeClaimsReviewPage;
