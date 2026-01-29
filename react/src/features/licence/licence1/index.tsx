import { GradingPage } from "../../../components/GradingPage";
import { FakeStudents } from "../../user/data";

export const Licence1 = () => (
  <GradingPage
    level="licence1"
    semester="sem1"
    pageTitle="Notes Licence 1"
    headerTitle="L1"
    subjectCode="MATH101"
    subjectTopic="Mathematiques appliquees"
    period="Controle continu #1"
    NC="10"
    CANT="20"
    levelDisplay="Licence 1"
    initialData={FakeStudents}
  />
);
