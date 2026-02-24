import {Form, Input} from "antd";
import {AppButton, PasswordInputFormItem} from "../../../../components";
import {useState} from "react";
import { useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../api/authApi";


export const LoginForm = ()=>{
    
    const [login, { isLoading }] = useLoginMutation();
    const navigate = useNavigate();

    type SubmissionForm = {
        username: string;
        password: string;
    };

    const [isProcessing, setIsProcessing] = useState(false);

    const onFinish = async ({
                                username,
                                password,
                            }: SubmissionForm) => {
        setIsProcessing(true);
        try {
            const data = await login({ username, password }).unwrap();
            if (data.role === 'ADMIN') {
                navigate('/dashboard/admin/dashboard');
            } else if (data.role === 'TEACHER') {
                navigate('/dashboard/teacher-dashboard');
            } else {
                navigate('/dashboard/overview');
            }
        } catch {
        } finally {
            setIsProcessing(false);
        }
    };


    return (
        <Form name={'login'} onFinish={onFinish}>
            <Form.Item
                name="username"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input 
                    id="login_username" 
                    size={'large'} 
                    placeholder='Enter your username'
                />
            </Form.Item>
            <PasswordInputFormItem/>
            <AppButton
                htmlType={'submit'}
                data-testid="login-submit"
                loading={isProcessing || isLoading}
                className={'btn-filled w-full mt-4'}
                size={'large'}
                label={'Sign In'}
            />
        </Form>

    )
}
