import { GradesHeader } from "./LicenceHeader";
import { EditableGradesTable } from "./EditableGradesTable";
import { usePageTitle } from "../hooks/usePageTitle";
import { useGradeEditor } from "../hooks/useGradeEditor";
import { studentResDto } from "../api/reponse-dto/user.res.dto";
import {
  useGetStudentsByTeachingLevelsQuery,
  useGetTeacherSubjectsQuery,
  useGetTeacherProfileQuery,
} from "../features/teacher/api/teacherDashboardApi";
import { useGetSemestersQuery } from "../features/admin/api/semesterApi";
import { Spinner } from "./Spinner";

export interface GradePageConfig {
  level: string;
  pageTitle: string;
  headerTitle: string;
  levelDisplay: string;
  studentLevel: string;
}

export const GradingPage = (config: GradePageConfig) => {
  usePageTitle(config.pageTitle);

  const { data: studentsByLevel, isLoading: studentsLoading } = useGetStudentsByTeachingLevelsQuery();
  const { data: subjects, isLoading: subjectsLoading } = useGetTeacherSubjectsQuery();
  const { data: semesters } = useGetSemestersQuery();
  const { data: profile } = useGetTeacherProfileQuery();

  const students: studentResDto[] = studentsByLevel?.[config.studentLevel] || [];

  const subject = subjects?.find((s) =>
    s.subjectsLevel?.some((l) => l.studentLevel === config.studentLevel)
  );

  const activeSemester = semesters?.find((s) => s.active);

  const gradeState = useGradeEditor({
    subjectId: subject?.subjectId,
    semesterId: activeSemester?.semesterId ?? activeSemester?.id,
    students,
  });

  if (studentsLoading || subjectsLoading) return <Spinner />;

  return (
    <div>
      <GradesHeader
        title={config.headerTitle}
        period={activeSemester?.name || "—"}
        topic={subject?.subjectName || "—"}
        code={subject?.subjectCode || "—"}
        level={config.levelDisplay}
        NC={String(students.length)}
        CANT="20"
      />
      <div className="mt-8">
        <EditableGradesTable
          extraColumns={[]}
          isEditable={gradeState.isTableEditable}
          data={gradeState.filteredTableData}
          onGradesChange={gradeState.setEditedData}
          onEdit={gradeState.handleEdit}
          onConfirm={gradeState.handleConfirm}
          isDataEditable={gradeState.isTableEditable}
          setIsDataEditable={gradeState.setIsTableEditable}
          onSearch={gradeState.setSearchValue}
          saving={gradeState.saving}
          pvInfo={{
            subject: subject?.subjectName || '',
            code: subject?.subjectCode || '',
            semester: activeSemester?.name || '',
            level: config.levelDisplay,
            teacher: `${profile?.firstName || ''} ${profile?.lastName || ''}`.trim(),
          }}
        />
      </div>
    </div>
  );
};
