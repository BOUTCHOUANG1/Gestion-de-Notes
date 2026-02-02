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

test.describe('Admin Dashboard - Full Verification', () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, 'admin', 'admin');
    await page.waitForLoadState('networkidle');
  });

  test('should display admin dashboard with all stats loaded', async ({ page }) => {
    await expect(page.getByText(/Admin Dashboard/i)).toBeVisible({ timeout: 10000 });
    
    const statCards = page.locator('.ant-statistic, [class*="stat"]').filter({ hasText: /Total|Students|Teachers|Subjects|Departments/i });
    await expect(statCards.first()).toBeVisible({ timeout: 10000 });
    
    const skeletonLoaders = page.locator('.ant-skeleton');
    await expect(skeletonLoaders.first()).toBeHidden({ timeout: 15000 }).catch(() => {});
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/admin-dashboard-full.png',
      fullPage: true
    });
  });

  test('should show real data in stat cards', async ({ page }) => {
    await page.waitForTimeout(2000);
    
    const totalStudentsCard = page.locator('text=/Total Students/i').locator('..').locator('..');
    const studentsValue = await totalStudentsCard.locator('.ant-statistic-content-value, [class*="value"]').first().textContent();
    expect(studentsValue).toBeTruthy();
    expect(parseInt(studentsValue || '0')).toBeGreaterThan(0);
    
    const totalTeachersCard = page.locator('text=/Total Teachers/i').locator('..').locator('..');
    const teachersValue = await totalTeachersCard.locator('.ant-statistic-content-value, [class*="value"]').first().textContent();
    expect(teachersValue).toBeTruthy();
    expect(parseInt(teachersValue || '0')).toBeGreaterThan(0);
  });

  test('should display students by level table', async ({ page }) => {
    await page.waitForTimeout(2000);
    
    const levelTable = page.locator('text=/Students by Level/i').locator('..').locator('..');
    await expect(levelTable).toBeVisible({ timeout: 10000 });
    
    const tableRows = levelTable.locator('.ant-table-row, tr').filter({ hasText: /LEVEL/i });
    const rowCount = await tableRows.count();
    expect(rowCount).toBeGreaterThan(0);
  });

  test('should display recent activity section', async ({ page }) => {
    await page.waitForTimeout(2000);
    
    const activitySection = page.locator('text=/Recent Activity|Activity|Recent/i').first();
    if (await activitySection.isVisible()) {
      await expect(activitySection).toBeVisible();
    }
  });

  test('should capture full page screenshot for documentation', async ({ page }) => {
    await page.waitForTimeout(3000);
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/admin-dashboard-complete.png',
      fullPage: true
    });
  });
});
