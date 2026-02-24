import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Select, InputNumber, Space, message, Card, Typography, Tag, Tabs, Empty, Upload } from 'antd';
import { PlusOutlined, ExclamationCircleOutlined, CheckCircleOutlined, CloseCircleOutlined, ClockCircleOutlined, UploadOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import type { UploadFile } from 'antd/es/upload';
import dayjs from 'dayjs';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useCreateRevendicationMutation,
  useGetStudentRevendicationsQuery,
} from '../api/revendicationApi';
import { useGetStudentTranscriptQuery } from '../../transcript/api/transcriptApi';
import type { RevendicationResponse, RevendicationRequest } from '../../../api/response-dto/revendication.dto';

const { Title, Text } = Typography;
const { TextArea } = Input;

const CLAIM_CAUSES = [
  { value: 'CALCULATION_ERROR', label: 'Calculation Error' },
  { value: 'MISSING_GRADE', label: 'Missing Grade' },
  { value: 'WRONG_ENTRY', label: 'Wrong Grade Entry' },
  { value: 'EXAM_CORRECTION', label: 'Exam Correction Issue' },
  { value: 'OTHER', label: 'Other' },
];

const PERIODS = [
  { value: 1, label: 'Continuous Assessment 1 (CC1)' },
  { value: 2, label: 'Continuous Assessment 2 (CC2)' },
  { value: 3, label: 'Session Normale 1 (Midterm)' },
  { value: 4, label: 'Session Normale 2 (Final)' },
];

const getStatusIcon = (status: string) => {
  switch (status) {
    case 'APPROVED': return <CheckCircleOutlined className="text-green-500" />;
    case 'REJECTED': return <CloseCircleOutlined className="text-red-500" />;
    default: return <ClockCircleOutlined className="text-orange-500" />;
  }
};

const getStatusColor = (status: string) => {
  switch (status) {
    case 'APPROVED': return 'green';
    case 'REJECTED': return 'red';
    default: return 'orange';
  }
};

interface ClaimFormValues {
  gradeId: number;
  requestedScore: number;
  examPeriodId: number;
  description: string;
}

