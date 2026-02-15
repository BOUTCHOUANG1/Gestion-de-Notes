import { Badge, Breadcrumb, Button, Dropdown, MenuProps } from "antd";

import { useContext } from "react";
import { DrawerSidebarContext, PageTitleContext } from "../contexts";
import next from "../images/next.png";
import back from "../images/back.png";
import UserIcon from "@heroicons/react/24/solid/esm/UserIcon";
import { useAppSelector } from "../store";
import { Link, useLocation } from "react-router";

interface Props {
  useAppSelector: (state: any) => any;
  onDisconnect: () => void;
  role: string;
}

export const DashboardHeader = ({ onDisconnect: disconnect }: Props) => {
  const location = useLocation();
  const pathnames = location.pathname.split("/").filter((x) => x);

  const BItems = [
    ...pathnames.map((segment, idx) => {
      const url = "/" + pathnames.slice(0, idx + 1).join("/");
      const labelMap: Record<string, string> = {
        licence1: "Licence 1",
      };
      return {
        title: <Link to={url} className="hover:underline text-[var(--text-secondary)]">{labelMap[segment] || segment}</Link>,
      };
    }),
  ];

  const { toggleSidebar, isSidebarOpen } = useContext(DrawerSidebarContext);
  const { pageTitle } = useContext(PageTitleContext);

  const userInfo = useAppSelector((state) => state.user.profile);

  const onDisconnect = () => {
    disconnect();
  };

  const items: MenuProps["items"] = [
    {
      label: "",
      key: "0",
    },
    {
      label: <button onClick={onDisconnect}>{"Logout"}</button>,
      key: "1",
    },
  ];

  return (
    <header className="h-[60px] border-b border-[var(--border-color)] bg-white/80 backdrop-blur-xl flex justify-between items-center px-4 md:px-6">
      <div className="flex items-center gap-4">
        <Button
          onClick={() => toggleSidebar?.()}
          type="text"
          shape="circle"
          className="hover:rotate-180 transition-transform duration-300"
          icon={
            isSidebarOpen ? (
              <img src={back} alt={"back"} width={15} />
            ) : (
              <img src={next} alt={"next"} width={15} />
            )
          }
        />
        <div className="hidden md:block">
          <span className="text-base font-semibold text-[var(--text-primary)]">ManageNotes</span>
        </div>
        <Breadcrumb items={BItems} separator=">" className="hidden lg:block" />
      </div>
      <div className="flex-1 flex justify-center">
        <span className="font-semibold text-sm md:text-base text-[var(--text-primary)] line-clamp-1">
          {pageTitle && pageTitle}
        </span>
      </div>

      <div className="flex gap-3 items-center">
        <Dropdown
          placement="bottomRight"
          className="relative"
          menu={{ items }}
          trigger={["hover"]}
        >
          <Button type="default" shape="circle">
            <Badge count={2} size="small">
              <UserIcon width={24} />
            </Badge>
          </Button>
        </Dropdown>
        <div className="hidden xl:block">
          <div className="flex flex-col text-xs">
            <div className="font-semibold flex gap-1 text-sm text-[var(--text-primary)]">
              <span>{userInfo.firstName}</span>
              <span>{userInfo.lastName}</span>
            </div>
            <span className="text-xs text-[var(--text-tertiary)]">{userInfo.role}</span>
          </div>
        </div>
      </div>
    </header>
  );
};
