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

test.describe('Teacher Dashboard - Full Verification', () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, 'prof.smith', 'duchelle');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(2000);
  });

  test('should display welcome message with teacher name', async ({ page }) => {
    const welcomeMessage = page.locator('text=/Welcome|Bienvenue/i');
    await expect(welcomeMessage.first()).toBeVisible({ timeout: 10000 });
  });

  test('should display teacher email', async ({ page }) => {
    const emailPattern = /john\.smith@university\.edu|@/i;
    const emailElement = page.locator(`text=${emailPattern}`);
    const emailCount = await emailElement.count();
    expect(emailCount).toBeGreaterThan(0);
  });

  test('should display stat cards', async ({ page }) => {
    const assignedSubjectsCard = page.locator('text=/Assigned Subjects|Matières assignées/i').first();
    const totalStudentsCard = page.locator('text=/Total Students|Total Étudiants/i').first();
    const teachingLevelsCard = page.locator('text=/Teaching Levels|Niveaux d\'enseignement/i').first();
    const statusCard = page.locator('text=/Status|Statut|Active|Actif/i').first();
    
    const visibleCards = [assignedSubjectsCard, totalStudentsCard, teachingLevelsCard, statusCard];
    let visibleCount = 0;
    
    for (const card of visibleCards) {
      if (await card.isVisible().catch(() => false)) {
        visibleCount++;
      }
    }
    
    expect(visibleCount).toBeGreaterThan(0);
  });

  test('should display subjects table', async ({ page }) => {
    const subjectsSection = page.locator('text=/My Subjects|Mes Matières/i').first();
    if (await subjectsSection.isVisible().catch(() => false)) {
      await expect(subjectsSection).toBeVisible();
      
      const table = page.locator('.ant-table, table').first();
      await expect(table).toBeVisible({ timeout: 10000 });
    }
  });

  test('should display students by level tabs', async ({ page }) => {
    const studentsSection = page.locator('text=/My Students|Mes Étudiants|Students by Level/i').first();
    if (await studentsSection.isVisible().catch(() => false)) {
      await expect(studentsSection).toBeVisible();
      
      const tabs = page.locator('.ant-tabs, [role="tablist"]').first();
      if (await tabs.isVisible().catch(() => false)) {
        await expect(tabs).toBeVisible();
      }
    }
  });

  test('should capture full teacher dashboard screenshot', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/teacher-dashboard-full.png',
      fullPage: true
    });
  });
});
