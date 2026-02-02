import {Form, Input, Select} from "antd";
import {AppButton, PasswordInputFormItem} from "../../../../components";
import {useContext} from "react";
import {StepperContext} from "../../../../contexts";
import {IdentificationInfoDataType, RegisterContext} from "../context";
import {Role} from "../../../../api/enums";
// import {useAppDispatch} from "../../../../store";
// import {navigateTo} from "../../../navigation";

export const IdentificationForm =()=>{

    const { handlePrev } = useContext(StepperContext);
    const { setIdentificationInfo , register , role , personalInfo, identificationInfo ,isRegisterLoading } = useContext(RegisterContext)
    //const dispatch = useAppDispatch()

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
        // dispatch(navigateTo('/auth'))
        // resetForm?.()

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
                <Input size={'large'} placeholder="Enter your username"/>
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
                <Input size={'large'} placeholder='Enter your email'/>
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
                        <Input size={'large'} placeholder='Enter your key'/>
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
                            <Input size={'large'} placeholder='Enter your level'/>
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
                            <Input size={'large'} placeholder='Enter your program'/>
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
                            />
                        </Form.Item>
                    </>



                )
            }
            <PasswordInputFormItem/>
            <div className={'w-full flex justify-between gap-6 mt-10'}>
                <AppButton
                    className={'w-full'}
                    label={'Previous'}
                    onClick={handlePrev}
                />
                <AppButton
                    htmlType={'submit'}
                    className={'w-full'}
                    label={'Next'}
                    loading={isRegisterLoading}
                />
            </div>
        </Form>
    )
}
