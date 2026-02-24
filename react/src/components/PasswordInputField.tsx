import { useState } from 'react';
import {
    EyeInvisibleOutlined,
    EyeOutlined,
} from '@ant-design/icons';
import {Form, Input} from 'antd';

export const PasswordInputFormItem = () => {
    const [showPassword, setShowPassword] = useState(false);

    const handlePwdVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
        <Form.Item
            name='password'
            rules={[
                {
                    required: true,
                    message: 'Required field',
                },
            ]}
        >
            <Input
                id="login_password"
                size={'large'}
                suffix={
                    showPassword ? (
                        <EyeOutlined
                            onClick={handlePwdVisibility}
                            aria-label={'show password icon'}
                        />
                    ) : (
                        <EyeInvisibleOutlined
                            onClick={handlePwdVisibility}
                            aria-label={'hide password icon'}
                        />
                    )
                }
                type={showPassword ? 'text' : 'password'}
                placeholder='Enter your password'
            />
        </Form.Item>
    );
};
