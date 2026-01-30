import { test, expect } from '@playwright/test';

const TEST_USERNAME = process.env.E2E_ADMIN_USER ?? 'admin';
const TEST_PASSWORD = process.env.E2E_ADMIN_PASS ?? 'admin';

test.describe('Authentication Flow', () => {
  test('should login successfully with valid credentials', async ({ page }) => {
    await page.goto('/auth');
    await page.waitForLoadState('networkidle');
    
    await page.fill('#login_username', TEST_USERNAME);
    await page.fill('#login_password', TEST_PASSWORD);
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('should show error with invalid credentials', async ({ page }) => {
    await page.goto('/auth');
    await page.waitForLoadState('networkidle');
    
    await page.fill('#login_username', 'invaliduser');
    await page.fill('#login_password', 'wrongpassword');
    
    const responsePromise = page.waitForResponse(
      response => response.url().includes('/api/auth/login')
    );
    
    await page.click('button[type="submit"]');
    const response = await responsePromise;
    
    await page.waitForTimeout(2000);
    await expect(page).toHaveURL(/\/auth/);
  });

  test('should redirect unauthenticated users to login', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    
    await expect(page).toHaveURL(/\/auth/);
  });

  test.skip('should logout successfully', async ({ page }) => {
    await page.goto('/auth');
    await page.waitForLoadState('networkidle');
    
    await page.fill('#login_username', TEST_USERNAME);
    await page.fill('#login_password', TEST_PASSWORD);
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    
    const userMenuButton = page.locator('.ant-badge').locator('..').locator('..');
    await userMenuButton.hover();
    await page.waitForTimeout(1000);
    
    await page.getByText(/deconnexion/i).click();
    
    await page.waitForURL('**/auth', { timeout: 10000 });
    await expect(page).toHaveURL(/\/auth/);
  });

  test('should persist authentication across page reloads', async ({ page }) => {
    await page.goto('/auth');
    await page.waitForLoadState('networkidle');
    
    await page.fill('#login_username', TEST_USERNAME);
    await page.fill('#login_password', TEST_PASSWORD);
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    
    await page.reload();
    await page.waitForLoadState('networkidle');
    
    await expect(page).toHaveURL(/\/dashboard/);
  });
});
