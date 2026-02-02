import {RegisterStepper} from "./views";
import {RegisterCtxProvider} from "./context";
import {Link} from "react-router";

export const RegisterPage = ()=>{
    return (
        <RegisterCtxProvider>
            <div>
                <p className="text-center text-2xl md:text-3xl font-mono font-bold mb-6">Sign Up!</p>
                <RegisterStepper isHorizontal/>
            </div>
            <div className={'flex items-center justify-between mt-5'}>
                <p>Already have an account?</p>
                <Link to={'/auth/login'} className="font-mono hover:underline">Sign In</Link>
            </div>
        </RegisterCtxProvider>
    )
}