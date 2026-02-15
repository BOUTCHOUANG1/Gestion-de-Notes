import {routes} from "./router";
import {createBrowserRouter} from "react-router";
import {RouterProvider} from "react-router/dom";
import {ServerNotificationHandler} from "./components";
import {ConfigProvider} from "antd";

const echelonTheme = {
    token: {
        colorPrimary: '#667EEA',
        colorLink: '#667EEA',
        colorSuccess: '#10B981',
        colorWarning: '#F59E0B',
        colorError: '#EF4444',
        colorInfo: '#3B82F6',
        colorTextBase: '#111827',
        colorBgBase: '#FAFBFC',
        colorBorder: '#E5E7EB',
        borderRadius: 8,
        fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, sans-serif",
        fontSize: 14,
    },
    components: {
        Button: { borderRadius: 9999 },
        Input: { borderRadius: 9999 },
        Statistic: { titleFontSize: 13, contentFontSize: 28 },
    },
};

export const App = () => {

    const router = createBrowserRouter(routes)

    return (
        <ConfigProvider theme={echelonTheme}>
            <ServerNotificationHandler/>
            <RouterProvider router={router}/>
        </ConfigProvider>
    )
}