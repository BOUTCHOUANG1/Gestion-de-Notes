type GradeBadgeProps = {
  grade: string | number;
  className?: string;
};

const getGradeClass = (grade: string | number): string => {
  const numericGrade = typeof grade === 'number' ? grade : parseFloat(String(grade));
  if (!isNaN(numericGrade)) {
    if (numericGrade >= 16) return 'grade-badge-a';
    if (numericGrade >= 14) return 'grade-badge-b';
    if (numericGrade >= 12) return 'grade-badge-c';
    if (numericGrade >= 10) return 'grade-badge-d';
    return 'grade-badge-f';
  }
  return 'grade-badge-c';
};

export const GradeBadge = ({ grade, className = '' }: GradeBadgeProps) => {
  const gradeClass = getGradeClass(grade);
  
  return (
    <span className={`grade-badge ${gradeClass} ${className}`.trim()}>
      {grade}
    </span>
  );
};
