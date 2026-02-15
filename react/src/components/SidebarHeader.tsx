
interface SideBarHeaderProps {
    title: string;
}

export const SideBarHeader = ({ title }: SideBarHeaderProps) => {
    return (
        <header className="flex items-center justify-center h-[60px]">
           <span className={'font-semibold text-base text-[var(--text-primary)]'}>{title}</span>
        </header>
    );
};
