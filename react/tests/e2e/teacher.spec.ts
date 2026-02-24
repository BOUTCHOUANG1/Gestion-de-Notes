import { test, expect } from '@playwright/test';

const TEACHER_USER = process.env.E2E_TEACHER_USER ?? 'prof.johnson';
const TEACHER_PASS = process.env.E2E_TEACHER_PASS ?? 'duchelle';

async function loginAs(page: any, username: string, password: string) {
  await page.context().clearCookies();
  await page.evaluate(() => localStorage.clear());

  await page.goto('/auth/login');
  await page.waitForLoadState('networkidle');

  await page.fill('#login_username', username);
  await page.fill('#login_password', password);
  await page.getByTestId('login-submit').click();

  await page.waitForURL('**/dashboard/**', { timeout: 15000 });
}

test.describe('Teacher Flow', () => {
  test('teacher should reach dashboard after login', async ({ page }) => {
    await loginAs(page, TEACHER_USER, TEACHER_PASS);
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('teacher should see teaching navigation items', async ({ page }) => {
    await loginAs(page, TEACHER_USER, TEACHER_PASS);
    // Teacher-only sidebar items from Dashboard.tsx
    await expect(page.getByText('Licence', { exact: true })).toBeVisible({ timeout: 10000 });
  });
});
