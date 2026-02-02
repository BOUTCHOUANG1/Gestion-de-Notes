import { Spin } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';

export const PageLoader = () => {
  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      width: '100%'
    }}>
      <Spin 
        indicator={<LoadingOutlined style={{ fontSize: 48 }} spin />}
        tip="Loading..."
        size="large"
      />
    </div>
  );
};
