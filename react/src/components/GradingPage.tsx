import { GradesHeader } from "./LicenceHeader";
import { EditableGradesTable } from "./EditableGradesTable";
import { usePageTitle } from "../hooks/usePageTitle";
import { useGradeEditor } from "../hooks/useGradeEditor";
import { studentResDto } from "../api/reponse-dto/user.res.dto";

export interface GradePageConfig {
  level: string;
  semester: string;
  pageTitle: string;
  headerTitle: string;
  subjectCode?: string;
  subjectTopic?: string;
  period: string;
  NC: string;
  CANT: string;
  levelDisplay: string;
  initialData?: studentResDto[];
}

export const GradingPage = (config: GradePageConfig) => {
  usePageTitle(config.pageTitle);

  const gradeState = useGradeEditor({
    level: config.level,
    semester: config.semester,
    initialData: config.initialData,
  });

  const extraColumns: { title: string; dataIndex: string }[] = [];

  return (
    <div>
      <GradesHeader
        title={config.headerTitle}
        period={config.period}
        topic={config.subjectTopic}
        code={config.subjectCode}
        level={config.levelDisplay}
        NC={config.NC}
        CANT={config.CANT}
      />
      <div className="mt-8">
        <EditableGradesTable
          extraColumns={extraColumns}
          isEditable={gradeState.isTableEditable}
          data={gradeState.filteredTableData}
          onGradesChange={gradeState.setEditedData}
          onEdit={gradeState.handleEdit}
          onConfirm={gradeState.handleConfirm}
          isDataEditable={gradeState.isTableEditable}
          setIsDataEditable={gradeState.setIsTableEditable}
          onSearch={gradeState.setSearchValue}
        />
      </div>
    </div>
  );
};