export const StudentGradeClaimPage = () => {
  usePageTitle('Grade Claims');

  const { data: revendications, isLoading: claimsLoading } = useGetStudentRevendicationsQuery();
  const { data: transcript } = useGetStudentTranscriptQuery();
  const [createClaim, { isLoading: isCreating }] = useCreateRevendicationMutation();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [proofFile, setProofFile] = useState<UploadFile[]>([]);
  const [form] = Form.useForm<ClaimFormValues>();

  const handleOpenModal = () => {
    form.resetFields();
    setProofFile([]);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setProofFile([]);
    form.resetFields();
  };

  const handleSubmitClaim = async (values: ClaimFormValues) => {
    const payload: RevendicationRequest = {
      gradeId: values.gradeId,
      examPeriodId: values.examPeriodId,
      requestedScore: values.requestedScore,
      description: values.description,
    };

    try {
      const proof = proofFile[0]?.originFileObj;
      await createClaim({ request: payload, proof }).unwrap();
      message.success('Grade claim submitted successfully');
      handleCloseModal();
    } catch (error: any) {
      const msg = error?.data?.message || error?.message || 'An error occurred';
      message.error(msg);
    }
  };

  const pendingClaims = revendications?.filter(r => r.status === 'PENDING') || [];
  const resolvedClaims = revendications?.filter(r => r.status !== 'PENDING') || [];

  const claimColumns: ColumnsType<RevendicationResponse> = [
    {
      title: 'Subject',
      key: 'subject',
      render: (_, record) => (
        <div>
          <Text strong>{record.subjectName}</Text>
          <br />
          <Text type="secondary" className="text-xs">{record.subjectCode}</Text>
        </div>
      ),
    },
    {
      title: 'Period',
      dataIndex: 'periodLabel',
      key: 'periodLabel',
      render: (period: string) => <Tag>{period || 'N/A'}</Tag>,
    },
    {
      title: 'Current Score',
      dataIndex: 'currentScore',
      key: 'currentScore',
      width: 100,
      render: (score: number) => <Text>{score}/20</Text>,
    },
    {
      title: 'Requested Score',
      dataIndex: 'requestedScore',
      key: 'requestedScore',
      width: 120,
      render: (score: number) => <Text strong className="text-blue-600">{score}/20</Text>,
    },
    {
      title: 'Reason',
      dataIndex: 'cause',
      key: 'cause',
      render: (cause: string) => CLAIM_CAUSES.find(c => c.value === cause)?.label || cause,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: (status: string) => (
        <Tag color={getStatusColor(status)} icon={getStatusIcon(status)}>
          {status}
        </Tag>
      ),
    },
    {
      title: 'Submitted',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 120,
      render: (date: string) => date ? dayjs(date).format('MMM DD, YYYY') : '-',
    },
  ];

  const resolvedColumns: ColumnsType<RevendicationResponse> = [
    ...claimColumns,
    {
      title: 'Response',
      key: 'response',
      width: 200,
      render: (_, record) => (
        <div>
          {record.status === 'APPROVED' && record.teacherComment && (
            <Text type="success" className="text-xs">{record.teacherComment}</Text>
          )}
          {record.status === 'REJECTED' && record.rejectionReason && (
            <Text type="danger" className="text-xs">{record.rejectionReason}</Text>
          )}
          {record.resolvedAt && (
            <div>
              <Text type="secondary" className="text-xs">
                Resolved: {dayjs(record.resolvedAt).format('MMM DD, YYYY')}
              </Text>
            </div>
          )}
        </div>
      ),
    },
  ];

  const gradeOptions = (transcript?.studentGrades || []).map(g => ({
    value: g.gradeId,
    label: `${g.subject?.subjectName} (${g.subject?.subjectCode}) - ${g.totalScore}/20 - ${g.semester?.name}`,
  }));

  const tabItems = [
    {
      key: 'pending',
      label: (
        <span>
          <ClockCircleOutlined />
          Pending ({pendingClaims.length})
        </span>
      ),
      children: pendingClaims.length > 0 ? (
        <Table
          columns={claimColumns}
          dataSource={pendingClaims}
          rowKey="id"
          loading={claimsLoading}
          pagination={{ pageSize: 5 }}
        />
      ) : (
        <Empty description="No pending claims" />
      ),
    },
    {
      key: 'resolved',
      label: (
        <span>
          <CheckCircleOutlined />
          Resolved ({resolvedClaims.length})
        </span>
      ),
      children: resolvedClaims.length > 0 ? (
        <Table
          columns={resolvedColumns}
          dataSource={resolvedClaims}
          rowKey="id"
          loading={claimsLoading}
          pagination={{ pageSize: 5 }}
        />
      ) : (
        <Empty description="No resolved claims" />
      ),
    },
  ];

  return (
    <div className="p-4">
      <Card>
        <div className="flex justify-between items-center mb-6">
          <div>
            <Title level={3} className="!mb-1">
              <ExclamationCircleOutlined className="mr-2" />
              Grade Claims
            </Title>
            <Text type="secondary">
              Submit and track your grade dispute requests
            </Text>
          </div>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={handleOpenModal}
          >
            New Claim
          </Button>
        </div>

        <Tabs items={tabItems} />
      </Card>

      <Modal
        title="Submit Grade Claim"
        open={isModalOpen}
        onCancel={handleCloseModal}
        footer={null}
        destroyOnClose
        width={640}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmitClaim}
          autoComplete="off"
        >
          <Form.Item
            name="gradeId"
            label="Select Grade to Contest"
            rules={[{ required: true, message: 'Please select a grade' }]}
          >
            <Select
              placeholder="Sélectionnez la note à contester"
              showSearch
              optionFilterProp="label"
              options={gradeOptions}
              popupMatchSelectWidth={false}
            />
          </Form.Item>

          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="requestedScore"
              label="Requested Score"
              rules={[
                { required: true, message: 'Please enter requested score' },
                { type: 'number', min: 0, max: 20, message: 'Score must be between 0 and 20' },
              ]}
            >
              <InputNumber
                min={0}
                max={20}
                step={0.25}
                style={{ width: '100%' }}
                placeholder="e.g., 15.5"
                addonAfter="/20"
              />
            </Form.Item>

            <Form.Item
              name="examPeriodId"
              label="Assessment Period"
              rules={[{ required: true, message: 'Please select period' }]}
            >
              <Select placeholder="Période" options={PERIODS} popupMatchSelectWidth={false} />
            </Form.Item>
          </div>

          <Form.Item
            name="description"
            label="Detailed Description"
            rules={[
              { required: true, message: 'Please provide a description' },
              { min: 10, message: 'Description must be at least 10 characters' },
            ]}
          >
            <TextArea
              rows={4}
              placeholder="Expliquez pourquoi votre note devrait être modifiée."
              maxLength={500}
              showCount
              style={{ resize: 'vertical', wordBreak: 'break-word' }}
            />
          </Form.Item>

          <div className="mb-4">
            <label className="block mb-1 font-medium">Proof Image (optional)</label>
            <Upload
              listType="picture"
              maxCount={1}
              fileList={proofFile}
              beforeUpload={(file) => {
                const isImage = file.type.startsWith('image/');
                if (!isImage) { message.error('Only image files are accepted'); return Upload.LIST_IGNORE; }
                if (file.size > 5 * 1024 * 1024) { message.error('Image must be smaller than 5MB'); return Upload.LIST_IGNORE; }
                return false; // prevent auto upload
              }}
              onChange={({ fileList }) => setProofFile(fileList)}
            >
              {proofFile.length === 0 && (
                <Button icon={<UploadOutlined />}>Upload Proof</Button>
              )}
            </Upload>
            <Text type="warning" className="text-xs mt-1 block">
              ⚠️ Ensure the image is clear and legible. Blurry or unreadable proofs will be rejected.
            </Text>
          </div>

          <div className="bg-yellow-50 border border-yellow-200 rounded p-3 mb-4">
            <Text type="warning" className="text-sm">
              <ExclamationCircleOutlined className="mr-1" />
              Please ensure all information is accurate. False claims may result in disciplinary action.
            </Text>
          </div>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isCreating}
              >
                Submit Claim
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default StudentGradeClaimPage;
