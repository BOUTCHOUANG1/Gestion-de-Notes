import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Space, Popconfirm, message, Card, Typography, Select, Tag, Tabs } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined, UserOutlined, TeamOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { usePageTitle } from '../../../hooks/usePageTitle';
import { useGetStudentsQuery, useUpdateStudentMutation, useDeleteStudentMutation } from '../../students/api/studentsApi';
import { useGetTeachersQuery, useUpdateTeacherMutation } from '../api/teachersApi';
import { useRegisterMutation } from '../../auth/api/authApi';
import type { studentResDto } from '../../../api/reponse-dto/user.res.dto';
import type { TeacherResponse, TeacherRequest } from '../../../api/response-dto/teacher.dto';
import type { RegisterReqDto } from '../../../api/request-dto/auth.req';
import { Role } from '../../../api/enums';

const { Title } = Typography;

const LEVELS = [
  { value: 1, label: 'Licence 1' },
  { value: 2, label: 'Licence 2' },
  { value: 3, label: 'Licence 3' },
  { value: 4, label: 'Master 1' },
  { value: 5, label: 'Master 2' },
];

const ROLES = [
  { value: Role.STUDENT, label: 'Student' },
  { value: Role.TEACHER, label: 'Teacher' },
  { value: Role.ADMIN, label: 'Admin' },
];

const getRoleColor = (role: string) => {
  switch (role) {
    case 'ADMIN': return 'red';
    case 'TEACHER': return 'blue';
    case 'STUDENT': return 'green';
    default: return 'default';
  }
};

