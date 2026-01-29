import { GradingPage } from "../../../components/GradingPage";
import { FakeStudents } from "../../user/data";

export default function Semester2() {
  return (
    <GradingPage
      level="student"
      semester="sem2"
      pageTitle="Semestre 2"
      headerTitle="S2"
      subjectCode="GENERAL201"
      subjectTopic="Notes Semestre 2"
      period="Controle continu #1"
      NC="10"
      CANT="20"
      levelDisplay="Semestre 2"
      initialData={FakeStudents}
    />
  );
}
