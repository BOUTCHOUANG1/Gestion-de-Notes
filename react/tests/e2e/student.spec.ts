import { test, expect } from '@playwright/test';

const STUDENT_USER = process.env.E2E_STUDENT_USER ?? 'STU2024001';
const STUDENT_PASS = process.env.E2E_STUDENT_PASS ?? 'student123';

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

test.describe('Student Flow', () => {
  test('student should reach dashboard after login', async ({ page }) => {
    await loginAs(page, STUDENT_USER, STUDENT_PASS);
    await expect(page).toHaveURL(/\/dashboard/);
  });

  test('student should see semester navigation items', async ({ page }) => {
    await loginAs(page, STUDENT_USER, STUDENT_PASS);
    await expect(page.getByText('Semestre 1', { exact: true })).toBeVisible({ timeout: 10000 });
    await expect(page.getByText('Semestre 2', { exact: true })).toBeVisible({ timeout: 10000 });
  });
});
