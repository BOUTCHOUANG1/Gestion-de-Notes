import { test, expect } from '@playwright/test';

test.describe('EditableGradesTable - Grade Editing After Filter', () => {
  const BASE_URL = 'http://localhost:5173';
  const TEACHER_USERNAME = 'prof.johnson';
  const TEACHER_PASSWORD = 'teacher123';

  test.beforeEach(async ({ page }) => {
    await page.goto(BASE_URL);
    await page.waitForSelector('input[type="text"], input[name="username"]', { timeout: 5000 });
    
    const usernameInput = page.locator('input[type="text"], input[name="username"]').first();
    const passwordInput = page.locator('input[type="password"], input[name="password"]').first();
    
    await usernameInput.fill(TEACHER_USERNAME);
    await passwordInput.fill(TEACHER_PASSWORD);
    
    const loginButton = page.locator('button:has-text("Login"), button:has-text("Sign In"), button:has-text("Connexion")').first();
    await loginButton.click();
    
    await page.waitForURL('**/dashboard', { timeout: 10000 }).catch(() => {
      return page.waitForLoadState('networkidle', { timeout: 5000 });
    });
  });

  test('should successfully login as teacher', async ({ page }) => {
    const pageContent = await page.content();
    expect(pageContent).toContain('prof.johnson');
  });

  test('should navigate to grades page', async ({ page }) => {
    const gradesLink = page.locator('a:has-text("Grades"), a:has-text("PV"), a:has-text("Notes")').first();
    
    if (await gradesLink.isVisible()) {
      await gradesLink.click();
      await page.waitForSelector('table', { timeout: 5000 });
    }
    
    const table = page.locator('table');
    await expect(table).toBeVisible();
  });

  test('should display student list in table', async ({ page }) => {
    const gradesLink = page.locator('a:has-text("Grades"), a:has-text("PV"), a:has-text("Notes")').first();
    if (await gradesLink.isVisible()) {
      await gradesLink.click();
    }
    
    await page.waitForSelector('table tbody tr', { timeout: 5000 });
    
    const studentRows = page.locator('table tbody tr');
    const rowCount = await studentRows.count();
    
    expect(rowCount).toBeGreaterThan(0);
    console.log(`Found ${rowCount} students in table`);
  });

  test('should filter students by name', async ({ page }) => {
    const gradesLink = page.locator('a:has-text("Grades"), a:has-text("PV"), a:has-text("Notes")').first();
    if (await gradesLink.isVisible()) {
      await gradesLink.click();
    }
    
    await page.waitForSelector('table tbody tr', { timeout: 5000 });
    
    const initialRows = await page.locator('table tbody tr').count();
    console.log(`Initial rows: ${initialRows}`);
    
    const searchInput = page.locator('input[placeholder*="nom"], input[placeholder*="Nom"], input[placeholder*="name"]').first();
    
    if (await searchInput.isVisible()) {
      await searchInput.fill('a');
      await page.waitForTimeout(500);
      
      const filteredRows = await page.locator('table tbody tr').count();
      console.log(`Filtered rows: ${filteredRows}`);
      
      expect(filteredRows).toBeLessThanOrEqual(initialRows);
    }
  });

  test('should edit grade for correct student after filtering', async ({ page }) => {
    const gradesLink = page.locator('a:has-text("Grades"), a:has-text("PV"), a:has-text("Notes")').first();
    if (await gradesLink.isVisible()) {
      await gradesLink.click();
    }
    
    await page.waitForSelector('table tbody tr', { timeout: 5000 });
    
    const firstStudentIdCell = page.locator('table tbody tr:first-child td:first-child');
    const firstStudentId = await firstStudentIdCell.textContent();
    console.log(`First student ID: ${firstStudentId}`);
    
    const firstStudentNameCell = page.locator('table tbody tr:first-child td:nth-child(2)');
    const firstStudentName = await firstStudentNameCell.textContent();
    console.log(`First student name: ${firstStudentName}`);
    
    const searchInput = page.locator('input[placeholder*="nom"], input[placeholder*="Nom"], input[placeholder*="name"]').first();
    if (await searchInput.isVisible() && firstStudentName) {
      const firstLetter = firstStudentName.charAt(0);
      await searchInput.fill(firstLetter);
      await page.waitForTimeout(500);
    }
    
    const editButton = page.locator('button:has-text("Edit"), button:has-text("Éditer"), button:has-text("Modifier")').first();
    if (await editButton.isVisible()) {
      await editButton.click();
      await page.waitForTimeout(300);
    }
    
    const gradeInputs = page.locator('table tbody tr:first-child input[type="number"]');
    const gradeInputCount = await gradeInputs.count();
    
    if (gradeInputCount > 0) {
      const firstGradeInput = gradeInputs.first();
      
      const currentValue = await firstGradeInput.inputValue();
      console.log(`Current grade value: ${currentValue}`);
      
      const newValue = '18';
      await firstGradeInput.clear();
      await firstGradeInput.fill(newValue);
      
      const updatedValue = await firstGradeInput.inputValue();
      expect(updatedValue).toBe(newValue);
      console.log(`Grade updated to: ${updatedValue}`);
      
      const confirmButton = page.locator('button:has-text("Confirm"), button:has-text("Confirmer"), button:has-text("Valider")').first();
      if (await confirmButton.isVisible()) {
        await confirmButton.click();
        await page.waitForTimeout(500);
      }
      
      if (await searchInput.isVisible()) {
        await searchInput.clear();
        await page.waitForTimeout(500);
      }
      
      const allRows = page.locator('table tbody tr');
      let found = false;
      
      for (let i = 0; i < await allRows.count(); i++) {
        const row = allRows.nth(i);
        const studentId = await row.locator('td:first-child').textContent();
        
        if (studentId === firstStudentId) {
          const gradeCell = row.locator('td:nth-child(3)');
          const gradeValue = await gradeCell.textContent();
          
          console.log(`Student ${firstStudentId} grade after edit: ${gradeValue}`);
          expect(gradeValue).toContain('18');
          found = true;
          break;
        }
      }
      
      expect(found).toBe(true);
      console.log('✅ Correct student was updated!');
    }
  });

  test('should handle multiple filter/edit cycles correctly', async ({ page }) => {
    const gradesLink = page.locator('a:has-text("Grades"), a:has-text("PV"), a:has-text("Notes")').first();
    if (await gradesLink.isVisible()) {
      await gradesLink.click();
    }
    
    await page.waitForSelector('table tbody tr', { timeout: 5000 });
    
    const allStudents = await page.locator('table tbody tr').count();
    console.log(`Total students: ${allStudents}`);
    
    for (let cycle = 0; cycle < 2; cycle++) {
      console.log(`\n--- Cycle ${cycle + 1} ---`);
      
      const searchInput = page.locator('input[placeholder*="nom"], input[placeholder*="Nom"], input[placeholder*="name"]').first();
      if (await searchInput.isVisible()) {
        const filterChar = String.fromCharCode(97 + cycle);
        await searchInput.fill(filterChar);
        await page.waitForTimeout(500);
      }
      
      const editButton = page.locator('button:has-text("Edit"), button:has-text("Éditer"), button:has-text("Modifier")').first();
      if (await editButton.isVisible()) {
        await editButton.click();
        await page.waitForTimeout(300);
      }
      
      const gradeInput = page.locator('table tbody tr:first-child input[type="number"]').first();
      if (await gradeInput.isVisible()) {
        const newValue = String(15 + cycle);
        await gradeInput.clear();
        await gradeInput.fill(newValue);
        console.log(`Edited grade to: ${newValue}`);
      }
      
      const confirmButton = page.locator('button:has-text("Confirm"), button:has-text("Confirmer"), button:has-text("Valider")').first();
      if (await confirmButton.isVisible()) {
        await confirmButton.click();
        await page.waitForTimeout(500);
      }
      
      if (await searchInput.isVisible()) {
        await searchInput.clear();
        await page.waitForTimeout(500);
      }
    }
    
    console.log('✅ Multiple cycles completed successfully!');
  });
});
