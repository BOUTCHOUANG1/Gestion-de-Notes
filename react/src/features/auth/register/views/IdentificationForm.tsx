import {Form, Input, Select} from "antd";
import {AppButton, PasswordInputFormItem} from "../../../../components";
import {useContext} from "react";
import {StepperContext} from "../../../../contexts";
import {IdentificationInfoDataType, RegisterContext} from "../context";
import {Role} from "../../../../api/enums";

export const IdentificationForm =()=>{

    const { handlePrev } = useContext(StepperContext);
    const { setIdentificationInfo , register , role , personalInfo, identificationInfo ,isRegisterLoading } = useContext(RegisterContext)

    const levels = [
        {
            label : 'LICENCE',
            value : 'license'
        },
        {
            label : 'MASTER',
            value : 'master'
        }
    ]


    const onFinish =(values : IdentificationInfoDataType)=>{
        const payload: IdentificationInfoDataType = {
            username: values.username,
            email: values.email,
            registrationKey: values.registrationKey,
            password: values.password,
            level : values.level,
            speciality: values.speciality,
            cycle: values.cycle,
        }
        setIdentificationInfo?.(payload)
        register?.({
            ...personalInfo,
            role,
            ...identificationInfo
        })

    }


    return (
        <Form name={'register'} onFinish={onFinish}>
            <Form.Item
                name="username"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'} placeholder="Enter your username" className="form-input"/>
            </Form.Item>
            <Form.Item
                name="email"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'} placeholder='Enter your email' className="form-input"/>
            </Form.Item>
            {
                (role === Role.ADMIN || role === Role.TEACHER) && (
                    <Form.Item
                        name="key"
                        rules={[
                            {
                                required: true,
                                message: 'Required field',
                            },
                        ]}
                    >
                        <Input size={'large'} placeholder='Enter your key' className="form-input"/>
                    </Form.Item>
                )
            }
            {
                role === Role.STUDENT && (
                    <>
                        <Form.Item
                            name="level"
                            rules={[
                                {
                                    required: true,
                                    message: 'Required field',
                                },
                            ]}
                        >
                            <Input size={'large'} placeholder='Enter your level' className="form-input"/>
                        </Form.Item>
                        <Form.Item
                            name="speciality"
                            rules={[
                                {
                                    required: true,
                                    message: 'Required field',
                                },
                            ]}
                        >
                            <Input size={'large'} placeholder='Enter your program' className="form-input"/>
                        </Form.Item>
                        <Form.Item
                            name="cycle"
                            rules={[
                                {
                                    required: true,
                                    message: 'Required field',
                                },
                            ]}
                        >
                            <Select
                                size={'large'}
                                aria-label="mfaChannel"
                                options={levels}
                                placeholder={'Select your cycle'}
                                className="form-input"
                            />
                        </Form.Item>
                    </>



                )
            }
            <PasswordInputFormItem/>
            <div className={'w-full flex justify-between gap-6 mt-10'}>
                <AppButton
                    className={'btn-primary w-full'}
                    label={'Previous'}
                    onClick={handlePrev}
                />
                <AppButton
                    htmlType={'submit'}
                    className={'btn-primary w-full'}
                    label={'Next'}
                    loading={isRegisterLoading}
                />
            </div>
        </Form>
    )
}
