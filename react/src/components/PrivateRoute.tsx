import { ReactNode, useEffect} from 'react';
import { Navigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector} from "../store";
import { AuthCheckingSkeleton } from './Skeletons';
import { markAsAuthenticated, markAsUnauthenticated } from '../features/auth/slice.ts';
import { useGetProfileQuery } from '../features/auth/api/authApi';


interface PrivateRoutesProps {
    children: ReactNode;
}

export const PrivateRoutes = ({ children }: PrivateRoutesProps) => {

    const dispatch = useAppDispatch();

    const { isAuthenticated } = useAppSelector(
        (state) => state.auth
    );
    
    const { isSuccess, isError } = useGetProfileQuery();

    useEffect(() => {
        if (isSuccess) {
            dispatch(markAsAuthenticated());
        } else if (isError) {
            dispatch(markAsUnauthenticated());
        }
    }, [isSuccess, isError, dispatch]);


    if (isAuthenticated == null) {
        return <AuthCheckingSkeleton />;
    }


    return isAuthenticated ? children : <Navigate to={'/auth/login'} />;
};
