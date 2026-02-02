import { ReactNode } from 'react';

type CardProps = {
  children: ReactNode;
  className?: string;
  onClick?: () => void;
  hoverable?: boolean;
};

export const Card = ({ children, className = '', onClick, hoverable = true }: CardProps) => {
  const baseClass = 'card';
  const hoverClass = hoverable ? 'cursor-pointer' : '';
  const clickClass = onClick ? 'cursor-pointer' : '';
  
  return (
    <div 
      className={`${baseClass} ${hoverClass} ${clickClass} ${className}`.trim()}
      onClick={onClick}
    >
      {children}
    </div>
  );
};
