import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import type { TranscriptResponse, TranscriptGrade } from '../api/response-dto/transcript.dto';

const LEVEL_LABELS: Record<string, string> = {
  LEVEL1: 'Licence 1', LEVEL2: 'Licence 2', LEVEL3: 'Licence 3',
  LEVEL4: 'Master 1', LEVEL5: 'Master 2',
};
const CYCLE_LABELS: Record<string, string> = {
  BACHELOR: 'Licence', MASTER: 'Master', PHD: 'Doctorat',
};

export function generateTranscriptPDF(t: TranscriptResponse) {
  const doc = new jsPDF();
  const pageWidth = doc.internal.pageSize.getWidth();
  const accent = [102, 126, 234]; // #667EEA
  let y = 15;

  // Header
  doc.setFontSize(18);
  doc.setFont('helvetica', 'bold');
  doc.text('UNIVERSITÉ DE DOUALA', pageWidth / 2, y, { align: 'center' });
  y += 7;
  doc.setFontSize(10);
  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text("Faculté des Sciences — Département d'Informatique", pageWidth / 2, y, { align: 'center' });
  y += 4;

  // Accent line
  doc.setDrawColor(...accent);
  doc.setLineWidth(1);
  doc.line(20, y, pageWidth - 20, y);
  y += 8;

  // Title
  doc.setFontSize(14);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(0);
  doc.text('RELEVÉ DE NOTES', pageWidth / 2, y, { align: 'center' });
  y += 6;
  doc.setFontSize(9);
  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text('Année Académique 2025/2026', pageWidth / 2, y, { align: 'center' });
  y += 10;

  // Student info box
  const level = LEVEL_LABELS[t.studentLevel?.studentLevel || ''] || t.studentLevel?.studentLevel || '';
  const cycle = CYCLE_LABELS[t.studentCycle || ''] || t.studentCycle || '';
  const status = t.status === 'PASSED' ? 'Admis' : 'Ajourné';

  doc.setFillColor(248, 249, 250);
  doc.roundedRect(15, y - 3, pageWidth - 30, 30, 2, 2, 'F');

  doc.setFontSize(9);
  doc.setTextColor(100);
  const col1 = 20, col2 = 65;
  const col3 = pageWidth / 2 + 5, col4 = pageWidth / 2 + 50;

  doc.text('Nom et Prénom:', col1, y + 4);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(0);
  doc.text(`${t.studentFirstName} ${t.studentLastName}`, col2, y + 4);

  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text('Matricule:', col3, y + 4);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(0);
  doc.text(t.studentMatricule || '', col4, y + 4);

  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text('Niveau:', col1, y + 12);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(0);
  doc.text(level, col2, y + 12);

  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text('Cycle:', col3, y + 12);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(0);
  doc.text(cycle, col4, y + 12);

  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text('Statut:', col1, y + 20);
  doc.setFont('helvetica', 'bold');
  doc.setTextColor(t.status === 'PASSED' ? 22 : 220, t.status === 'PASSED' ? 163 : 38, t.status === 'PASSED' ? 74 : 38);
  doc.text(status, col2, y + 20);

  y += 35;

  // Group grades by semester, deduplicate (keep best per subject)
  const grades = t.studentGrades || [];
  const bySem = new Map<string, TranscriptGrade[]>();
  for (const g of grades) {
    const sem = g.semester?.name || 'Unknown';
    if (!bySem.has(sem)) bySem.set(sem, []);
    bySem.get(sem)!.push(g);
  }

  for (const [semName, semGrades] of bySem) {
    // Deduplicate: best grade per subject
    const bySubject = new Map<string, TranscriptGrade>();
    for (const g of semGrades) {
      const code = g.subject?.subjectCode || '';
      const existing = bySubject.get(code);
      if (!existing || (g.totalScore ?? 0) > (existing.totalScore ?? 0)) {
        bySubject.set(code, g);
      }
    }
    const unique = Array.from(bySubject.values());

    // Check if we need a new page
    if (y > 240) { doc.addPage(); y = 20; }

    // Semester header
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(...accent);
    doc.text(semName, 15, y);
    y += 2;

    // Grade table
    autoTable(doc, {
      startY: y,
      head: [['Code', 'Matière', 'Crédits', 'CC', 'TP', 'SN', 'Total', 'Résultat']],
      body: unique.map(g => [
        g.subject?.subjectCode || '',
        g.subject?.subjectName || '',
        String(g.subject?.credits ?? ''),
        g.ccScore != null ? String(g.ccScore) : '—',
        g.tpScore != null ? String(g.tpScore) : '—',
        g.snScore != null ? String(g.snScore) : '—',
        `${(g.totalScore ?? 0).toFixed(2)}/20`,
        g.hasPassed ? 'Validé' : 'Non validé',
      ]),
      theme: 'grid',
      headStyles: { fillColor: accent, fontSize: 8, fontStyle: 'bold', halign: 'center' },
      bodyStyles: { fontSize: 8 },
      columnStyles: {
        0: { cellWidth: 18 },
        1: { cellWidth: 45 },
        2: { cellWidth: 15, halign: 'center' },
        3: { cellWidth: 15, halign: 'center' },
        4: { cellWidth: 15, halign: 'center' },
        5: { cellWidth: 15, halign: 'center' },
        6: { cellWidth: 22, halign: 'center', fontStyle: 'bold' },
        7: { cellWidth: 25, halign: 'center' },
      },
      didParseCell: (data) => {
        if (data.section === 'body' && data.column.index === 7) {
          data.cell.styles.textColor = data.cell.raw === 'Validé' ? [22, 163, 74] : [220, 38, 38];
          data.cell.styles.fontStyle = 'bold';
        }
        if (data.section === 'body' && data.column.index === 6) {
          const val = parseFloat(String(data.cell.raw));
          data.cell.styles.textColor = val >= 10 ? [22, 163, 74] : [220, 38, 38];
        }
      },
      margin: { left: 15, right: 15 },
    });

    y = (doc as any).lastAutoTable.finalY + 4;

    // Semester summary
    const semAvg = unique.length > 0
      ? unique.reduce((s, g) => s + (g.totalScore ?? 0), 0) / unique.length
      : 0;
    const semCredits = unique.reduce((s, g) => s + (g.hasPassed ? (g.subject?.credits ?? 0) : 0), 0);
    const totalCredits = unique.reduce((s, g) => s + (g.subject?.credits ?? 0), 0);

    doc.setFontSize(9);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(0);
    doc.text(`Moyenne: ${semAvg.toFixed(2)}/20`, 15, y);
    doc.text(`Crédits: ${semCredits}/${totalCredits}`, pageWidth - 15, y, { align: 'right' });
    y += 10;
  }

  // Summary section
  if (y > 230) { doc.addPage(); y = 20; }

  doc.setDrawColor(200);
  doc.setLineWidth(0.3);
  doc.line(15, y, pageWidth - 15, y);
  y += 8;

  // Summary boxes
  const boxW = (pageWidth - 45) / 3;
  const boxes = [
    { label: 'Moyenne Annuelle', value: `${(t.annualAverage ?? 0).toFixed(2)}/20`, color: (t.annualAverage ?? 0) >= 10 ? [22, 163, 74] : [220, 38, 38] },
    { label: 'Crédits Obtenus', value: `${t.creditsEarned ?? 0}/${t.totalCreditsRequired ?? '—'}`, color: [0, 0, 0] },
    { label: 'Progression', value: `${t.totalCreditsRequired ? Math.round(((t.creditsEarned || 0) / t.totalCreditsRequired) * 100) : 0}%`, color: accent },
  ];

  boxes.forEach((box, i) => {
    const x = 15 + i * (boxW + 7.5);
    doc.setFillColor(240, 244, 255);
    doc.roundedRect(x, y, boxW, 22, 2, 2, 'F');
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(100);
    doc.text(box.label, x + boxW / 2, y + 7, { align: 'center' });
    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(...(box.color as [number, number, number]));
    doc.text(box.value, x + boxW / 2, y + 17, { align: 'center' });
  });

  y += 35;

  // Signature
  const today = new Date().toLocaleDateString('fr-FR', { day: '2-digit', month: 'long', year: 'numeric' });
  doc.setFontSize(9);
  doc.setFont('helvetica', 'normal');
  doc.setTextColor(100);
  doc.text(`Fait à Douala, le ${today}`, pageWidth - 20, y, { align: 'right' });
  y += 6;
  doc.text('Le Chef de Département', pageWidth - 20, y, { align: 'right' });
  y += 18;
  doc.text('_________________________', pageWidth - 20, y, { align: 'right' });

  // Footer
  doc.setFontSize(7);
  doc.setTextColor(180);
  doc.text(
    `Document généré automatiquement — ManageNotes © ${new Date().getFullYear()}`,
    pageWidth / 2,
    doc.internal.pageSize.getHeight() - 10,
    { align: 'center' }
  );

  doc.save(`Releve_${t.studentMatricule}.pdf`);
}
