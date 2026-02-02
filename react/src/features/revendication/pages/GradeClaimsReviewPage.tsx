import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, message, Card, Typography, Tag, Tabs, Empty, Badge, Tooltip } from 'antd';
import { CheckOutlined, CloseOutlined, EyeOutlined, ExclamationCircleOutlined, CheckCircleOutlined, CloseCircleOutlined, ClockCircleOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useGetTeacherRevendicationsQuery,
  useApproveRevendicationMutation,
  useRejectRevendicationMutation,
} from '../api/revendicationApi';
import type { RevendicationResponse } from '../../../api/response-dto/revendication.dto';

const { Title, Text, Paragraph } = Typography;
const { TextArea } = Input;

const CLAIM_CAUSES: Record<string, string> = {
  'CALCULATION_ERROR': 'Calculation Error',
  'MISSING_GRADE': 'Missing Grade',
  'WRONG_ENTRY': 'Wrong Grade Entry',
  'EXAM_CORRECTION': 'Exam Correction Issue',
  'OTHER': 'Other',
};

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

export const GradeClaimsReviewPage = () => {
  usePageTitle('Review Grade Claims');

  const { data: revendications, isLoading } = useGetTeacherRevendicationsQuery();
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
      await approveClaim({ id: selectedClaim.id, comment: values.comment }).unwrap();
      message.success('Claim approved successfully. Grade has been updated.');
      handleCloseModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to approve claim: ${errorMessage}`);
    }
  };

  const handleReject = async (values: { reason?: string }) => {
    if (!selectedClaim) return;
    try {
      await rejectClaim({ id: selectedClaim.id, reason: values.reason }).unwrap();
      message.success('Claim rejected successfully.');
      handleCloseModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to reject claim: ${errorMessage}`);
    }
  };

  const pendingColumns: ColumnsType<RevendicationResponse> = [
    {
      title: 'Student',
      dataIndex: 'studentName',
      key: 'studentName',
      render: (name: string, record) => (
        <div>
          <Text strong>{name}</Text>
          <br />
          <Text type="secondary" className="text-xs">ID: {record.studentId}</Text>
        </div>
      ),
    },
    {
      title: 'Subject',
      key: 'subject',
      render: (_, record) => (
        <div>
          <Text>{record.subjectName}</Text>
          <br />
          <Text type="secondary" className="text-xs">{record.subjectCode}</Text>
        </div>
      ),
    },
    {
      title: 'Period',
      dataIndex: 'periodLabel',
      key: 'periodLabel',
      width: 100,
      render: (period: string) => <Tag>{period || 'N/A'}</Tag>,
    },
    {
      title: 'Current',
      dataIndex: 'currentScore',
      key: 'currentScore',
      width: 80,
      render: (score: number) => <Text>{score}/20</Text>,
    },
    {
      title: 'Requested',
      dataIndex: 'requestedScore',
      key: 'requestedScore',
      width: 90,
      render: (score: number) => <Text strong className="text-blue-600">{score}/20</Text>,
    },
    {
      title: 'Difference',
      key: 'difference',
      width: 90,
      render: (_, record) => {
        const diff = record.requestedScore - record.currentScore;
        return (
          <Tag color={diff > 0 ? 'green' : 'red'}>
            {diff > 0 ? '+' : ''}{diff.toFixed(2)}
          </Tag>
        );
      },
    },
    {
      title: 'Reason',
      dataIndex: 'cause',
      key: 'cause',
      render: (cause: string) => CLAIM_CAUSES[cause] || cause,
    },
    {
      title: 'Submitted',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 100,
      render: (date: string) => date ? dayjs(date).format('MMM DD') : '-',
      sorter: (a, b) => {
        if (!a.createdAt || !b.createdAt) return 0;
        return dayjs(a.createdAt).unix() - dayjs(b.createdAt).unix();
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 150,
      render: (_, record) => (
        <Space size="small">
          <Tooltip title="View Details">
            <Button
              type="text"
              icon={<EyeOutlined />}
              onClick={() => handleOpenModal(record, 'view')}
            />
          </Tooltip>
          <Tooltip title="Approve">
            <Button
              type="text"
              className="text-green-600"
              icon={<CheckOutlined />}
              onClick={() => handleOpenModal(record, 'approve')}
            />
          </Tooltip>
          <Tooltip title="Reject">
            <Button
              type="text"
              danger
              icon={<CloseOutlined />}
              onClick={() => handleOpenModal(record, 'reject')}
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  const resolvedColumns: ColumnsType<RevendicationResponse> = [
    {
      title: 'Student',
      dataIndex: 'studentName',
      key: 'studentName',
    },
    {
      title: 'Subject',
      key: 'subject',
      render: (_, record) => `${record.subjectName} (${record.subjectCode})`,
    },
    {
      title: 'Score Change',
      key: 'scoreChange',
      render: (_, record) => (
        <span>
          {record.currentScore} → {record.requestedScore}
        </span>
      ),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={getStatusColor(status)} icon={getStatusIcon(status)}>
          {status}
        </Tag>
      ),
      filters: [
        { text: 'Approved', value: 'APPROVED' },
        { text: 'Rejected', value: 'REJECTED' },
      ],
      onFilter: (value, record) => record.status === value,
    },
    {
      title: 'Response',
      key: 'response',
      render: (_, record) => (
        <Text type="secondary" className="text-xs">
          {record.status === 'APPROVED' ? record.teacherComment : record.rejectionReason || '-'}
        </Text>
      ),
    },
    {
      title: 'Resolved',
      dataIndex: 'resolvedAt',
      key: 'resolvedAt',
      render: (date: string) => date ? dayjs(date).format('MMM DD, YYYY') : '-',
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 80,
      render: (_, record) => (
        <Button
          type="text"
          icon={<EyeOutlined />}
          onClick={() => handleOpenModal(record, 'view')}
        />
      ),
    },
  ];

  const tabItems = [
    {
      key: 'pending',
      label: (
        <Badge count={pendingClaims.length} offset={[10, 0]}>
          <span className="pr-4">
            <ClockCircleOutlined className="mr-1" />
            Pending Review
          </span>
        </Badge>
      ),
      children: pendingClaims.length > 0 ? (
        <Table
          columns={pendingColumns}
          dataSource={pendingClaims}
          rowKey="id"
          loading={isLoading}
          pagination={{ pageSize: 10 }}
        />
      ) : (
        <Empty description="No pending claims to review" />
      ),
    },
    {
      key: 'resolved',
      label: (
        <span>
          <CheckCircleOutlined className="mr-1" />
          Resolved ({resolvedClaims.length})
        </span>
      ),
      children: resolvedClaims.length > 0 ? (
        <Table
          columns={resolvedColumns}
          dataSource={resolvedClaims}
          rowKey="id"
          loading={isLoading}
          pagination={{ pageSize: 10 }}
        />
      ) : (
        <Empty description="No resolved claims" />
      ),
    },
  ];

  return (
    <div className="p-4">
      <Card>
        <div className="mb-6">
          <Title level={3} className="!mb-1">
            <ExclamationCircleOutlined className="mr-2" />
            Grade Claims Review
          </Title>
          <Text type="secondary">
            Review and process student grade dispute requests
          </Text>
        </div>

        <Tabs items={tabItems} />
      </Card>

      <Modal
        title={
          actionType === 'view' ? 'Claim Details' :
          actionType === 'approve' ? 'Approve Claim' :
          'Reject Claim'
        }
        open={!!selectedClaim && !!actionType}
        onCancel={handleCloseModal}
        footer={actionType === 'view' ? (
          <Button onClick={handleCloseModal}>Close</Button>
        ) : null}
        destroyOnClose
        width={500}
      >
        {selectedClaim && (
          <div>
            <div className="bg-gray-50 rounded p-4 mb-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <Text type="secondary">Student</Text>
                  <div><Text strong>{selectedClaim.studentName}</Text></div>
                </div>
                <div>
                  <Text type="secondary">Subject</Text>
                  <div><Text strong>{selectedClaim.subjectName}</Text></div>
                </div>
                <div>
                  <Text type="secondary">Current Score</Text>
                  <div><Text>{selectedClaim.currentScore}/20</Text></div>
                </div>
                <div>
                  <Text type="secondary">Requested Score</Text>
                  <div><Text strong className="text-blue-600">{selectedClaim.requestedScore}/20</Text></div>
                </div>
                <div>
                  <Text type="secondary">Reason</Text>
                  <div><Tag>{CLAIM_CAUSES[selectedClaim.cause] || selectedClaim.cause}</Tag></div>
                </div>
                <div>
                  <Text type="secondary">Period</Text>
                  <div><Text>{selectedClaim.periodLabel || 'N/A'}</Text></div>
                </div>
              </div>
              {selectedClaim.description && (
                <div className="mt-3 pt-3 border-t">
                  <Text type="secondary">Description</Text>
                  <Paragraph className="!mb-0 mt-1">{selectedClaim.description}</Paragraph>
                </div>
              )}
            </div>

            {actionType === 'approve' && (
              <Form form={form} layout="vertical" onFinish={handleApprove}>
                <div className="bg-green-50 border border-green-200 rounded p-3 mb-4">
                  <Text className="text-green-700">
                    <CheckCircleOutlined className="mr-1" />
                    Approving this claim will update the student's grade from{' '}
                    <strong>{selectedClaim.currentScore}</strong> to{' '}
                    <strong>{selectedClaim.requestedScore}</strong>.
                  </Text>
                </div>
                <Form.Item name="comment" label="Comment (Optional)">
                  <TextArea rows={3} placeholder="Add a comment for the student (optional)" />
                </Form.Item>
                <Form.Item className="mb-0 flex justify-end">
                  <Space>
                    <Button onClick={handleCloseModal}>Cancel</Button>
                    <Button type="primary" htmlType="submit" loading={isApproving} className="bg-green-600">
                      Approve & Update Grade
                    </Button>
                  </Space>
                </Form.Item>
              </Form>
            )}

            {actionType === 'reject' && (
              <Form form={form} layout="vertical" onFinish={handleReject}>
                <div className="bg-red-50 border border-red-200 rounded p-3 mb-4">
                  <Text className="text-red-700">
                    <CloseCircleOutlined className="mr-1" />
                    Rejecting this claim will keep the current grade unchanged.
                  </Text>
                </div>
                <Form.Item
                  name="reason"
                  label="Rejection Reason"
                  rules={[{ required: true, message: 'Please provide a reason for rejection' }]}
                >
                  <TextArea rows={3} placeholder="Explain why the claim is being rejected" />
                </Form.Item>
                <Form.Item className="mb-0 flex justify-end">
                  <Space>
                    <Button onClick={handleCloseModal}>Cancel</Button>
                    <Button type="primary" danger htmlType="submit" loading={isRejecting}>
                      Reject Claim
                    </Button>
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
