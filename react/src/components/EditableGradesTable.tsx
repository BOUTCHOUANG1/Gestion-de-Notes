import { useState, useRef } from "react";
import { Table, Input, Button, Tag } from "antd";
import { MagnifyingGlassCircleIcon, PrinterIcon } from "@heroicons/react/24/solid";
import { GradesEdition } from "./GradesEditionBtn";
import { EditPvButton } from "./EditPvButton";
import { studentResDto } from "../api/reponse-dto/user.res.dto";

// CC 30%, TP 20%, SN 50% — all on /20
const calcTotal = (r: studentResDto) => {
  const cc = Number(r.cc1) || 0;
  const tp = Number(r.tp) || 0;
  const sn = Number(r.sn1) || 0;
  return Math.round((cc * 0.3 + tp * 0.2 + sn * 0.5) * 100) / 100;
};

const getMention = (score: number) => {
  if (score >= 16) return { text: "TB", color: "green" };
  if (score >= 14) return { text: "B", color: "blue" };
  if (score >= 12) return { text: "AB", color: "cyan" };
  if (score >= 10) return { text: "P", color: "orange" };
  return { text: "Échec", color: "red" };
};

interface EditableGradesTableProps {
  extraColumns?: { title: string; dataIndex: string }[];
  isEditable: boolean;
  data: studentResDto[];
  onGradesChange?: (data: studentResDto[]) => void;
  onEdit: () => void;
  onConfirm: () => void;
  isDataEditable?: boolean;
  setIsDataEditable: (value: boolean) => void;
  onSearch?: (value: string) => void;
  saving?: boolean;
  pvInfo?: { subject: string; code: string; semester: string; level: string; teacher: string };
}

export const EditableGradesTable = ({
  extraColumns = [],
  isEditable,
  data,
  onGradesChange,
  onEdit,
  onConfirm,
  setIsDataEditable,
  isDataEditable,
  onSearch,
  saving,
  pvInfo,
}: EditableGradesTableProps) => {
  const [localData, setLocalData] = useState<studentResDto[]>(data);

  // Sync local data when entering/exiting edit mode or when data changes while not editing
  const prevEditable = useRef(isEditable);
  if (isEditable && !prevEditable.current) {
    // Just entered edit mode — snapshot current data
    setLocalData(data);
  }
  prevEditable.current = isEditable;

  const displayData = isEditable ? localData : data || [];

  const handleCellChange = (recordId: number, field: string, val: string) => {
    const newData = localData.map((s) =>
      s.id === recordId ? { ...s, [field]: val } : s
    );
    setLocalData(newData);
    onGradesChange?.(newData);
  };

  // Reusable editable grade cell — only editable if student has no existing grade
  const hasGrade = (record: studentResDto) => !!(record as any)._gradeId;

  const gradeCol = (title: string, field: string) => ({
    title,
    dataIndex: field,
    render: (text: string, record: studentResDto) =>
      isEditable && !hasGrade(record) ? (
        <Input
          type="number"
          min={0}
          max={20}
          step={0.25}
          value={(record[field] as string) || ""}
          onChange={(e) => {
            const val = e.target.value;
            if (val !== "" && (Number(val) < 0 || Number(val) > 20)) return;
            handleCellChange(record.id, field, val);
          }}
          style={{ width: 70 }}
        />
      ) : (
        text ?? "-"
      ),
    sorter: (a: studentResDto, b: studentResDto) =>
      Number(a[field] || 0) - Number(b[field] || 0),
  });

  const baseColumns = [
    {
      title: "Matricule",
      dataIndex: "matricule",
      render: (text: string) => text || "-",
      sorter: (a: studentResDto, b: studentResDto) =>
        String(a.matricule || "").localeCompare(String(b.matricule || "")),
    },
    {
      title: "Nom Complet",
      key: "fullName",
      render: (_: unknown, record: studentResDto) =>
        `${record.firstName} ${record.lastName}`,
      sorter: (a: studentResDto, b: studentResDto) =>
        String(a.firstName).localeCompare(String(b.firstName)),
    },
    gradeCol("CC /20", "cc1"),
    gradeCol("TP /20", "tp"),
    gradeCol("SN /20", "sn1"),
    {
      title: "Total /20",
      key: "total",
      render: (_: unknown, record: studentResDto) => {
        const total = calcTotal(record);
        const mention = getMention(total);
        return (
          <span>
            {total} <Tag color={mention.color} style={{ marginLeft: 4 }}>{mention.text}</Tag>
          </span>
        );
      },
      sorter: (a: studentResDto, b: studentResDto) => calcTotal(a) - calcTotal(b),
    },
  ];

  const columns = [
    ...baseColumns,
    ...extraColumns.map((col) => ({
      ...col,
      render: (text: string, record: studentResDto) =>
        isEditable && !hasGrade(record) ? (
          <Input
            type="number"
            min={0}
            max={20}
            value={(record[col.dataIndex] as string) || ""}
            onChange={(e) => {
              handleCellChange(record.id, col.dataIndex, e.target.value);
            }}
            style={{ width: 70 }}
          />
        ) : (
          text
        ),
    })),
  ];

  const gradeFields = ["cc1", "tp", "sn1", ...extraColumns.map((c) => c.dataIndex)];
  const attributedGrades = displayData.filter((s) =>
    gradeFields.some((f) => {
      const v = s[f];
      return v !== undefined && v !== null && v !== "";
    })
  ).length;

  const tagColor = attributedGrades < displayData.length ? "orange" : "green";

  return (
    <div>
      <div className="flex justify-between mt-4 mb-2 w-full">
        <div className="flex w-3/4">
          <Input
            prefix={<MagnifyingGlassCircleIcon width={18} className="text-[var(--text-tertiary)]" />}
            placeholder="Rechercher par nom..."
            onChange={(e) => onSearch?.(e.target.value)}
            allowClear
          />
        </div>
        <div>
          {isDataEditable ? (
            <GradesEdition editGrades={onEdit} confirmGrades={onConfirm} setIsTableEditable={setIsDataEditable} saving={saving} />
          ) : (
            <EditPvButton setIsTableEditable={setIsDataEditable} onEdit={onEdit} />
          )}
        </div>
      </div>
      <Table rowKey="id" columns={columns} dataSource={displayData} pagination={{ pageSize: 7 }} scroll={{ x: true }} />
      <div className="flex justify-between mt-4 w-full">
        <Button
          icon={<PrinterIcon width={16} />}
          className={`!py-2 ${isEditable ? "!bg-gray-100 !text-[var(--text-tertiary)]" : ""}`}
          disabled={isEditable}
          onClick={async () => {
            const { generatePvPDF } = await import('../utils/generatePvPDF');
            generatePvPDF(displayData, pvInfo || { subject: '', code: '', semester: '', level: '', teacher: '' });
          }}
        >
          Imprimer PV
        </Button>
        <Tag color={tagColor} className="!h-7 !flex !items-center !justify-center text-xs">
          {attributedGrades} notes attribuées sur {displayData.length} étudiants
        </Tag>
      </div>
    </div>
  );
};
