import { useState, useMemo } from "react";
import { studentResDto } from "../api/reponse-dto/user.res.dto";

export interface UseGradeEditorParams {
  level: string;
  semester: string;
  initialData?: studentResDto[];
}

export const useGradeEditor = ({ level, semester, initialData = [] }: UseGradeEditorParams) => {
  const storageKey = `${level}_${semester}_tableData`;
  
  const [isTableEditable, setIsTableEditable] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  
  const [tableData, setTableData] = useState<studentResDto[]>(() => {
    const saved = localStorage.getItem(storageKey);
    if (saved) {
      try {
        const parsed = JSON.parse(saved) as studentResDto[];
        // Merge: keep grades from localStorage but update student info from API
        if (initialData.length > 0) {
          const savedMap = new Map(parsed.map(s => [s.id, s]));
          return initialData.map(s => ({ ...s, ...savedMap.get(s.id), firstName: s.firstName, lastName: s.lastName, matricule: s.matricule }));
        }
        return parsed;
      } catch {
        return initialData;
      }
    }
    return initialData;
  });
  
  const [editedData, setEditedData] = useState<studentResDto[]>(tableData);

  const handleEdit = () => {
    setEditedData(tableData);
    setIsTableEditable(true);
  };

  const handleConfirm = () => {
    setTableData(editedData);
    setIsTableEditable(false);
    localStorage.setItem(storageKey, JSON.stringify(editedData));
  };

  const filteredTableData = useMemo(() => {
    const base = isTableEditable ? editedData : tableData;
    if (!searchValue) return base;
    return base.filter((student) => {
      const full = `${student.firstName} ${student.lastName}`.toLowerCase();
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
  };
};
