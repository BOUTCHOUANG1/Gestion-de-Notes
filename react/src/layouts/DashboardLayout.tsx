import { ReactNode, useContext } from "react";
import { Drawer } from "antd";
import { DrawerSidebarContext } from "../contexts";

interface DashboardLayoutProps {
  collapsedWidth: number;
  sidebarView: ReactNode;
  children: ReactNode;
}

export const DashboardLayout = ({
  children,
  sidebarView,
  collapsedWidth,
}: DashboardLayoutProps) => {
  const { isSidebarOpen, toggleSidebar } = useContext(DrawerSidebarContext);

  const sidebarWidth = isSidebarOpen ? 300 : collapsedWidth;
  return (
    <>
      <Drawer
        aria-label="drawer-sidebar"
        width={300}
        placement={"left"}
        closable={false}
        rootClassName={"lg:hidden"}
        classNames={{
          body: "!p-0 mobile-drawer !bg-[#33332D]",
        }}
        onClose={() => toggleSidebar?.()}
        open={isSidebarOpen ?? false}
        key={"left"}
      >
        {sidebarView}
      </Drawer>
      <div className="dashboard-layout">
        <aside
          className={`sidebar-container hidden my-4  lg:grid grid-rows-[auto_1fr_auto] overflow-hidden duration-300 ease-in-out transition-all bg-[#33332D]`}
          style={{ width: sidebarWidth }}
        >
          {sidebarView}
        </aside>
        <main className={"__main"}>{children}</main>
      </div>
    </>
  );
};
