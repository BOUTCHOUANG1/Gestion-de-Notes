import { test, expect } from '@playwright/test';

test.describe('Dashboard Navigation', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/auth');
    await page.waitForLoadState('networkidle');
    
    await page.fill('input[name="username"]', 'admin');
    await page.fill('input[name="password"]', 'admin123');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/dashboard', { timeout: 10000 });
  });

  test('should display dashboard after successful login', async ({ page }) => {
    await expect(page).toHaveURL(/\/dashboard/);
    await expect(page.locator('body')).toBeVisible();
  });

  test('should display user profile information', async ({ page }) => {
    const userProfile = page.locator('text=/admin/i, [data-testid="user-profile"]').first();
    await expect(userProfile).toBeVisible({ timeout: 5000 });
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
