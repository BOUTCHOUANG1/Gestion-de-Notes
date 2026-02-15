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
import { useGetStudentsByTeachingLevelsQuery } from "../features/teacher/api/teacherDashboardApi.ts";

const LEVEL_CONFIG: Record<string, { label: string; route: string; cycle: string }> = {
  LEVEL1: { label: "Licence 1", route: "licence1", cycle: "licence" },
  LEVEL2: { label: "Licence 2", route: "licence2", cycle: "licence" },
  LEVEL3: { label: "Licence 3", route: "licence3", cycle: "licence" },
  LEVEL4: { label: "Master 1", route: "master1", cycle: "master" },
  LEVEL5: { label: "Master 2", route: "master2", cycle: "master" },
};

export const Dashboard = () => {
  const dispatch = useAppDispatch();
  const userProfile = useAppSelector((state) => state.user.profile);
  const { data: studentsByLevel } = useGetStudentsByTeachingLevelsQuery(undefined, {
    skip: !hasPermission([Role.TEACHER]),
  });

  if (!userProfile) {
    return <Spinner />;
  }

  const teacherLevels = studentsByLevel ? Object.keys(studentsByLevel) : [];
  const licenceLevels = teacherLevels.filter(l => LEVEL_CONFIG[l]?.cycle === "licence");
  const masterLevels = teacherLevels.filter(l => LEVEL_CONFIG[l]?.cycle === "master");

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
            <div className="h-30 flex items-center justify-center text-xs font-medium uppercase tracking-wider text-[var(--text-tertiary)]">MENU</div>
            <main
              className={"overflow-y-auto overflow-x-hidden h-full"}
            >
              <SidebarNavItem
                to={"overview"}
                icon={<HomeIcon width={24} />}
                label={"Home"}
              />
              {hasPermission([Role.TEACHER]) && (
                <>
                  <SidebarNavItem
                    to={"teacher-dashboard"}
                    icon={<ChartBarIcon width={26} />}
                    label={"My Dashboard"}
                  />
                  {licenceLevels.length > 0 && (
                    <SidebarNavItem
                      to={LEVEL_CONFIG[licenceLevels[0]].route}
                      icon={<AcademicCapIcon width={26} />}
                      label={"Licence"}
                    >
                      {licenceLevels.map(l => (
                        <SidebarNavSubItem key={l} label={LEVEL_CONFIG[l].label} to={LEVEL_CONFIG[l].route} />
                      ))}
                    </SidebarNavItem>
                  )}
                  {masterLevels.length > 0 && (
                    <SidebarNavItem
                      to={LEVEL_CONFIG[masterLevels[0]].route}
                      icon={<AcademicCapIcon width={26} />}
                      label={"Master"}
                    >
                      {masterLevels.map(l => (
                        <SidebarNavSubItem key={l} label={LEVEL_CONFIG[l].label} to={LEVEL_CONFIG[l].route} />
                      ))}
                    </SidebarNavItem>
                  )}
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

            <footer className="flex flex-col border-t border-[var(--border-color)]">
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
