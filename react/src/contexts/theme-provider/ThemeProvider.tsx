import { ReactNode } from 'react';
import { ConfigProvider } from 'antd';
import {ColorTheme} from "../../api/enums";

type ThemeProviderProps = {
    children: ReactNode;
};

export const ThemeProvider = ({ children }: ThemeProviderProps) => {
    const textColor = typeof window !== 'undefined'
        ? getComputedStyle(document.documentElement).getPropertyValue('--mn-color-text').trim() || '#33332D'
        : '#33332D';

    const borderColor = typeof window !== 'undefined'
        ? getComputedStyle(document.documentElement).getPropertyValue('--mn-color-border').trim() || '#E1E1E1'
        : '#E1E1E1';

    const bgColor = typeof window !== 'undefined'
        ? getComputedStyle(document.documentElement).getPropertyValue('--mn-color-bg').trim() || '#FFFFFF'
        : '#FFFFFF';

    return (
        <ConfigProvider
            theme={{
                token: {
                    colorText: textColor,
                    colorBorder: borderColor,
                    colorBgBase: bgColor,
                    fontFamily: '"IBM Plex Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace',
                    fontSize: 18,
                },
                components: {
                    Notification: {
                        colorPrimary: ColorTheme.PRIMARY,
                    },
                    DatePicker:{
                        activeBorderColor: 'transparent',
                        hoverBorderColor: ' border-gray-300',
                    },
                    Button: {
                        defaultHoverBg: ColorTheme.SECONDARY,
                        defaultHoverColor: 'white',
                        defaultHoverBorderColor: 'transparent',
                        defaultBg: ColorTheme.PRIMARY,
                        defaultColor:'white'
                    },
                    Input:{
                        activeBorderColor: 'transparent',
                        hoverBorderColor: ' border-gray-300',
                    },
                    Tabs : {
                        colorPrimary : ColorTheme.SECONDARY
                    },
                    Radio : {
                        colorPrimary :ColorTheme.SECONDARY
                    },
                    Checkbox : {
                        colorPrimary : '#1f7d53'
                    },
                },
            }}
        >
            {children}
        </ConfigProvider>
    );
};
