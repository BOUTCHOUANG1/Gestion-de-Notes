import {RegisterStepper} from "./views";
import {RegisterCtxProvider} from "./context";
import {Link} from "react-router";

export const RegisterPage = ()=>{
    return (
        <RegisterCtxProvider>
            <div>
                <p className={'text-center text-2xl mb-3'}>Sign Up!</p>
                <RegisterStepper isHorizontal/>
            </div>
            <div className={'flex items-center justify-between text-gray-400 '}>
                <p>Already have an account?</p>
                <Link to={'/auth/login'} className={'text-primary'}>Sign In</Link>
            </div>
        </RegisterCtxProvider>
    )
}