import { Outlet } from "react-router-dom";

import { clearTokens } from "../api/services/token.service";
import { markAsUnauthenticated } from "../features/auth/slice";
import { navigateTo } from "../features/navigation";
import { useAppDispatch, useAppSelector } from "../store";
import { DrawerSidebarProvider } from "../contexts";
import { DashboardLayout } from "./DashboardLayout.tsx";
import { SidebarNavItem } from "../components/SidebarNavItem.tsx";
import { DashboardHeader } from "../components/DashboardHeader.tsx";
import signOutIconSvg from "../images/logoutt.png";
import { AcademicCapIcon, HomeIcon, Cog6ToothIcon, DocumentTextIcon, ClipboardDocumentListIcon, UserIcon, ChartBarIcon } from "@heroicons/react/24/solid";
import { SidebarNavSubItem } from "../components/SidebarNavSubItem.tsx";
import { hasPermission } from "../utils/index.ts";
import { Role } from "../api/enums/index.ts";
import { Spinner } from "../components/Spinner.tsx";

export const Dashboard = () => {
  const dispatch = useAppDispatch();

  const userProfile = useAppSelector((state) => state.user.profile);

  if (!userProfile) {
    return <Spinner />;
  }

  const onDisconnect = () => {
    clearTokens();
    dispatch(markAsUnauthenticated());
    dispatch({ type: 'RESET' });
    dispatch(navigateTo('/'));
  };

  return (
    <DrawerSidebarProvider>
      <DashboardLayout
        collapsedWidth={65}
        sidebarView={
          <>
            <div className="h-30 flex items-center justify-center font-mono text-sm opacity-70 text-white">MENU</div>
            <main
              className={"overflow-y-auto overflow-x-hidden h-full text-white"}
            >
              <SidebarNavItem
                to={"overview"}
                icon={<HomeIcon width={24} className="text-white/70" />}
                label={"Home"}
              />
              {hasPermission([Role.TEACHER]) && (
                <>
                  <SidebarNavItem
                    to={"teacher-dashboard"}
                    icon={<ChartBarIcon width={26} />}
                    label={"My Dashboard"}
                  />
                  <SidebarNavItem
                    to={"licence1"}
                    icon={<AcademicCapIcon width={26} />}
                    label={"Licence"}
                  >
                    <SidebarNavSubItem label={"Licence 1"} to={"licence1"} />
                    <SidebarNavSubItem label={"Licence 2"} to={"licence2"} />
                    <SidebarNavSubItem label={"Licence 3"} to={"licence3"} />
                  </SidebarNavItem>
                  <SidebarNavItem
                    to={"master1"}
                    icon={<AcademicCapIcon width={26} />}
                    label={"Master"}
                  >
                    <SidebarNavSubItem label={"Master 1"} to={"master1"} />
                    <SidebarNavSubItem label={"Master 2"} to={"master2"} />
                  </SidebarNavItem>
                  <SidebarNavItem
                    to={"overview"}
                    icon={<AcademicCapIcon width={26} />}
                    label={"Doctorat"}
                  />
                </>
              )}

              {hasPermission([Role.STUDENT]) && (
                <>
                  <SidebarNavItem
                    to={"semester1"}
                    icon={<AcademicCapIcon width={26} />}
                    label={"Semestre 1"}
                  />
                  <SidebarNavItem
                    to={"semester2"}
                    icon={<AcademicCapIcon width={26} />}
                    label={"Semestre 2"}
                  />
                  <SidebarNavItem
                    to={"grade-claims"}
                    icon={<ClipboardDocumentListIcon width={26} />}
                    label={"Grade Claims"}
                  />
                  <SidebarNavItem
                    to={"transcript"}
                    icon={<DocumentTextIcon width={26} />}
                    label={"Transcript"}
                  />
                </>
              )}
              {hasPermission([Role.TEACHER]) && (
                <SidebarNavItem
                  to={"grade-claims/review"}
                  icon={<ClipboardDocumentListIcon width={26} />}
                  label={"Grade Claims"}
                />
              )}
              {hasPermission([Role.ADMIN]) && (
                <SidebarNavItem
                  to={"admin/dashboard"}
                  icon={<Cog6ToothIcon width={26} />}
                  label={"Administration"}
                >
                  <SidebarNavSubItem 
                    label={"Dashboard"} 
                    to={"admin/dashboard"} 
                  />
                  <SidebarNavSubItem 
                    label={"Users"} 
                    to={"admin/users"} 
                  />
                  <SidebarNavSubItem 
                    label={"Departments"} 
                    to={"admin/departments"} 
                  />
                  <SidebarNavSubItem 
                    label={"Subjects"} 
                    to={"admin/subjects"} 
                  />
                  <SidebarNavSubItem 
                    label={"Semesters"} 
                    to={"admin/semesters"} 
                  />
                  <SidebarNavSubItem 
                    label={"Grade Claims"} 
                    to={"grade-claims/review"} 
                  />
                </SidebarNavItem>
              )}
            </main>

            <footer className="flex flex-col border-t border-white/20">
              <SidebarNavItem
                to={"profile"}
                icon={<UserIcon width={24} />}
                label={"Profile"}
              />
              <SidebarNavItem
                aria-label="dashboard sign out"
                to={"/"}
                onClick={onDisconnect}
                icon={<img src={signOutIconSvg} width={34} alt="" />}
                label={"Sign out"}
              />
            </footer>
          </>
        }
      >
        <div className="h-full grid grid-rows-[auto_1fr]">
          <DashboardHeader
            onDisconnect={onDisconnect}
            useAppSelector={useAppSelector}
            role="TEACHER"
          />
          <div className={"outlet overflow-hidden overflow-y-auto pt-4 "}>
            <div className="container mx-auto ">
              <Outlet />
            </div>
          </div>
        </div>
      </DashboardLayout>
    </DrawerSidebarProvider>
  );
};
