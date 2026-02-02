import {LoginForm} from "./views";
import {Link} from "react-router";

export const LoginPage = () => {

    return (
        <>
            <div>
                <h1 className={'text-primary'}>
                    Grade <br />Management
                </h1>
            </div>
            <div className={'w-full xl:-mt-15 md:-mt-52 mb-10'}>
                <h2 className={'text-primary'}>Hello!</h2>
                <p className={'my-4 text-primary'}>Sign in to get started</p>
                <LoginForm/>
                <div className={'flex items-center justify-between text-primary mt-5'}>
                    <p>Don't have an account?</p>
                    <Link to={'/auth/register'} className={'text-primary'}>Sign Up</Link>
                </div>
            </div>
        </>
    )
}
