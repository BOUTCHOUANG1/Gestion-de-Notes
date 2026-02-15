import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, Popconfirm, message, Card, Typography, Select, InputNumber, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined, BookOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useGetSubjectsQuery,
  useCreateSubjectMutation,
  useUpdateSubjectMutation,
  useDeleteSubjectMutation,
} from '../api/subjectApi';
import { useGetTeachersQuery } from '../api/teachersApi';
import { useGetDepartmentsQuery } from '../api/departmentApi';
import type { SubjectResponse, SubjectRequest } from '../../../api/response-dto/subject.dto';

const { Title } = Typography;
const { TextArea } = Input;

const LEVELS = [
  { value: 'LEVEL1', label: 'Licence 1' },
  { value: 'LEVEL2', label: 'Licence 2' },
  { value: 'LEVEL3', label: 'Licence 3' },
  { value: 'LEVEL4', label: 'Master 1' },
  { value: 'LEVEL5', label: 'Master 2' },
];

const CYCLES = [
  { value: 'BACHELOR', label: 'Bachelor (Licence)' },
  { value: 'MASTER', label: 'Master' },
  { value: 'PHD', label: 'PhD (Doctorate)' },
];

const getLevelLabel = (level: string) => LEVELS.find(l => l.value === level)?.label || level;
const getCycleLabel = (cycle: string) => CYCLES.find(c => c.value === cycle)?.label || cycle;

