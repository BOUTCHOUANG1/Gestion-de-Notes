import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, Popconfirm, message, Card, Typography } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { usePageTitle } from '../../../hooks/usePageTitle';
import {
  useGetDepartmentsQuery,
  useCreateDepartmentMutation,
  useUpdateDepartmentMutation,
  useDeleteDepartmentMutation,
} from '../api/departmentApi';
import type { DepartmentResponse, DepartmentRequest } from '../../../api/response-dto/department.dto';

const { Title } = Typography;

export const DepartmentManagementPage = () => {
  usePageTitle('Department Management');

  const { data: departments, isLoading, isFetching } = useGetDepartmentsQuery();
  const [createDepartment, { isLoading: isCreating }] = useCreateDepartmentMutation();
  const [updateDepartment, { isLoading: isUpdating }] = useUpdateDepartmentMutation();
  const [deleteDepartment, { isLoading: isDeleting }] = useDeleteDepartmentMutation();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDepartment, setEditingDepartment] = useState<DepartmentResponse | null>(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm<DepartmentRequest>();

  const filteredDepartments = departments?.filter((dept) =>
    dept.name.toLowerCase().includes(searchText.toLowerCase())
  );
  const handleOpenModal = (department?: DepartmentResponse) => {
    if (department) {
      setEditingDepartment(department);
      form.setFieldsValue({ name: department.name });
    } else {
      setEditingDepartment(null);
      form.resetFields();
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingDepartment(null);
    form.resetFields();
  };

  const handleSubmit = async (values: DepartmentRequest) => {
    try {
      if (editingDepartment) {
        await updateDepartment({ id: editingDepartment.id, ...values }).unwrap();
        message.success('Department updated successfully');
      } else {
        await createDepartment(values).unwrap();
        message.success('Department created successfully');
      }
      handleCloseModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(editingDepartment ? `Failed to update department: ${errorMessage}` : `Failed to create department: ${errorMessage}`);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteDepartment(id).unwrap();
      message.success('Department deleted successfully');
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to delete department: ${errorMessage}`);
    }
  };

  const columns: ColumnsType<DepartmentResponse> = [
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
      title: 'Created Date',
      dataIndex: 'creationDate',
      key: 'creationDate',
      render: (date: string) => (date ? new Date(date).toLocaleDateString() : '-'),
      sorter: (a, b) => {
        if (!a.creationDate || !b.creationDate) return 0;
        return new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime();
      },
    },
    {
      title: 'Last Modified',
      dataIndex: 'lastModifiedDate',
      key: 'lastModifiedDate',
      render: (date: string) => (date ? new Date(date).toLocaleDateString() : '-'),
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
            title="Edit department"
          />
          <Popconfirm
            title="Delete Department"
            description="Are you sure you want to delete this department?"
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
              title="Delete department"
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
            Department Management
          </Title>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => handleOpenModal()}
          >
            Add Department
          </Button>
        </div>

        <div className="mb-4">
          <Input
            placeholder="Search departments..."
            prefix={<SearchOutlined />}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            allowClear
            style={{ maxWidth: 300 }}
          />
        </div>

        <Table
          columns={columns}
          dataSource={filteredDepartments}
          rowKey="id"
          loading={isLoading || isFetching}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} departments`,
          }}
        />
      </Card>

      <Modal
        title={editingDepartment ? 'Edit Department' : 'Create Department'}
        open={isModalOpen}
        onCancel={handleCloseModal}
        footer={null}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          autoComplete="off"
        >
          <Form.Item
            name="name"
            label="Department Name"
            rules={[
              { required: true, message: 'Please enter department name' },
              { min: 2, message: 'Name must be at least 2 characters' },
              { max: 100, message: 'Name must not exceed 100 characters' },
            ]}
          >
            <Input placeholder="Enter department name" />
          </Form.Item>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isCreating || isUpdating}
              >
                {editingDepartment ? 'Update' : 'Create'}
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default DepartmentManagementPage;
