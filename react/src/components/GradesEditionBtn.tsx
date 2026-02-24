import { PencilSquareIcon, XCircleIcon } from "@heroicons/react/24/solid";
import { Button } from "antd";

interface GradesEditionProps {
    editGrades?: () => void;
    confirmGrades?: () => void;
    setIsTableEditable: (value: boolean) => void;
    saving?: boolean;
}
 
export const GradesEdition = ({
  confirmGrades,
  setIsTableEditable,
  saving,
}: GradesEditionProps) => {
  return (
    <div className="flex gap-2">
      <Button
        className="!bg-white !text-primary w-[150px] border border-gray-300"
        icon={<XCircleIcon width={20} />}
        onClick={() => setIsTableEditable(false)}
        disabled={saving}
      >
        Annuler
      </Button>
      <Button
        className="w-[150px]"
        icon={<PencilSquareIcon width={20} />}
        onClick={confirmGrades}
        loading={saving}
      >
        Confirmer
      </Button>
    </div>
  );
};
