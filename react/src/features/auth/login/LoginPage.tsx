import {LoginForm} from "./views";
import {Link} from "react-router";

export const LoginPage = () => {

    return (
        <>
            <div>
                <h1 className={'text-primary'}>
                    Gestion <br />des notes
                </h1>
            </div>
            <div className={'w-full xl:-mt-15 md:-mt-52 mb-10'}>
                <h2 className={'text-primary'}>Bonjour !</h2>
                <p className={'my-4 text-primary'}>Connectez vous pour commencer a travailler </p>
                <LoginForm/>
                <div className={'flex items-center justify-between text-primary mt-5'}>
                    <p>Vous n'avez pas de compte ?</p>
                    <Link to={'/auth/register'} className={'text-primary'}>S'inscrire</Link>
                </div>
            </div>
        </>
    )
}
