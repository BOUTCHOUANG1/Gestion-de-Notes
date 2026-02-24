import { Document, Page, Text, View, StyleSheet, Font } from '@react-pdf/renderer';
import type { TranscriptResponse, TranscriptGrade } from '../../api/response-dto/transcript.dto';

Font.register({
  family: 'Inter',
  fonts: [
    { src: 'https://fonts.gstatic.com/s/inter/v18/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuLyfAZ9hjQ.ttf', fontWeight: 400 },
    { src: 'https://fonts.gstatic.com/s/inter/v18/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuI6fAZ9hjQ.ttf', fontWeight: 600 },
    { src: 'https://fonts.gstatic.com/s/inter/v18/UcCO3FwrK3iLTeHuS_nVMrMxCp50SjIw2boKoduKmMEVuFuYAZ9hjQ.ttf', fontWeight: 700 },
  ],
});

const s = StyleSheet.create({
  page: { padding: 40, fontFamily: 'Inter', fontSize: 9, color: '#1a1a1a' },
  header: { textAlign: 'center', marginBottom: 20 },
  uniName: { fontSize: 16, fontWeight: 700, marginBottom: 2 },
  uniSub: { fontSize: 10, color: '#555', marginBottom: 4 },
  title: { fontSize: 14, fontWeight: 700, marginTop: 10, marginBottom: 2, textTransform: 'uppercase', letterSpacing: 2 },
  divider: { borderBottomWidth: 2, borderBottomColor: '#667EEA', marginVertical: 8 },
  thinDivider: { borderBottomWidth: 0.5, borderBottomColor: '#ccc', marginVertical: 6 },
  row: { flexDirection: 'row', marginBottom: 3 },
  label: { width: 120, color: '#666', fontSize: 9 },
  value: { fontWeight: 600, fontSize: 9 },
  semTitle: { fontSize: 11, fontWeight: 700, marginTop: 14, marginBottom: 6, color: '#667EEA' },
  // Table
  tableHeader: { flexDirection: 'row', backgroundColor: '#667EEA', paddingVertical: 5, paddingHorizontal: 4 },
  tableHeaderCell: { color: '#fff', fontWeight: 700, fontSize: 8 },
  tableRow: { flexDirection: 'row', paddingVertical: 4, paddingHorizontal: 4, borderBottomWidth: 0.5, borderBottomColor: '#e5e5e5' },
  tableRowAlt: { backgroundColor: '#f9f9f9' },
  tableCell: { fontSize: 8 },
  // Column widths
  colCode: { width: '12%' },
  colSubject: { width: '28%' },
  colCredits: { width: '8%', textAlign: 'center' },
  colCC: { width: '10%', textAlign: 'center' },
  colTP: { width: '10%', textAlign: 'center' },
  colSN: { width: '10%', textAlign: 'center' },
  colTotal: { width: '10%', textAlign: 'center' },
  colResult: { width: '12%', textAlign: 'center' },
  // Summary
  summaryBox: { flexDirection: 'row', marginTop: 12, gap: 12 },
  summaryCard: { flex: 1, backgroundColor: '#f0f4ff', borderRadius: 4, padding: 10, textAlign: 'center' },
  summaryLabel: { fontSize: 8, color: '#666', marginBottom: 2 },
  summaryValue: { fontSize: 14, fontWeight: 700 },
  pass: { color: '#16a34a' },
  fail: { color: '#dc2626' },
  footer: { position: 'absolute', bottom: 30, left: 40, right: 40, textAlign: 'center', fontSize: 7, color: '#999' },
  stamp: { marginTop: 30, textAlign: 'right', fontSize: 8, color: '#666' },
});

const LEVEL_LABELS: Record<string, string> = {
  LEVEL1: 'Licence 1', LEVEL2: 'Licence 2', LEVEL3: 'Licence 3',
  LEVEL4: 'Master 1', LEVEL5: 'Master 2',
};
const CYCLE_LABELS: Record<string, string> = {
  BACHELOR: 'Licence', MASTER: 'Master', PHD: 'Doctorat',
};

interface Props { transcript: TranscriptResponse; }

