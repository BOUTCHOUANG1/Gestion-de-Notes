import {Action, combineReducers, configureStore} from "@reduxjs/toolkit";
import {setupListeners} from "@reduxjs/toolkit/query";
import {navigationReducer} from "../features/navigation";
import {authReducer} from "../features/auth/slice.ts";
import {TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import {notificationReducer} from "../contexts";
import { userReducer } from "../features/user/slices.ts";
import { api } from "./api/apiSlice";



const combinedReducer = combineReducers({
    navigation : navigationReducer,
    auth: authReducer,
    notification: notificationReducer,
    user : userReducer,
    [api.reducerPath]: api.reducer,

})

const rootReducer = (state: any, action: Action) => {
    if (action.type === 'RESET') {
        // Clear all state including RTK Query cache
        return combinedReducer(undefined, action);
    }
    return combinedReducer(state, action);
};

export const createStore = () => {
    const store = configureStore({
        reducer: rootReducer,
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware().concat(api.middleware),
    });
    
    setupListeners(store.dispatch);
    return store;
}



export const store = createStore();



export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
