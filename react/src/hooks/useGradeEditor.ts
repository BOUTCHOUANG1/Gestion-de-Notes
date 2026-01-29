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
        return JSON.parse(saved);
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
    return base.filter((student) =>
      (student.firstName || "")
        .toLowerCase()
        .includes(searchValue.toLowerCase())
    );
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
