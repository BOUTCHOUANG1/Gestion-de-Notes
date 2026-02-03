import { test, expect } from '@playwright/test';

const TEST_USERNAME = process.env.E2E_ADMIN_USER ?? 'admin';
const TEST_PASSWORD = process.env.E2E_ADMIN_PASS ?? 'admin';

test.describe('Authentication Flow', () => {
  test('should login successfully with valid credentials', async ({ page }) => {
    await page.goto('/auth/login');
    await page.waitForLoadState('networkidle');
    await page.context().clearCookies();
    await page.evaluate(() => localStorage.clear());
    
    await page.fill('#login_username', TEST_USERNAME);
    await page.fill('#login_password', TEST_PASSWORD);
    await page.getByTestId('login-submit').click();
    
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('should show error with invalid credentials', async ({ page }) => {
    await page.goto('/auth/login');
    await page.waitForLoadState('networkidle');
    await page.context().clearCookies();
    await page.evaluate(() => localStorage.clear());
    
    await page.fill('#login_username', 'invaliduser');
    await page.fill('#login_password', 'wrongpassword');
    
    const responsePromise = page.waitForResponse(
      response => response.url().includes('/api/auth/login')
    );
    
    await page.getByTestId('login-submit').click();
    const response = await responsePromise;
    
    await page.waitForTimeout(2000);
    await expect(page).toHaveURL(/\/auth/);
  });

  test('should redirect unauthenticated users to login', async ({ browser }) => {
    const context = await browser.newContext({ storageState: undefined });
    const page = await context.newPage();
    
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    
    await expect(page).toHaveURL(/\/auth/);
    
    await context.close();
  });

  test('should logout successfully', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    
    const logoutLink = page.getByRole('link', { name: /sign out/i });
    await logoutLink.click();
    
    await page.waitForLoadState('networkidle');
    await expect(page).toHaveURL(/\/(auth|$)/);
    
    const token = await page.evaluate(() => localStorage.getItem('token'));
    expect(token).toBeNull();
  });

  test('should persist authentication across page reloads', async ({ page }) => {
    // Uses default storageState from globalSetup.
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    await page.waitForURL('**/dashboard/**', { timeout: 15000 });
    
    await page.reload();
    await page.waitForLoadState('networkidle');
    
    await expect(page).toHaveURL(/\/dashboard/);
  });
});