const GradeTable = ({ grades, semLabel }: { grades: TranscriptGrade[]; semLabel: string }) => {
  // Deduplicate: keep best grade per subject
  const bySubject = new Map<string, TranscriptGrade>();
  for (const g of grades) {
    const code = g.subject?.subjectCode || '';
    const existing = bySubject.get(code);
    if (!existing || (g.totalScore ?? 0) > (existing.totalScore ?? 0)) {
      bySubject.set(code, g);
    }
  }
  const unique = Array.from(bySubject.values());
  const semAvg = unique.length > 0
    ? unique.reduce((sum, g) => sum + (g.totalScore ?? 0), 0) / unique.length
    : 0;
  const semCredits = unique.reduce((sum, g) => sum + (g.hasPassed ? (g.subject?.credits ?? 0) : 0), 0);
  const totalCredits = unique.reduce((sum, g) => sum + (g.subject?.credits ?? 0), 0);

  return (
    <View>
      <Text style={s.semTitle}>{semLabel}</Text>
      <View style={s.tableHeader}>
        <Text style={[s.tableHeaderCell, s.colCode]}>Code</Text>
        <Text style={[s.tableHeaderCell, s.colSubject]}>Matière</Text>
        <Text style={[s.tableHeaderCell, s.colCredits]}>Crédits</Text>
        <Text style={[s.tableHeaderCell, s.colCC]}>CC</Text>
        <Text style={[s.tableHeaderCell, s.colTP]}>TP</Text>
        <Text style={[s.tableHeaderCell, s.colSN]}>SN</Text>
        <Text style={[s.tableHeaderCell, s.colTotal]}>Total</Text>
        <Text style={[s.tableHeaderCell, s.colResult]}>Résultat</Text>
      </View>
      {unique.map((g, i) => (
        <View key={g.gradeId} style={[s.tableRow, i % 2 === 1 ? s.tableRowAlt : {}]}>
          <Text style={[s.tableCell, s.colCode]}>{g.subject?.subjectCode}</Text>
          <Text style={[s.tableCell, s.colSubject]}>{g.subject?.subjectName}</Text>
          <Text style={[s.tableCell, s.colCredits]}>{g.subject?.credits}</Text>
          <Text style={[s.tableCell, s.colCC]}>{g.ccScore ?? '—'}</Text>
          <Text style={[s.tableCell, s.colTP]}>{g.tpScore ?? '—'}</Text>
          <Text style={[s.tableCell, s.colSN]}>{g.snScore ?? '—'}</Text>
          <Text style={[s.tableCell, s.colTotal, g.hasPassed ? s.pass : s.fail]}>
            {g.totalScore?.toFixed(2)}/20
          </Text>
          <Text style={[s.tableCell, s.colResult, g.hasPassed ? s.pass : s.fail]}>
            {g.hasPassed ? 'Validé' : 'Non validé'}
          </Text>
        </View>
      ))}
      <View style={[s.row, { marginTop: 6, justifyContent: 'space-between' }]}>
        <Text style={{ fontSize: 9, fontWeight: 600 }}>
          Moyenne du semestre: <Text style={semAvg >= 10 ? s.pass : s.fail}>{semAvg.toFixed(2)}/20</Text>
        </Text>
        <Text style={{ fontSize: 9, fontWeight: 600 }}>
          Crédits: {semCredits}/{totalCredits}
        </Text>
      </View>
    </View>
  );
};

export const TranscriptPDF = ({ transcript: t }: Props) => {
  const level = t.studentLevel?.studentLevel || '';
  const grades = t.studentGrades || [];
  const bySem = new Map<string, TranscriptGrade[]>();
  for (const g of grades) {
    const sem = g.semester?.name || 'Unknown';
    if (!bySem.has(sem)) bySem.set(sem, []);
    bySem.get(sem)!.push(g);
  }

  const today = new Date().toLocaleDateString('fr-FR', { day: '2-digit', month: 'long', year: 'numeric' });
  const progressPct = t.totalCreditsRequired
    ? Math.round(((t.creditsEarned || 0) / t.totalCreditsRequired) * 100)
    : 0;

  return (
    <Document>
      <Page size="A4" style={s.page}>
        {/* Header */}
        <View style={s.header}>
          <Text style={s.uniName}>UNIVERSITÉ DE DOUALA</Text>
          <Text style={s.uniSub}>Faculté des Sciences — Département d'Informatique</Text>
          <View style={s.divider} />
          <Text style={s.title}>Relevé de Notes</Text>
          <Text style={{ fontSize: 9, color: '#666' }}>Année Académique 2025/2026</Text>
        </View>

        {/* Student Info */}
        <View style={{ backgroundColor: '#f8f9fa', borderRadius: 4, padding: 10, marginBottom: 12 }}>
          <View style={s.row}>
            <Text style={s.label}>Nom et Prénom:</Text>
            <Text style={s.value}>{t.studentFirstName} {t.studentLastName}</Text>
          </View>
          <View style={s.row}>
            <Text style={s.label}>Matricule:</Text>
            <Text style={s.value}>{t.studentMatricule}</Text>
          </View>
          <View style={s.row}>
            <Text style={s.label}>Niveau:</Text>
            <Text style={s.value}>{LEVEL_LABELS[level] || level}</Text>
          </View>
          <View style={s.row}>
            <Text style={s.label}>Cycle:</Text>
            <Text style={s.value}>{CYCLE_LABELS[t.studentCycle || ''] || t.studentCycle}</Text>
          </View>
          <View style={s.row}>
            <Text style={s.label}>Statut:</Text>
            <Text style={[s.value, t.status === 'PASSED' ? s.pass : s.fail]}>
              {t.status === 'PASSED' ? 'Admis' : 'Ajourné'}
            </Text>
          </View>
        </View>

        {/* Grade tables by semester */}
        {Array.from(bySem.entries()).map(([semName, semGrades]) => (
          <GradeTable key={semName} grades={semGrades} semLabel={semName} />
        ))}

        {/* Summary */}
        <View style={s.thinDivider} />
        <View style={s.summaryBox}>
          <View style={s.summaryCard}>
            <Text style={s.summaryLabel}>Moyenne Annuelle</Text>
            <Text style={[s.summaryValue, (t.annualAverage ?? 0) >= 10 ? s.pass : s.fail]}>
              {(t.annualAverage ?? 0).toFixed(2)}/20
            </Text>
          </View>
          <View style={s.summaryCard}>
            <Text style={s.summaryLabel}>Crédits Obtenus</Text>
            <Text style={s.summaryValue}>
              {t.creditsEarned ?? 0}/{t.totalCreditsRequired ?? '—'}
            </Text>
          </View>
          <View style={s.summaryCard}>
            <Text style={s.summaryLabel}>Progression</Text>
            <Text style={s.summaryValue}>{progressPct}%</Text>
          </View>
        </View>

        {/* Stamp */}
        <View style={s.stamp}>
          <Text>Fait à Douala, le {today}</Text>
          <Text style={{ marginTop: 4 }}>Le Chef de Département</Text>
          <Text style={{ marginTop: 20, fontStyle: 'italic' }}>_________________________</Text>
        </View>

        {/* Footer */}
        <View style={s.footer}>
          <Text>Document généré automatiquement — ManageNotes © {new Date().getFullYear()}</Text>
        </View>
      </Page>
    </Document>
  );
};
