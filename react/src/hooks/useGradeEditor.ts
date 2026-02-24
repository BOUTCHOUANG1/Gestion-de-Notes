import { useState, useMemo, useCallback } from "react";
import { message } from "antd";
import { studentResDto } from "../api/reponse-dto/user.res.dto";
import {
  useGetTeacherGradesQuery,
  useCreateGradeMutation,
  useUpdateGradeMutation,
  GradeResponse,
} from "../features/teacher/api/teacherDashboardApi";

export interface UseGradeEditorParams {
  subjectId?: number;
  semesterId?: number;
  examId?: number;
  students: studentResDto[];
}

type StudentRow = studentResDto & { _gradeId?: number };

const mergeGrades = (students: studentResDto[], grades: GradeResponse[], subjectId?: number): StudentRow[] => {
  if (!subjectId) return students;
  const gradeMap = new Map<number, GradeResponse>();
  for (const g of grades) {
    if (g.subject.id === subjectId) gradeMap.set(g.student.id, g);
  }
  return students.map((s) => {
    const g = gradeMap.get(s.id);
    if (!g) return s;
    return { ...s, cc1: g.ccScore ?? "", tp: g.tpScore ?? "", sn1: g.snScore ?? "", _gradeId: g.gradeId };
  });
};

export const useGradeEditor = ({ subjectId, semesterId, examId = 1, students }: UseGradeEditorParams) => {
  const { data: allGrades = [] } = useGetTeacherGradesQuery();
  const [createGrade] = useCreateGradeMutation();
  const [updateGrade] = useUpdateGradeMutation();

  const [isTableEditable, setIsTableEditable] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [editedData, setEditedData] = useState<StudentRow[]>([]);
  const [saving, setSaving] = useState(false);

  const tableData = useMemo(
    () => mergeGrades(students, allGrades, subjectId),
    [students, allGrades, subjectId]
  );

  const handleEdit = useCallback(() => {
    setEditedData(tableData);
    setIsTableEditable(true);
  }, [tableData]);

  const handleConfirm = useCallback(async () => {
    if (!subjectId || !semesterId) {
      message.error("Matière ou semestre non sélectionné");
      return;
    }
    setSaving(true);

    const promises = editedData
      .filter((row) => {
        if (row._gradeId) return false; // existing grades are read-only — skip
        const cc = row.cc1 !== "" && row.cc1 != null;
        const tp = row.tp !== "" && row.tp != null;
        const sn = row.sn1 !== "" && row.sn1 != null;
        return cc || tp || sn;
      })
      .map((row) => {
        const body = {
          studentId: row.id,
          subjectId,
          examId,
          semesterId,
          ccScore: row.cc1 !== "" && row.cc1 != null ? Number(row.cc1) : undefined,
          tpScore: row.tp !== "" && row.tp != null ? Number(row.tp) : undefined,
          snScore: row.sn1 !== "" && row.sn1 != null ? Number(row.sn1) : undefined,
          assessmentType: "CC_1",
        };
        return createGrade(body).unwrap();
      });

    const results = await Promise.allSettled(promises);
    const saved = results.filter((r) => r.status === "fulfilled").length;
    const errors = results.filter((r) => r.status === "rejected").length;

    setSaving(false);
    setIsTableEditable(false);
    if (saved > 0) message.success(`${saved} note(s) enregistrée(s)`);
    if (errors > 0) message.error(`${errors} erreur(s) lors de l'enregistrement`);
  }, [editedData, subjectId, semesterId, examId, createGrade, updateGrade]);

  const filteredTableData = useMemo(() => {
    const base = isTableEditable ? editedData : tableData;
    if (!searchValue) return base;
    return base.filter((s) => {
      const full = `${s.firstName} ${s.lastName}`.toLowerCase();
      return full.includes(searchValue.toLowerCase());
    });
  }, [searchValue, tableData, editedData, isTableEditable]);

  return {
    isTableEditable,
    setIsTableEditable,
    tableData,
    editedData,
    setEditedData,
    searchValue,
    setSearchValue,
    filteredTableData,
    handleEdit,
    handleConfirm,
    saving,
  };
};
