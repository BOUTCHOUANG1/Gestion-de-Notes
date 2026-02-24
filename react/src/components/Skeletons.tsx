import { Skeleton, Card, Row, Col, Table } from 'antd';
import type { ColumnsType } from 'antd/es/table';

export const ProfileSkeleton = () => (
  <div className="p-4 max-w-4xl mx-auto">
    <Card>
      <div className="flex flex-col items-center mb-6">
        <Skeleton.Avatar size={100} active />
        <Skeleton.Input className="!mt-4" style={{ width: 200 }} active />
        <Skeleton.Button className="!mt-2" style={{ width: 100 }} active />
      </div>
      <Row gutter={[24, 24]}>
        <Col xs={24} md={12}>
          <Card className="bg-gray-50" bordered={false}>
            <Skeleton active paragraph={{ rows: 3 }} />
          </Card>
        </Col>
        <Col xs={24} md={12}>
          <Card className="bg-gray-50" bordered={false}>
            <Skeleton active paragraph={{ rows: 2 }} />
          </Card>
        </Col>
      </Row>
    </Card>
  </div>
);

export const DashboardSkeleton = () => (
  <div className="space-y-6 p-4">
    <div className="mb-6">
      <Skeleton.Input style={{ width: 300 }} active />
      <Skeleton.Input className="!mt-2" style={{ width: 200 }} active size="small" />
    </div>
    <Row gutter={[16, 16]}>
      {[1, 2, 3, 4].map((i) => (
        <Col xs={24} sm={12} lg={6} key={i}>
          <Card>
            <Skeleton active paragraph={{ rows: 1 }} />
          </Card>
        </Col>
      ))}
    </Row>
    <Card className="mt-6">
      <Skeleton active paragraph={{ rows: 5 }} />
    </Card>
  </div>
);

export const TranscriptSkeleton = () => (
  <div className="p-4">
    <Card className="mb-4">
      <div className="flex justify-between items-start mb-4">
        <div>
          <Skeleton.Input style={{ width: 200 }} active />
          <Skeleton.Input className="!mt-2" style={{ width: 150 }} active size="small" />
        </div>
        <div className="flex gap-2">
          <Skeleton.Button active />
          <Skeleton.Button active />
        </div>
      </div>
      <Row gutter={[24, 16]} className="mb-6">
        {[1, 2, 3, 4].map((i) => (
          <Col xs={24} sm={12} md={6} key={i}>
            <Skeleton active paragraph={{ rows: 1 }} />
          </Col>
        ))}
      </Row>
      <Skeleton active paragraph={{ rows: 8 }} />
    </Card>
  </div>
);

export const FullPageSkeleton = () => (
  <div className="flex flex-col items-center justify-center min-h-screen w-full p-8">
    <Skeleton.Avatar size={64} active className="mb-4" />
    <Skeleton.Input style={{ width: 200 }} active />
    <Skeleton.Input className="!mt-2" style={{ width: 150 }} active size="small" />
  </div>
);

export const AuthCheckingSkeleton = () => (
  <div className="flex flex-col items-center justify-center w-screen h-screen bg-gray-50">
    <Skeleton.Avatar size={48} active className="mb-4" />
    <Skeleton.Input style={{ width: 180 }} active />
    <p className="text-gray-400 text-sm mt-4">Checking authentication...</p>
  </div>
);

interface TableSkeletonRowsProps {
  columns: number;
  rows?: number;
}

export const TableSkeletonRows = ({ columns, rows = 5 }: TableSkeletonRowsProps) => {
  const skeletonColumns: ColumnsType<{ key: number }> = Array.from(
    { length: columns },
    (_, index) => ({
      key: index,
      dataIndex: `col${index}`,
      render: () => <Skeleton.Input style={{ width: '100%' }} active size="small" />,
    })
  );

  const skeletonData = Array.from({ length: rows }, (_, index) => ({ key: index }));

  return (
    <Table
      columns={skeletonColumns}
      dataSource={skeletonData}
      pagination={false}
      showHeader={false}
    />
  );
};

export const CardSkeleton = () => (
  <Card>
    <Skeleton active avatar paragraph={{ rows: 2 }} />
  </Card>
);
