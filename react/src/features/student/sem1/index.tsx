import { GradingPage } from "../../../components/GradingPage";
import { FakeStudents } from "../../user/data";

export default function Semester1() {
  return (
    <GradingPage
      level="student"
      semester="sem1"
      pageTitle="Semestre 1"
      headerTitle="S1"
      subjectCode="GENERAL101"
      subjectTopic="Notes Semestre 1"
      period="Controle continu #1"
      NC="10"
      CANT="20"
      levelDisplay="Semestre 1"
      initialData={FakeStudents}
    />
  );
}
