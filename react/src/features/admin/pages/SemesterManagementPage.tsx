import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, Popconfirm, message, Card, Typography, DatePicker, Switch, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined, CalendarOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useGetSemestersQuery,
  useCreateSemesterMutation,
  useUpdateSemesterMutation,
  useDeleteSemesterMutation,
} from '../api/semesterApi';
import type { SemesterResponse, SemesterRequest } from '../../../api/response-dto/semester.dto';

const { Title } = Typography;
const { RangePicker } = DatePicker;

interface SemesterFormValues {
  name: string;
  dateRange: [dayjs.Dayjs, dayjs.Dayjs];
  active: boolean;
  orderIndex?: number;
}

export const SemesterManagementPage = () => {
  usePageTitle('Semester Management');

  const { data: semesters, isLoading, isFetching } = useGetSemestersQuery();
  const [createSemester, { isLoading: isCreating }] = useCreateSemesterMutation();
  const [updateSemester, { isLoading: isUpdating }] = useUpdateSemesterMutation();
  const [deleteSemester, { isLoading: isDeleting }] = useDeleteSemesterMutation();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSemester, setEditingSemester] = useState<SemesterResponse | null>(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm<SemesterFormValues>();

  const filteredSemesters = semesters?.filter((sem) =>
    sem.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const handleOpenModal = (semester?: SemesterResponse) => {
    if (semester) {
      setEditingSemester(semester);
      form.setFieldsValue({
        name: semester.name,
        dateRange: [dayjs(semester.startDate), dayjs(semester.endDate)],
        active: semester.active,
        orderIndex: semester.orderIndex,
      });
    } else {
      setEditingSemester(null);
      form.resetFields();
      form.setFieldsValue({ active: true });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingSemester(null);
    form.resetFields();
  };

  const handleSubmit = async (values: SemesterFormValues) => {
    const payload: SemesterRequest = {
      name: values.name,
      startDate: values.dateRange[0].format('YYYY-MM-DD'),
      endDate: values.dateRange[1].format('YYYY-MM-DD'),
      active: values.active,
      orderIndex: values.orderIndex,
    };

    try {
      if (editingSemester) {
        await updateSemester({ id: editingSemester.id, ...payload }).unwrap();
        message.success('Semester updated successfully');
      } else {
        await createSemester(payload).unwrap();
        message.success('Semester created successfully');
      }
      handleCloseModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(editingSemester ? `Failed to update semester: ${errorMessage}` : `Failed to create semester: ${errorMessage}`);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteSemester(id).unwrap();
      message.success('Semester deleted successfully');
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to delete semester: ${errorMessage}`);
    }
  };

  const columns: ColumnsType<SemesterResponse> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
      sorter: (a, b) => a.id - b.id,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: 'Start Date',
      dataIndex: 'startDate',
      key: 'startDate',
      render: (date: string) => dayjs(date).format('MMM DD, YYYY'),
      sorter: (a, b) => dayjs(a.startDate).unix() - dayjs(b.startDate).unix(),
    },
    {
      title: 'End Date',
      dataIndex: 'endDate',
      key: 'endDate',
      render: (date: string) => dayjs(date).format('MMM DD, YYYY'),
      sorter: (a, b) => dayjs(a.endDate).unix() - dayjs(b.endDate).unix(),
    },
    {
      title: 'Status',
      dataIndex: 'active',
      key: 'active',
      width: 100,
      render: (active: boolean) => (
        <Tag color={active ? 'green' : 'default'}>
          {active ? 'Active' : 'Inactive'}
        </Tag>
      ),
      filters: [
        { text: 'Active', value: true },
        { text: 'Inactive', value: false },
      ],
      onFilter: (value, record) => record.active === value,
    },
    {
      title: 'Duration',
      key: 'duration',
      render: (_, record) => {
        const days = dayjs(record.endDate).diff(dayjs(record.startDate), 'day');
        return `${days} days`;
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
            title="Edit semester"
          />
          <Popconfirm
            title="Delete Semester"
            description="Are you sure you want to delete this semester? This may affect related grades."
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
            okButtonProps={{ danger: true }}
          >
            <Button
              type="text"
              danger
              icon={<DeleteOutlined />}
              loading={isDeleting}
              title="Delete semester"
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
            <CalendarOutlined className="mr-2" />
            Semester Management
          </Title>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => handleOpenModal()}
          >
            Add Semester
          </Button>
        </div>

        <div className="mb-4">
          <Input
            placeholder="Search semesters..."
            prefix={<SearchOutlined />}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            allowClear
            style={{ maxWidth: 300 }}
          />
        </div>

        <Table
          columns={columns}
          dataSource={filteredSemesters}
          rowKey="id"
          loading={isLoading || isFetching}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} semesters`,
          }}
        />
      </Card>

      <Modal
        title={editingSemester ? 'Edit Semester' : 'Create Semester'}
        open={isModalOpen}
        onCancel={handleCloseModal}
        footer={null}
        destroyOnClose
        width={500}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          autoComplete="off"
          initialValues={{ active: true }}
        >
          <Form.Item
            name="name"
            label="Semester Name"
            rules={[
              { required: true, message: 'Please enter semester name' },
              { min: 2, message: 'Name must be at least 2 characters' },
            ]}
          >
            <Input placeholder="e.g., Semester 1 2024-2025" />
          </Form.Item>

          <Form.Item
            name="dateRange"
            label="Date Range"
            rules={[{ required: true, message: 'Please select date range' }]}
          >
            <RangePicker
              style={{ width: '100%' }}
              format="YYYY-MM-DD"
              placeholder={['Start Date', 'End Date']}
            />
          </Form.Item>

          <Form.Item
            name="orderIndex"
            label="Order Index"
            tooltip="Determines the display order of semesters"
          >
            <Input type="number" placeholder="e.g., 1, 2, 3..." />
          </Form.Item>

          <Form.Item
            name="active"
            label="Active"
            valuePropName="checked"
          >
            <Switch checkedChildren="Active" unCheckedChildren="Inactive" />
          </Form.Item>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isCreating || isUpdating}
              >
                {editingSemester ? 'Update' : 'Create'}
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default SemesterManagementPage;
