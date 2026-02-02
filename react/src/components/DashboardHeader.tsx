import { Badge, Breadcrumb, Button, Dropdown, MenuProps } from "antd";

import { useContext } from "react";
import { DrawerSidebarContext, PageTitleContext } from "../contexts";
import next from "../images/next.png";
import back from "../images/back.png";
import UserIcon from "@heroicons/react/24/solid/esm/UserIcon";
import { store } from "../store";
import { Link, useLocation } from "react-router";
import { ThemeToggle } from "./";

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
        title: <Link to={url} className="font-mono hover:underline">{labelMap[segment] || segment}</Link>,
      };
    }),
  ];

  const { toggleSidebar, isSidebarOpen } = useContext(DrawerSidebarContext);
  const { pageTitle } = useContext(PageTitleContext);

  const userInfo = store.getState().user.profile;

  const onDisconnect = () => {
    disconnect();
  };

  const items: MenuProps["items"] = [
    {
      label: "",
      key: "0",
    },
    {
      label: <button onClick={onDisconnect} className="font-mono">{"Logout"}</button>,
      key: "1",
    },
  ];

  return (
    <header className="h-[60px] border-b-2 flex justify-between items-center px-4 md:px-6">
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
          <h1 className="text-xl font-mono font-bold logo">ManageNotes</h1>
        </div>
        <Breadcrumb items={BItems} separator=">" className="hidden lg:block" />
      </div>
      <div className="flex-1 flex justify-center">
        <h2 className="font-mono font-semibold text-lg md:text-xl line-clamp-1">
          {pageTitle && pageTitle}
        </h2>
      </div>

      <div className="flex gap-3 items-center">
        <ThemeToggle />
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
            <div className="font-bold flex gap-1 text-sm font-mono">
              <span>{userInfo.firstName}</span>
              <span>{userInfo.lastName}</span>
            </div>
            <span className="text-xs opacity-70">{userInfo.role}</span>
          </div>
        </div>
      </div>
    </header>
  );
};