export const SubjectManagementPage = () => {
  usePageTitle('Subject Management');

  const { data: subjects, isLoading, isFetching } = useGetSubjectsQuery();
  const { data: teachers } = useGetTeachersQuery();
  const { data: departments } = useGetDepartmentsQuery();
  const [createSubject, { isLoading: isCreating }] = useCreateSubjectMutation();
  const [updateSubject, { isLoading: isUpdating }] = useUpdateSubjectMutation();
  const [deleteSubject, { isLoading: isDeleting }] = useDeleteSubjectMutation();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSubject, setEditingSubject] = useState<SubjectResponse | null>(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm<SubjectRequest>();

  const filteredSubjects = subjects?.filter((s) => {
    const q = searchText.toLowerCase();
    return (
      s.subjectName.toLowerCase().includes(q) ||
      s.subjectCode.toLowerCase().includes(q) ||
      (s.teacher && `${s.teacher.firstName} ${s.teacher.lastName}`.toLowerCase().includes(q))
    );
  });

  const handleOpenModal = (subject?: SubjectResponse) => {
    if (subject) {
      setEditingSubject(subject);
      form.setFieldsValue({
        subjectName: subject.subjectName,
        subjectCode: subject.subjectCode,
        credits: subject.credits,
        description: subject.description,
        subjectsLevel: subject.subjectsLevel?.map(l => l.studentLevel) ?? [],
        Studentcycle: subject.studentCycle,
        teacherId: subject.teacher?.teacherId,
        departmentId: subject.departmentId,
      });
    } else {
      setEditingSubject(null);
      form.resetFields();
      form.setFieldsValue({ credits: 3 });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingSubject(null);
    form.resetFields();
  };

  const handleSubmit = async (values: SubjectRequest) => {
    try {
      if (editingSubject) {
        await updateSubject({ id: editingSubject.subjectId, ...values }).unwrap();
        message.success('Subject updated successfully');
      } else {
        await createSubject(values).unwrap();
        message.success('Subject created successfully');
      }
      handleCloseModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(editingSubject ? `Failed to update: ${errorMessage}` : `Failed to create: ${errorMessage}`);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteSubject(id).unwrap();
      message.success('Subject deleted successfully');
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to delete: ${errorMessage}`);
    }
  };

  const columns: ColumnsType<SubjectResponse> = [
    {
      title: 'Code',
      dataIndex: 'subjectCode',
      key: 'subjectCode',
      width: 100,
      sorter: (a, b) => a.subjectCode.localeCompare(b.subjectCode),
    },
    {
      title: 'Name',
      dataIndex: 'subjectName',
      key: 'subjectName',
      sorter: (a, b) => a.subjectName.localeCompare(b.subjectName),
    },
    {
      title: 'Level',
      key: 'subjectsLevel',
      render: (_, record) => (
        <Space size={2} wrap>
          {record.subjectsLevel?.map(l => (
            <Tag color="blue" key={l.teachingLevelId}>{getLevelLabel(l.studentLevel)}</Tag>
          )) || '-'}
        </Space>
      ),
      filters: LEVELS.map(l => ({ text: l.label, value: l.value })),
      onFilter: (value, record) => record.subjectsLevel?.some(l => l.studentLevel === value) ?? false,
    },
    {
      title: 'Cycle',
      dataIndex: 'studentCycle',
      key: 'studentCycle',
      render: (cycle: string) => cycle ? <Tag color="purple">{getCycleLabel(cycle)}</Tag> : '-',
      filters: CYCLES.map(c => ({ text: c.label, value: c.value })),
      onFilter: (value, record) => record.studentCycle === value,
    },
    {
      title: 'Credits',
      dataIndex: 'credits',
      key: 'credits',
      width: 80,
      sorter: (a, b) => a.credits - b.credits,
    },
    {
      title: 'Teacher',
      key: 'teacher',
      render: (_, record) =>
        record.teacher
          ? `${record.teacher.firstName} ${record.teacher.lastName}`
          : <span className="text-gray-400">Not assigned</span>,
    },
    {
      title: 'Department',
      key: 'departmentId',
      render: (_, record) => {
        const dept = departments?.find(d => d.departmentId === record.departmentId);
        return dept?.departmentName || String(record.departmentId ?? '-');
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 150,
      render: (_, record) => (
        <Space size="small">
          <Button
            type="text"
            icon={<EditOutlined />}
            onClick={() => handleOpenModal(record)}
            title="Edit subject"
          />
          <Popconfirm
            title="Delete Subject"
            description="Are you sure you want to delete this subject?"
            onConfirm={() => handleDelete(record.subjectId)}
            okText="Yes"
            cancelText="No"
            okButtonProps={{ danger: true }}
          >
            <Button
              type="text"
              danger
              icon={<DeleteOutlined />}
              loading={isDeleting}
              title="Delete subject"
            />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div className="p-4">
      <Card>
        <div className="flex justify-between items-center mb-6">
          <Title level={3} className="!mb-0">
            <BookOutlined className="mr-2" />
            Subject Management
          </Title>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => handleOpenModal()}
          >
            Add Subject
          </Button>
        </div>

        <div className="mb-4">
          <Input
            placeholder="Search by name, code, or teacher..."
            prefix={<SearchOutlined />}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            allowClear
            style={{ maxWidth: 350 }}
          />
        </div>

        <Table
          columns={columns}
          dataSource={filteredSubjects}
          rowKey="subjectId"
          loading={isLoading || isFetching}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} subjects`,
          }}
          scroll={{ x: 1000 }}
        />
      </Card>

      <Modal
        title={editingSubject ? 'Edit Subject' : 'Create Subject'}
        open={isModalOpen}
        onCancel={handleCloseModal}
        footer={null}
        destroyOnClose
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          autoComplete="off"
          initialValues={{ credits: 3 }}
        >
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="subjectName"
              label="Subject Name"
              rules={[{ required: true, message: 'Please enter subject name' }]}
            >
              <Input placeholder="e.g., Introduction to Programming" />
            </Form.Item>

            <Form.Item
              name="subjectCode"
              label="Subject Code"
              rules={[
                { required: true, message: 'Please enter subject code' },
                { pattern: /^[A-Z0-9]+$/, message: 'Uppercase letters and numbers only' },
              ]}
            >
              <Input placeholder="e.g., CS101" style={{ textTransform: 'uppercase' }} />
            </Form.Item>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="subjectsLevel"
              label="Level(s)"
              rules={[{ required: true, message: 'Please select at least one level' }]}
            >
              <Select mode="multiple" placeholder="Select level(s)" options={LEVELS} />
            </Form.Item>

            <Form.Item
              name="Studentcycle"
              label="Cycle"
              rules={[{ required: true, message: 'Please select cycle' }]}
            >
              <Select placeholder="Select cycle" options={CYCLES} />
            </Form.Item>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="credits"
              label="Credits"
              rules={[{ required: true, message: 'Please enter credits' }]}
            >
              <InputNumber min={1} max={10} style={{ width: '100%' }} />
            </Form.Item>

            <Form.Item name="teacherId" label="Assigned Teacher">
              <Select
                placeholder="Select teacher"
                allowClear
                showSearch
                optionFilterProp="label"
                options={teachers?.map(t => ({
                  value: t.teacherId,
                  label: `${t.firstName} ${t.lastName}`,
                }))}
              />
            </Form.Item>
          </div>

          <Form.Item name="departmentId" label="Department">
            <Select
              placeholder="Select department"
              allowClear
              showSearch
              optionFilterProp="label"
              options={departments?.map(d => ({
                value: d.departmentId,
                label: d.departmentName,
              }))}
            />
          </Form.Item>

          <Form.Item
            name="description"
            label="Description"
            rules={[{ required: true, message: 'Description is required' }, { min: 10, message: 'At least 10 characters' }]}
          >
            <TextArea rows={3} placeholder="Description of the subject" />
          </Form.Item>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isCreating || isUpdating}
              >
                {editingSubject ? 'Update' : 'Create'}
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default SubjectManagementPage;
