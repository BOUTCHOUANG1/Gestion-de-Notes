import React, { ReactNode } from 'react';
import {Button, ButtonProps} from "antd";

export interface Props {
    label?: string;
    children?: ReactNode;
    btnType?: 'submit' | 'button';
    className?: string;
}

export const AppButton: React.FC<Props & ButtonProps> = ({
                                                             label,
                                                             btnType = 'submit',
                                                             className,
                                                             children,
                                                            ...props
                                                         }) => {
    return (
        <Button
            {...props}
            className={`${className} rounded-full font-semibold transition hover:disabled:cursor-not-allowed ${
                btnType === 'submit'
                    ? 'bg-[var(--accent)] text-white hover:bg-[var(--accent-hover)]'
                    : 'bg-transparent text-[var(--text-primary)] border border-[var(--border-color)]'
            }`}
        >
            {label ? label : children}
        </Button>
    );
};
