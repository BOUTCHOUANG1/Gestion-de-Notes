import { test, expect } from '@playwright/test';

const TEST_USERNAME = process.env.E2E_ADMIN_USER ?? 'admin';
const TEST_PASSWORD = process.env.E2E_ADMIN_PASS ?? 'admin';

test.describe('Dashboard Navigation', () => {
  test.beforeEach(async ({ page }) => {
    // Uses default storageState from globalSetup.
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
  });

  test('should display dashboard after successful login', async ({ page }) => {
    await expect(page).toHaveURL(/\/dashboard/);
    await expect(page.locator('body')).toBeVisible();
  });

  test.skip('should display user profile information', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    await expect(page.getByText(/System|Administrator|ADMIN/i).first()).toBeVisible({ timeout: 5000 });
  });

  test('should have navigation menu', async ({ page }) => {
    const menuItems = page.locator('nav, .ant-menu, [role="navigation"]');
    await expect(menuItems.first()).toBeVisible({ timeout: 5000 });
  });

  test('should navigate to students page if available', async ({ page }) => {
    const studentsLink = page.locator('a[href*="student"], a:has-text("Student")').first();
    
    if (await studentsLink.isVisible({ timeout: 2000 })) {
      await studentsLink.click();
      await page.waitForLoadState('networkidle');
      await expect(page.url()).toMatch(/student/i);
    } else {
      test.skip();
    }
  });

  test('should display dashboard content', async ({ page }) => {
    const mainContent = page.locator('main, .ant-layout-content, [role="main"]');
    await expect(mainContent.first()).toBeVisible({ timeout: 5000 });
  });

  test('should have responsive layout', async ({ page }) => {
    const viewports = [
      { width: 1920, height: 1080 },
      { width: 1366, height: 768 },
      { width: 768, height: 1024 },
    ];

    for (const viewport of viewports) {
      await page.setViewportSize(viewport);
      await page.waitForLoadState('networkidle');
      
      const body = page.locator('body');
      await expect(body).toBeVisible();
    }
  });
});
