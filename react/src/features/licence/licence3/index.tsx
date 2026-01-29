import { GradingPage } from "../../../components/GradingPage";
import { FakeStudents } from "../../user/data";

export const Licence3 = () => (
  <GradingPage
    level="licence3"
    semester="sem1"
    pageTitle="PV Licence 3"
    headerTitle="L3"
    subjectCode="MATH301"
    subjectTopic="Mathematiques appliquees"
    period="Controle continu #1"
    NC="10"
    CANT="20"
    levelDisplay="Licence 3"
    initialData={FakeStudents}
  />
);
