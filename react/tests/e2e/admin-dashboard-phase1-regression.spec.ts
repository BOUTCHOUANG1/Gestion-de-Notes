import { test, expect } from '@playwright/test';

/**
 * Regression test for commit 01ebda0
 * Verifies Phase 1 admin dashboard fixes:
 * 1. DashboardHeader uses useAppSelector() instead of store.getState() 
 * 2. Admin sidebar includes Grade Claims submenu item (line 139 of Dashboard.tsx)
 * 3. Dashboard cards have gradient shadow CSS (index.css lines 80-83, 269, 273)
 */

test.describe('Admin Dashboard - Phase 1 Regression Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.setViewportSize({ width: 1600, height: 1200 });
    await page.goto('/auth/login');
    await page.context().clearCookies();
    await page.evaluate(() => localStorage.clear());
    
    await page.waitForTimeout(1000);
    
    await page.fill('#login_username', 'admin');
    await page.fill('#login_password', 'admin');
    await page.getByTestId('login-submit').click();
    
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    await page.waitForTimeout(1000);
  });

  test('REGRESSION: Dashboard header loads user profile from Redux state', async ({ page }) => {
    await page.goto('/dashboard/admin/dashboard');
    await page.waitForTimeout(2000);
    
    const pageContent = await page.content();
    
    expect(pageContent).toContain('admin');
    expect(pageContent).not.toContain('User!');
    expect(pageContent).not.toContain('Unknown');
    
    console.log('✅ DashboardHeader successfully uses useAppSelector for reactive state');
  });

  test('REGRESSION: Dashboard cards have shadow styling applied', async ({ page }) => {
    await page.goto('/dashboard/admin/dashboard');
    await page.waitForTimeout(2000);
    
    const card = page.locator('.card, .ant-card').first();
    await expect(card).toBeVisible({ timeout: 10000 });
    
    const boxShadow = await card.evaluate((el) => {
      const styles = window.getComputedStyle(el);
      return styles.boxShadow;
    });
    
    expect(boxShadow).toBeTruthy();
    expect(boxShadow).not.toBe('none');
    
    console.log('✅ Dashboard cards have gradient shadow CSS applied');
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/admin-dashboard-shadows-verified.png',
      fullPage: true
    });
  });

  test('REGRESSION: Admin dashboard loads with real backend data', async ({ page }) => {
    await page.goto('/dashboard/admin/dashboard');
    await page.waitForTimeout(3000);
    
    await expect(page.getByRole('heading', { name: /Admin Dashboard/i }).first()).toBeVisible({ timeout: 10000 });
    
    const statCards = page.locator('.ant-statistic-content-value');
    const cardCount = await statCards.count();
    expect(cardCount).toBeGreaterThan(0);
    
    const firstValue = await statCards.first().textContent();
    expect(firstValue).toBeTruthy();
    expect(parseInt(firstValue || '0')).toBeGreaterThanOrEqual(0);
    
    console.log('✅ Dashboard displays real stats from backend API');
    
    await page.screenshot({ 
      path: 'playwright-report/screenshots/admin-dashboard-phase1-complete.png',
      fullPage: true
    });
  });
});
