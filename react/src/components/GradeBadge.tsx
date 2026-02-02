type GradeBadgeProps = {
  grade: string | number;
  className?: string;
};

const getGradeClass = (grade: string | number): string => {
  const gradeStr = String(grade).toUpperCase();
  
  if (gradeStr.startsWith('A')) return 'grade-badge-a';
  if (gradeStr.startsWith('B')) return 'grade-badge-b';
  if (gradeStr.startsWith('C')) return 'grade-badge-c';
  if (gradeStr.startsWith('D')) return 'grade-badge-d';
  if (gradeStr.startsWith('F')) return 'grade-badge-f';
  
  const numericGrade = typeof grade === 'number' ? grade : parseFloat(gradeStr);
  if (!isNaN(numericGrade)) {
    if (numericGrade >= 90) return 'grade-badge-a';
    if (numericGrade >= 80) return 'grade-badge-b';
    if (numericGrade >= 70) return 'grade-badge-c';
    if (numericGrade >= 60) return 'grade-badge-d';
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
