import { ReactNode, useEffect, useState } from 'react';
import { ConfigProvider } from 'antd';
import enUS from 'antd/locale/en_US';
import { useTheme } from '../ThemeContext';

type ThemeProviderProps = {
    children: ReactNode;
};

const getCSSVariable = (variableName: string, fallback: string): string => {
    if (typeof window === 'undefined') return fallback;
    const value = getComputedStyle(document.documentElement).getPropertyValue(variableName).trim();
    return value || fallback;
};

export const ThemeProvider = ({ children }: ThemeProviderProps) => {
    const { theme } = useTheme();
    const [cssVars, setCssVars] = useState({
        textColor: getCSSVariable('--mn-color-text', '#33332D'),
        borderColor: getCSSVariable('--mn-color-border', '#E1E1E1'),
        bgColor: getCSSVariable('--mn-color-bg', '#FFFFFF'),
        primaryColor: getCSSVariable('--mn-color-primary', '#1f7d53'),
        secondaryColor: getCSSVariable('--mn-color-secondary', '#2b8a5e'),
    });

    useEffect(() => {
        const updateCSSVars = () => {
            setCssVars({
                textColor: getCSSVariable('--mn-color-text', '#33332D'),
                borderColor: getCSSVariable('--mn-color-border', '#E1E1E1'),
                bgColor: getCSSVariable('--mn-color-bg', '#FFFFFF'),
                primaryColor: getCSSVariable('--mn-color-primary', '#1f7d53'),
                secondaryColor: getCSSVariable('--mn-color-secondary', '#2b8a5e'),
            });
        };

        updateCSSVars();

        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.attributeName === 'data-theme') {
                    setTimeout(updateCSSVars, 0);
                }
            });
        });

        observer.observe(document.documentElement, {
            attributes: true,
            attributeFilter: ['data-theme'],
        });

        return () => observer.disconnect();
    }, [theme]);

    return (
        <ConfigProvider
            locale={enUS}
            theme={{
                token: {
                    colorText: cssVars.textColor,
                    colorBorder: cssVars.borderColor,
                    colorBgBase: cssVars.bgColor,
                    colorPrimary: cssVars.primaryColor,
                    fontFamily: '"IBM Plex Sans", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
                    fontFamilyCode: '"IBM Plex Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace',
                    fontSize: 16,
                },
                components: {
                    Notification: {
                        colorPrimary: cssVars.primaryColor,
                    },
                    DatePicker:{
                        activeBorderColor: cssVars.primaryColor,
                        hoverBorderColor: cssVars.borderColor,
                    },
                    Button: {
                        defaultHoverBg: cssVars.secondaryColor,
                        defaultHoverColor: 'white',
                        defaultHoverBorderColor: cssVars.secondaryColor,
                        defaultBg: cssVars.primaryColor,
                        defaultColor: 'white',
                        primaryColor: cssVars.primaryColor,
                        primaryShadow: 'none',
                    },
                    Input:{
                        activeBorderColor: cssVars.primaryColor,
                        hoverBorderColor: cssVars.borderColor,
                    },
                    Tabs : {
                        colorPrimary: cssVars.secondaryColor
                    },
                    Radio : {
                        colorPrimary: cssVars.secondaryColor
                    },
                    Checkbox : {
                        colorPrimary: cssVars.primaryColor
                    },
                },
            }}
        >
            {children}
        </ConfigProvider>
    );
};
