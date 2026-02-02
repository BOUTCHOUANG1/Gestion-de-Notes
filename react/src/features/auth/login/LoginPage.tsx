import {LoginForm} from "./views";
import {Link} from "react-router";

export const LoginPage = () => {

    return (
        <>
            <div>
                <h1 className="text-4xl md:text-5xl font-mono font-bold mb-2">
                    Grade <br />Management
                </h1>
            </div>
            <div className={'w-full xl:-mt-15 md:-mt-52 mb-10'}>
                <h2 className="text-2xl md:text-3xl font-mono font-bold mb-4">Hello!</h2>
                <p className={'my-4 text-lg'}>Sign in to get started</p>
                <LoginForm/>
                <div className={'flex items-center justify-between mt-5'}>
                    <p>Don't have an account?</p>
                    <Link to={'/auth/register'} className="font-mono hover:underline">Sign Up</Link>
                </div>
            </div>
        </>
    )
}
