import { Table, Tag, Empty, Spin } from "antd";
import { useGetStudentTranscriptQuery } from "../../transcript/api/transcriptApi";
import { usePageTitle } from "../../../hooks/usePageTitle";
import { TranscriptGrade } from "../../../api/response-dto/transcript.dto";

const getMention = (score: number) => {
  if (score >= 16) return { text: "TB", color: "green" };
  if (score >= 14) return { text: "B", color: "blue" };
  if (score >= 12) return { text: "AB", color: "cyan" };
  if (score >= 10) return { text: "P", color: "orange" };
  return { text: "Échec", color: "red" };
};

export default function Semester1() {
  usePageTitle("Semestre 1");
  const { data: transcript, isLoading } = useGetStudentTranscriptQuery();

  const grades = transcript?.semesters
    ?.find((s) => s.semesterName?.includes("1"))
    ?.grades || [];

  if (isLoading) return <Spin className="flex justify-center mt-20" />;
  if (!grades.length) return <Empty description="Aucune note pour le Semestre 1" className="mt-20" />;

  const columns = [
    { title: "Matière", dataIndex: "subjectName", key: "subject" },
    { title: "Code", dataIndex: "subjectCode", key: "code" },
    {
      title: "Note /20", dataIndex: "grade", key: "grade",
      render: (v: number | null) => {
        if (v == null) return "—";
        const m = getMention(v);
        return <span>{v} <Tag color={m.color}>{m.text}</Tag></span>;
      },
    },
    { title: "Crédits", dataIndex: "credits", key: "credits" },
    {
      title: "Résultat", dataIndex: "passed", key: "passed",
      render: (v: boolean) => v ? <Tag color="green">Validé</Tag> : <Tag color="red">Non validé</Tag>,
    },
  ];

  return (
    <div className="p-6">
      <h2 className="text-lg font-semibold mb-4">Mes Notes — Semestre 1</h2>
      <Table<TranscriptGrade> rowKey="subjectCode" columns={columns} dataSource={grades} pagination={false} />
    </div>
  );
}
