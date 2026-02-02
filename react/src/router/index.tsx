import { lazy, Suspense } from "react";
import { redirect, type RouteObject } from "react-router-dom";
import { LoginPage, RegisterPage } from "../features";
import { AuthLayout } from "../layouts";
import { Dashboard } from "../layouts/Dashboard.tsx";
import { PrivateRoutes } from "../components/PrivateRoute.tsx";
import { PageTitleProvider } from "../contexts";
import NotFoundView from "../components/NotFoundView.tsx";
import { PageLoader } from "../components/PageLoader";

const Overview = lazy(() => import("../features").then(m => ({ default: m.Overview })));
const Licence1 = lazy(() => import("../features").then(m => ({ default: m.Licence1 })));
const Licence2 = lazy(() => import("../features").then(m => ({ default: m.Licence2 })));
const Licence3 = lazy(() => import("../features").then(m => ({ default: m.Licence3 })));
const Master1 = lazy(() => import("../features").then(m => ({ default: m.Master1 })));
const Master2 = lazy(() => import("../features").then(m => ({ default: m.Master2 })));
const Semester1 = lazy(() => import("../features/student/sem1/index.tsx"));
const Semester2 = lazy(() => import("../features/student/sem2/index.tsx"));
const UserManagementPage = lazy(() => import("../features/admin").then(m => ({ default: m.UserManagementPage })));
const DepartmentManagementPage = lazy(() => import("../features/admin").then(m => ({ default: m.DepartmentManagementPage })));
const SubjectManagementPage = lazy(() => import("../features/admin").then(m => ({ default: m.SubjectManagementPage })));
const SemesterManagementPage = lazy(() => import("../features/admin").then(m => ({ default: m.SemesterManagementPage })));
const StudentGradeClaimPage = lazy(() => import("../features/revendication").then(m => ({ default: m.StudentGradeClaimPage })));
const GradeClaimsReviewPage = lazy(() => import("../features/revendication").then(m => ({ default: m.GradeClaimsReviewPage })));
const TranscriptPage = lazy(() => import("../features/transcript").then(m => ({ default: m.TranscriptPage })));
const ProfilePage = lazy(() => import("../features/profile").then(m => ({ default: m.ProfilePage })));
const TeacherDashboardPage = lazy(() => import("../features/teacher").then(m => ({ default: m.TeacherDashboardPage })));

export const routes: RouteObject[] = [
  {
    path: "/",
    loader() {
      return redirect("/auth");
    },
  },
  {
    path: "/auth",
    element: <AuthLayout />,
    children: [
      {
        path: "",
        loader() {
          return redirect("login");
        },
      },
      {
        path: "login",
        element: <LoginPage />,
      },
      {
        path: "register",
        element: <RegisterPage />,
      },
    ],
  },
  {
    path: "/dashboard",
    element: (
      <PrivateRoutes>
        <PageTitleProvider>
          <Dashboard />,
        </PageTitleProvider>
      </PrivateRoutes>
    ),
    children: [
      {
        path: "",
        loader() {
          return redirect("overview");
        },
      },
      {
        path: "overview",
        element: <Suspense fallback={<PageLoader />}><Overview /></Suspense>,
      },
      {
        path: "licence1",
        element: <Suspense fallback={<PageLoader />}><Licence1 /></Suspense>,
      },
      {
        path: "licence2",
        element: <Suspense fallback={<PageLoader />}><Licence2 /></Suspense>,
      },
      {
        path: "licence3",
        element: <Suspense fallback={<PageLoader />}><Licence3 /></Suspense>,
      },
      {
        path: "master1",
        element: <Suspense fallback={<PageLoader />}><Master1 /></Suspense>,
      },
      {
        path: "master2",
        element: <Suspense fallback={<PageLoader />}><Master2 /></Suspense>,
      },
      {
        path: "semester1",
        element: <Suspense fallback={<PageLoader />}><Semester1 /></Suspense>,
      },
      {
        path: "semester2",
        element: <Suspense fallback={<PageLoader />}><Semester2 /></Suspense>,
      },
      {
        path: "admin/users",
        element: <Suspense fallback={<PageLoader />}><UserManagementPage /></Suspense>,
      },
      {
        path: "admin/departments",
        element: <Suspense fallback={<PageLoader />}><DepartmentManagementPage /></Suspense>,
      },
      {
        path: "admin/subjects",
        element: <Suspense fallback={<PageLoader />}><SubjectManagementPage /></Suspense>,
      },
      {
        path: "admin/semesters",
        element: <Suspense fallback={<PageLoader />}><SemesterManagementPage /></Suspense>,
      },
      {
        path: "grade-claims",
        element: <Suspense fallback={<PageLoader />}><StudentGradeClaimPage /></Suspense>,
      },
      {
        path: "grade-claims/review",
        element: <Suspense fallback={<PageLoader />}><GradeClaimsReviewPage /></Suspense>,
      },
      {
        path: "transcript",
        element: <Suspense fallback={<PageLoader />}><TranscriptPage /></Suspense>,
      },
      {
        path: "profile",
        element: <Suspense fallback={<PageLoader />}><ProfilePage /></Suspense>,
      },
      {
        path: "teacher-dashboard",
        element: <Suspense fallback={<PageLoader />}><TeacherDashboardPage /></Suspense>,
      },
    ],
  },
  {
    path: "*",
    element: <NotFoundView />,
  },
];
