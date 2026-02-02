import {DatePicker, Form, Input} from "antd";
import {AppButton} from "../../../../components";
import {useContext} from "react";
import {StepperContext} from "../../../../contexts";
import {PersonalInfoDataType, RegisterContext} from "../context";

export const PersonalInfoForm =()=>{

    const { handleNext ,  handlePrev } = useContext(StepperContext);
    const { personalInfo, setPersonalInfo} = useContext(RegisterContext)


    const onFinish =(values : PersonalInfoDataType)=>{
        const payload: PersonalInfoDataType = {
            firstName : values.firstName,
            lastName: values.lastName,
            phone: values.phone,
            dateOfBirth: values.dateOfBirth,
            placeOfBirth: values.placeOfBirth,
        }
        setPersonalInfo?.(payload)
        handleNext?.()
    }

    const disabledButton = personalInfo?.firstName === null || personalInfo?.lastName === null

    return (
        <Form name={'register'} onFinish={onFinish}>
            <Form.Item
                name="firstName"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'}  placeholder='Enter your first name' className="form-input"/>
            </Form.Item>
            <Form.Item
                name="lastName"
                className={'mb-200'}
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'}  placeholder='Enter your last name' className="form-input"/>
            </Form.Item>
            <Form.Item
                name="phone"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'} placeholder='Enter your phone number' className="form-input"/>
            </Form.Item>
            <Form.Item
                name="dateOfBirth"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <DatePicker
                    size="large"
                    placeholder={'Enter your date of birth'}
                    className={'w-full form-input'}
                />
            </Form.Item>
            <Form.Item
                name="placeOfBirth"
                rules={[
                    {
                        required: true,
                        message: 'Required field',
                    },
                ]}
            >
                <Input size={'large'}  placeholder='Enter your place of birth' className="form-input"/>
            </Form.Item>
            <div className={'w-full flex justify-between gap-10 mt-10'}>
                <AppButton
                    className={'btn-primary w-full'}
                    label={'Previous'}
                    onClick={handlePrev}
                />
                <AppButton
                    className={'btn-primary w-full'}
                    htmlType={'submit'}
                    label={'Next'}
                    disabled={disabledButton}
                />
            </div>

        </Form>
    )
}