export const UserManagementPage = () => {
  usePageTitle('User Management');

  const { data: students, isLoading: studentsLoading, isFetching: studentsFetching } = useGetStudentsQuery();
  const { data: teachers, isLoading: teachersLoading, isFetching: teachersFetching } = useGetTeachersQuery();
  const [registerUser, { isLoading: isRegistering }] = useRegisterMutation();
  const [updateTeacher, { isLoading: isUpdating }] = useUpdateTeacherMutation();
  const [updateStudent] = useUpdateStudentMutation();
  const [deleteStudent] = useDeleteStudentMutation();

  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isEditTeacherModalOpen, setIsEditTeacherModalOpen] = useState(false);
  const [editingTeacher, setEditingTeacher] = useState<TeacherResponse | null>(null);
  const [searchText, setSearchText] = useState('');
  const [activeTab, setActiveTab] = useState('students');
  const [createForm] = Form.useForm<RegisterReqDto>();
  const [editTeacherForm] = Form.useForm<TeacherRequest>();
  const [isEditStudentModalOpen, setIsEditStudentModalOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<studentResDto | null>(null);
  const [editStudentForm] = Form.useForm();

  const filteredStudents = students?.filter((s) =>
    (s.firstName || '').toLowerCase().includes(searchText.toLowerCase()) ||
    (s.lastName || '').toLowerCase().includes(searchText.toLowerCase()) ||
    (s.email || '').toLowerCase().includes(searchText.toLowerCase()) ||
    (s.matricule || '').toLowerCase().includes(searchText.toLowerCase())
  );

  const filteredTeachers = teachers?.filter((t) =>
    t.firstName.toLowerCase().includes(searchText.toLowerCase()) ||
    t.lastName.toLowerCase().includes(searchText.toLowerCase()) ||
    t.email.toLowerCase().includes(searchText.toLowerCase()) ||
    t.username.toLowerCase().includes(searchText.toLowerCase())
  );

  const handleOpenEditStudentModal = (student: studentResDto) => {
    setEditingStudent(student);
    editStudentForm.setFieldsValue({
      firstName: student.firstName,
      lastName: student.lastName,
      email: student.email,
    });
    setIsEditStudentModalOpen(true);
  };

  const handleUpdateStudent = async (values: any) => {
    if (!editingStudent) return;
    try {
      await updateStudent({ id: editingStudent.id, ...values }).unwrap();
      message.success('Student updated');
      setIsEditStudentModalOpen(false);
      setEditingStudent(null);
    } catch { message.error('Failed to update student'); }
  };

  const handleDeleteStudent = async (id: number) => {
    try {
      await deleteStudent(id).unwrap();
      message.success('Student deleted');
    } catch { message.error('Failed to delete student'); }
  };

  const handleOpenCreateModal = () => {
    createForm.resetFields();
    setIsCreateModalOpen(true);
  };

  const handleCloseCreateModal = () => {
    setIsCreateModalOpen(false);
    createForm.resetFields();
  };

  const handleOpenEditTeacherModal = (teacher: TeacherResponse) => {
    setEditingTeacher(teacher);
    editTeacherForm.setFieldsValue({
      firstName: teacher.firstName,
      lastName: teacher.lastName,
      email: teacher.email,
    });
    setIsEditTeacherModalOpen(true);
  };

  const handleCloseEditTeacherModal = () => {
    setIsEditTeacherModalOpen(false);
    setEditingTeacher(null);
    editTeacherForm.resetFields();
  };

  const handleCreateUser = async (values: RegisterReqDto) => {
    try {
      const payload = { ...values, username: values.matricule || values.email.split('@')[0] };
      await registerUser(payload).unwrap();
      message.success('User created successfully');
      handleCloseCreateModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to create user: ${errorMessage}`);
    }
  };

  const handleUpdateTeacher = async (values: TeacherRequest) => {
    if (!editingTeacher) return;
    try {
      await updateTeacher({ id: editingTeacher.id, ...values }).unwrap();
      message.success('Teacher updated successfully');
      handleCloseEditTeacherModal();
    } catch (error: unknown) {
      const errorMessage = error instanceof Error ? error.message : 'An error occurred';
      message.error(`Failed to update teacher: ${errorMessage}`);
    }
  };

  const studentColumns: ColumnsType<studentResDto> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 70,
      sorter: (a, b) => a.id - b.id,
    },
    {
      title: 'Matricule',
      dataIndex: 'matricule',
      key: 'matricule',
      sorter: (a, b) => (a.matricule || '').localeCompare(b.matricule || ''),
    },
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
      sorter: (a, b) => a.firstName.localeCompare(b.firstName),
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
      sorter: (a, b) => a.lastName.localeCompare(b.lastName),
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Level',
      dataIndex: 'studentLevel',
      key: 'studentLevel',
      width: 120,
      render: (level: any) => {
        const name = typeof level === 'string' ? level : level?.studentLevel || '';
        const labels: Record<string, string> = { LEVEL1: 'Licence 1', LEVEL2: 'Licence 2', LEVEL3: 'Licence 3', LEVEL4: 'Master 1', LEVEL5: 'Master 2' };
        return name ? <Tag color="purple">{labels[name] || name}</Tag> : <span className="text-gray-400">-</span>;
      },
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      width: 100,
      render: (role: string) => <Tag color={getRoleColor(role)}>{role}</Tag>,
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 120,
      render: (_, record) => (
        <Space size="small">
          <Button type="text" icon={<EditOutlined />} onClick={() => handleOpenEditStudentModal(record)} title="Edit" />
          <Popconfirm title="Delete student?" onConfirm={() => handleDeleteStudent(record.id)} okText="Yes" cancelText="No" okButtonProps={{ danger: true }}>
            <Button type="text" danger icon={<DeleteOutlined />} title="Delete" />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const teacherColumns: ColumnsType<TeacherResponse> = [
    {
      title: 'ID',
      dataIndex: 'teacherId',
      key: 'teacherId',
      width: 70,
      sorter: (a, b) => (a as any).teacherId - (b as any).teacherId,
    },
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username',
      sorter: (a, b) => a.username.localeCompare(b.username),
    },
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
      sorter: (a, b) => a.firstName.localeCompare(b.firstName),
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
      sorter: (a, b) => a.lastName.localeCompare(b.lastName),
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Assigned Levels',
      dataIndex: 'teachingLevel',
      key: 'teachingLevel',
      render: (levels: any[]) => {
        if (!levels?.length) return <span className="text-gray-400">None</span>;
        const labels: Record<string, string> = { LEVEL1: 'Licence 1', LEVEL2: 'Licence 2', LEVEL3: 'Licence 3', LEVEL4: 'Master 1', LEVEL5: 'Master 2' };
        return levels.map((l, i) => {
          const name = typeof l === 'string' ? l : l?.studentLevel || String(l);
          return <Tag key={name + i} color="purple">{labels[name] || name}</Tag>;
        });
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      render: (_, record) => (
        <Button
          type="text"
          icon={<EditOutlined />}
          onClick={() => handleOpenEditTeacherModal(record)}
          title="Edit teacher"
        />
      ),
    },
  ];

  const tabItems = [
    {
      key: 'students',
      label: (
        <span>
          <UserOutlined />
          Students ({students?.length || 0})
        </span>
      ),
      children: (
        <Table
          columns={studentColumns}
          dataSource={filteredStudents}
          rowKey="id"
          loading={studentsLoading || studentsFetching}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} students`,
          }}
        />
      ),
    },
    {
      key: 'teachers',
      label: (
        <span>
          <TeamOutlined />
          Teachers ({teachers?.length || 0})
        </span>
      ),
      children: (
        <Table
          columns={teacherColumns}
          dataSource={filteredTeachers}
          rowKey="id"
          loading={teachersLoading || teachersFetching}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} teachers`,
          }}
        />
      ),
    },
  ];

  return (
    <div className="p-4">
      <Card>
        <div className="flex justify-between items-center mb-6">
          <Title level={3} className="!mb-0">
            <TeamOutlined className="mr-2" />
            User Management
          </Title>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={handleOpenCreateModal}
          >
            Create User
          </Button>
        </div>

        <div className="mb-4">
          <Input
            placeholder="Search by name, email, or username..."
            prefix={<SearchOutlined />}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            allowClear
            style={{ maxWidth: 350 }}
          />
        </div>

        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          items={tabItems}
        />
      </Card>

      <Modal
        title="Create New User"
        open={isCreateModalOpen}
        onCancel={handleCloseCreateModal}
        footer={null}
        destroyOnClose
        width={550}
      >
        <Form
          form={createForm}
          layout="vertical"
          onFinish={handleCreateUser}
          autoComplete="off"
        >
          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="firstName"
              label="First Name"
              rules={[{ required: true, message: 'Please enter first name' }]}
            >
              <Input placeholder="John" />
            </Form.Item>

            <Form.Item
              name="lastName"
              label="Last Name"
              rules={[{ required: true, message: 'Please enter last name' }]}
            >
              <Input placeholder="Doe" />
            </Form.Item>
          </div>

          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Please enter email' },
              { type: 'email', message: 'Please enter a valid email' },
            ]}
          >
            <Input placeholder="john.doe@university.edu" />
          </Form.Item>

          <Form.Item
            name="password"
            label="Password"
            rules={[
              { required: true, message: 'Please enter password' },
              { min: 6, message: 'Password must be at least 6 characters' },
            ]}
          >
            <Input.Password placeholder="Password" />
          </Form.Item>

          <Form.Item
            name="phone"
            label="Phone"
          >
            <Input placeholder="+237 6XX XXX XXX" />
          </Form.Item>

          <div className="grid grid-cols-2 gap-4">
            <Form.Item
              name="role"
              label="Role"
              rules={[{ required: true, message: 'Please select role' }]}
            >
              <Select placeholder="Select role" options={ROLES} />
            </Form.Item>

            <Form.Item
              noStyle
              shouldUpdate={(prev, curr) => prev.role !== curr.role}
            >
              {({ getFieldValue }) => 
                getFieldValue('role') === Role.STUDENT ? (
                  <Form.Item
                    name="levelId"
                    label="Level"
                    rules={[{ required: true, message: 'Please select level' }]}
                  >
                    <Select placeholder="Select level" options={LEVELS} />
                  </Form.Item>
                ) : null
              }
            </Form.Item>
          </div>

          <Form.Item
            noStyle
            shouldUpdate={(prev, curr) => prev.role !== curr.role}
          >
            {({ getFieldValue }) => 
              getFieldValue('role') === Role.STUDENT ? (
                <Form.Item
                  name="matricule"
                  label="Matricule"
                  rules={[
                    { required: true, message: 'Please enter student matricule' },
                    { pattern: /^[0-9]{2}[A-Z][0-9]{4}$/, message: 'Format: 2 digits + 1 uppercase letter + 4 digits (e.g. 21A0001)' },
                  ]}
                >
                  <Input placeholder="e.g. 21A0001" style={{ textTransform: 'uppercase' }} />
                </Form.Item>
              ) : null
            }
          </Form.Item>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseCreateModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isRegistering}
              >
                Create User
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Edit Teacher"
        open={isEditTeacherModalOpen}
        onCancel={handleCloseEditTeacherModal}
        footer={null}
        destroyOnClose
      >
        <Form
          form={editTeacherForm}
          layout="vertical"
          onFinish={handleUpdateTeacher}
          autoComplete="off"
        >
          <Form.Item
            name="firstName"
            label="First Name"
            rules={[{ required: true, message: 'Please enter first name' }]}
          >
            <Input placeholder="John" />
          </Form.Item>

          <Form.Item
            name="lastName"
            label="Last Name"
            rules={[{ required: true, message: 'Please enter last name' }]}
          >
            <Input placeholder="Doe" />
          </Form.Item>

          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Please enter email' },
              { type: 'email', message: 'Please enter a valid email' },
            ]}
          >
            <Input placeholder="john.doe@university.edu" />
          </Form.Item>

          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={handleCloseEditTeacherModal}>Cancel</Button>
              <Button
                type="primary"
                htmlType="submit"
                loading={isUpdating}
              >
                Update
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Edit Student"
        open={isEditStudentModalOpen}
        onCancel={() => { setIsEditStudentModalOpen(false); setEditingStudent(null); }}
        footer={null}
        destroyOnClose
      >
        <Form form={editStudentForm} layout="vertical" onFinish={handleUpdateStudent}>
          <Form.Item name="firstName" label="First Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="lastName" label="Last Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="Email" rules={[{ required: true, type: 'email' }]}>
            <Input />
          </Form.Item>
          <Form.Item className="mb-0 flex justify-end">
            <Space>
              <Button onClick={() => { setIsEditStudentModalOpen(false); setEditingStudent(null); }}>Cancel</Button>
              <Button type="primary" htmlType="submit">Update</Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default UserManagementPage;
