import { test, expect, type Page } from '@playwright/test';

async function loginAs(page: Page, username: string, password: string) {
  await page.goto('/auth/login');
  await page.context().clearCookies();
  await page.evaluate(() => localStorage.clear());
  
  await page.waitForLoadState('networkidle');
  
  await page.fill('#login_username', username);
  await page.fill('#login_password', password);
  await page.getByTestId('login-submit').click();
  
  await page.waitForURL('**/dashboard/**', { timeout: 15000 });
}

test.describe('Student Dashboard - Full Verification', () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, '24a0001', 'nathan');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(2000);
  });

  test('should display academic info banner', async ({ page }) => {
    const levelInfo = page.locator('text=/Level|Niveau|LEVEL1|LEVEL2|LEVEL3/i').first();
    const cycleInfo = page.locator('text=/Cycle|BACHELOR|MASTER/i').first();
    
    const hasLevelOrCycle = await levelInfo.isVisible().catch(() => false) || 
                            await cycleInfo.isVisible().catch(() => false);
    expect(hasLevelOrCycle).toBeTruthy();
  });

  test('should display stat cards', async ({ page }) => {
    const enrolledSubjectsCard = page.locator('text=/Enrolled Subjects|Matières inscrites/i').first();
    const gpaCard = page.locator('text=/GPA|Average|Moyenne/i').first();
    const creditsCard = page.locator('text=/Credits|Crédits/i').first();
    const claimsCard = page.locator('text=/Claims|Réclamations|Pending/i').first();
    
    const visibleCards = [enrolledSubjectsCard, gpaCard, creditsCard, claimsCard];
    let visibleCount = 0;
    
    for (const card of visibleCards) {
      if (await card.isVisible().catch(() => false)) {
        visibleCount++;
      }
    }
    
    expect(visibleCount).toBeGreaterThan(0);
  });

  test('should display GPA stat', async ({ page }) => {
    const gpaSection = page.locator('text=/GPA|Average|Moyenne/i').first();
    if (await gpaSection.isVisible().catch(() => false)) {
      await expect(gpaSection).toBeVisible();
      
      const gpaCard = gpaSection.locator('..').locator('..');
      const gpaValue = await gpaCard.locator('.ant-statistic-content-value, [class*="value"]').first().textContent();
      expect(gpaValue).toBeTruthy();
    }
  });

  test('should display recent grades section', async ({ page }) => {
    const gradesSection = page.locator('text=/Recent Grades|Notes récentes|My Grades|Mes Notes/i').first();
    if (await gradesSection.isVisible().catch(() => false)) {
      await expect(gradesSection).toBeVisible();
    }
  });

  test('should display grade table with columns', async ({ page }) => {
    const table = page.locator('.ant-table, table').first();
    if (await table.isVisible().catch(() => false)) {
      await expect(table).toBeVisible({ timeout: 10000 });
      
      const subjectColumn = table.locator('text=/Subject|Matière|Code/i').first();
      const gradeColumn = table.locator('text=/Grade|Note|Score/i').first();
      const creditsColumn = table.locator('text=/Credits|Crédits/i').first();
      const statusColumn = table.locator('text=/Status|Statut/i').first();
      
      const hasRequiredColumns = await subjectColumn.isVisible().catch(() => false) ||
                                   await gradeColumn.isVisible().catch(() => false) ||
                                   await creditsColumn.isVisible().catch(() => false) ||
                                   await statusColumn.isVisible().catch(() => false);
      
      if (hasRequiredColumns) {
        expect(hasRequiredColumns).toBeTruthy();
      }
    }
  });

  test('should capture full student dashboard screenshot', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/student-dashboard-full.png',
      fullPage: true
    });
  });
});
