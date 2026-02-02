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
            await login({ username, password }).unwrap();
            navigate('/dashboard');
        } catch {
            // Error handled by baseQuery
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
                        message: 'Champ obligatoire',
                    },
                ]}
            >
                <Input id="login_username" size={'large'} placeholder='Entrez votre nom'/>
            </Form.Item>
            <PasswordInputFormItem/>
            <AppButton
                htmlType={'submit'}
                data-testid="login-submit"
                loading={isProcessing || isLoading}
                className={'w-full'}
                label={'Se connecter'}
            />
        </Form>

    )
}
