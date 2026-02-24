import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { studentResDto } from '../api/reponse-dto/user.res.dto';

interface PvInfo {
  subject: string;
  code: string;
  semester: string;
  level: string;
  teacher: string;
}

const calcTotal = (r: studentResDto) => {
  const cc = Number(r.cc1) || 0;
  const tp = Number(r.tp) || 0;
  const sn = Number(r.sn1) || 0;
  return Math.round((cc * 0.3 + tp * 0.2 + sn * 0.5) * 100) / 100;
};

const getMention = (score: number) => {
  if (score >= 16) return 'TB';
  if (score >= 14) return 'B';
  if (score >= 12) return 'AB';
  if (score >= 10) return 'P';
  return 'Échec';
};

export const generatePvPDF = (students: studentResDto[], info: PvInfo) => {
  const doc = new jsPDF('portrait', 'mm', 'a4');
  const w = doc.internal.pageSize.getWidth();

  // Header
  doc.setFontSize(11);
  doc.setFont('helvetica', 'bold');
  doc.text('UNIVERSITÉ DE DOUALA', w / 2, 15, { align: 'center' });
  doc.setFontSize(9);
  doc.setFont('helvetica', 'normal');
  doc.text('Faculté des Sciences', w / 2, 20, { align: 'center' });

  doc.setFontSize(13);
  doc.setFont('helvetica', 'bold');
  doc.text('PROCÈS-VERBAL DES NOTES', w / 2, 30, { align: 'center' });

  // Info block
  doc.setFontSize(9);
  doc.setFont('helvetica', 'normal');
  const y0 = 38;
  doc.text(`Matière : ${info.subject} (${info.code})`, 14, y0);
  doc.text(`Niveau : ${info.level}`, 14, y0 + 5);
  doc.text(`Période : ${info.semester}`, w - 14, y0, { align: 'right' });
  doc.text(`Enseignant : ${info.teacher}`, w - 14, y0 + 5, { align: 'right' });
  doc.text(`Effectif : ${students.length} étudiants`, 14, y0 + 10);
  doc.text(`Date : ${new Date().toLocaleDateString('fr-FR')}`, w - 14, y0 + 10, { align: 'right' });

  // Table
  const sorted = [...students].sort((a, b) =>
    String(a.matricule || '').localeCompare(String(b.matricule || ''))
  );

  const rows = sorted.map((s, i) => {
    const total = calcTotal(s);
    return [
      i + 1,
      s.matricule || '-',
      `${s.lastName} ${s.firstName}`,
      s.cc1 !== '' && s.cc1 != null ? Number(s.cc1).toFixed(2) : '-',
      s.tp !== '' && s.tp != null ? Number(s.tp).toFixed(2) : '-',
      s.sn1 !== '' && s.sn1 != null ? Number(s.sn1).toFixed(2) : '-',
      total.toFixed(2),
      getMention(total),
    ];
  });

  autoTable(doc, {
    startY: y0 + 16,
    head: [['N°', 'Matricule', 'Nom & Prénom', 'CC /20', 'TP /20', 'SN /20', 'Total /20', 'Mention']],
    body: rows,
    styles: { fontSize: 8, cellPadding: 2 },
    headStyles: { fillColor: [102, 126, 234], textColor: 255, fontStyle: 'bold' },
    columnStyles: {
      0: { cellWidth: 10, halign: 'center' },
      3: { halign: 'center' },
      4: { halign: 'center' },
      5: { halign: 'center' },
      6: { halign: 'center', fontStyle: 'bold' },
      7: { halign: 'center' },
    },
    didParseCell: (data) => {
      if (data.section === 'body' && data.column.index === 7) {
        const m = String(data.cell.raw);
        if (m === 'Échec') data.cell.styles.textColor = [220, 38, 38];
        else if (m === 'TB') data.cell.styles.textColor = [22, 163, 74];
        else if (m === 'B') data.cell.styles.textColor = [37, 99, 235];
        else if (m === 'P') data.cell.styles.textColor = [234, 88, 12];
      }
    },
  });

  // Stats
  const totals = sorted.map(calcTotal);
  const graded = totals.filter((_, i) => {
    const s = sorted[i];
    return (s.cc1 != null && s.cc1 !== '') || (s.sn1 != null && s.sn1 !== '');
  });
  const avg = graded.length ? (graded.reduce((a, b) => a + b, 0) / graded.length) : 0;
  const passed = graded.filter(t => t >= 10).length;

  const finalY = (doc as any).lastAutoTable.finalY + 8;
  doc.setFontSize(9);
  doc.text(`Moyenne générale : ${avg.toFixed(2)}/20`, 14, finalY);
  doc.text(`Admis : ${passed}/${graded.length} (${graded.length ? Math.round(passed / graded.length * 100) : 0}%)`, 14, finalY + 5);
  doc.text(`Notes attribuées : ${graded.length}/${students.length}`, w - 14, finalY, { align: 'right' });

  // Signature
  doc.text('Signature de l\'enseignant :', w - 60, finalY + 20);
  doc.line(w - 60, finalY + 30, w - 14, finalY + 30);

  // Footer
  const pageH = doc.internal.pageSize.getHeight();
  doc.setFontSize(7);
  doc.setTextColor(150);
  doc.text(`PV généré le ${new Date().toLocaleString('fr-FR')} — ManageNotes`, w / 2, pageH - 8, { align: 'center' });

  doc.save(`PV_${info.code}_${info.level.replace(/\s/g, '_')}.pdf`);
};
