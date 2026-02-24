import { ReactNode } from 'react';

interface CardProps extends React.HTMLAttributes<HTMLElement> {
    isActive: boolean;
    className?: string;
    children: ReactNode;
    activeClassName?: string;
    IconClassName?: string;
    htmlType?: 'button' | 'submit';
}
export const SelectCard = ({
                               isActive,
                               children,
                               className,
                               IconClassName,
                               activeClassName,
                               htmlType,
                               ...props
                           }: CardProps) => {
    return (
        <div className="relative">
            {isActive && (
                <div
                    className={`${IconClassName} absolute right-[-6px] top-[-7px] w-5 h-5 rounded-full border-[1.9px] border-[var(--bg-card)] bg-[var(--accent)]`}
                />
            )}
            <button
                {...props}
                type={htmlType}
                className={`cursor-pointer w-full text-left lg:mb-7 ${
                    isActive
                        ? `${activeClassName} border-2 rounded-md border-[var(--accent)] bg-[var(--accent-light)] text-[var(--accent)]`
                        : `${className}`
                }
        }`}
            >
                {children}
            </button>
        </div>
    );
};
