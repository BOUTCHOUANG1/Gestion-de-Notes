import { GradingPage } from "../../../components/GradingPage";
import { FakeStudents } from "../../user/data";

export const Master1 = () => (
  <GradingPage
    level="master1"
    semester="sem1"
    pageTitle="PV Master 1"
    headerTitle="M1"
    subjectCode="MATH401"
    subjectTopic="Mathematiques appliquees"
    period="Controle continu #1"
    NC="10"
    CANT="20"
    levelDisplay="Master 1"
    initialData={FakeStudents}
  />
);
