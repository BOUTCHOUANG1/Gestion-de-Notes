import { ReactNode, useEffect} from 'react';
import { Navigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector} from "../store";
import { AuthCheckingSkeleton } from './Skeletons';
import { markAsAuthenticated, markAsUnauthenticated } from '../features/auth/slice.ts';
import { useGetProfileQuery } from '../features/auth/api/authApi';
import { loadUserProfile } from '../features/user/slices.ts';


interface PrivateRoutesProps {
    children: ReactNode;
}

export const PrivateRoutes = ({ children }: PrivateRoutesProps) => {

    const dispatch = useAppDispatch();

    const { isAuthenticated } = useAppSelector(
        (state) => state.auth
    );
    
    const { data: profileData, isSuccess, isError } = useGetProfileQuery();

    useEffect(() => {
        if (isSuccess && profileData) {
            dispatch(loadUserProfile(profileData));
            dispatch(markAsAuthenticated());
        } else if (isError) {
            dispatch(markAsUnauthenticated());
        }
    }, [isSuccess, isError, profileData, dispatch]);


    if (isAuthenticated == null) {
        return <AuthCheckingSkeleton />;
    }


    return isAuthenticated ? children : <Navigate to={'/auth/login'} />;
};
