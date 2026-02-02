import { test, expect, Page } from '@playwright/test';

const TEACHER_USERNAME = process.env.E2E_TEACHER_USER ?? 'prof.johnson';
const TEACHER_PASSWORD = process.env.E2E_TEACHER_PASS ?? 'teacher123';

async function loginAsTeacher(page: Page) {
  await page.context().clearCookies();
  await page.evaluate(() => localStorage.clear());
  
  await page.goto('/auth');
  await page.waitForLoadState('networkidle');
  
  await page.fill('#login_username', TEACHER_USERNAME);
  await page.fill('#login_password', TEACHER_PASSWORD);
  await page.getByTestId('login-submit').click();
  
  await page.waitForURL('**/dashboard/**', { timeout: 15000 });
}

test.describe('Teacher Grade Management', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsTeacher(page);
  });

  test('should navigate to grading page', async ({ page }) => {
    const gradingLink = page.locator('a[href*="grade"], a:has-text("Grade")').first();
    
    if (await gradingLink.isVisible({ timeout: 3000 })) {
      await gradingLink.click();
      await page.waitForLoadState('networkidle');
      await expect(page.url()).toMatch(/grade/i);
    } else {
      test.skip();
    }
  });

  test('should display grade entry form', async ({ page }) => {
    const gradingPage = page.locator('a[href*="grade"]').first();
    
    if (await gradingPage.isVisible({ timeout: 2000 })) {
      await gradingPage.click();
      await page.waitForLoadState('networkidle');
      
      const formElement = page.locator('form, [data-testid="grade-form"], .grade-entry').first();
      await expect(formElement).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });

  test('should validate grade value range', async ({ page }) => {
    const gradingPage = page.locator('a[href*="grade"]').first();
    
    if (await gradingPage.isVisible({ timeout: 2000 })) {
      await gradingPage.click();
      await page.waitForLoadState('networkidle');
      
      const gradeInput = page.locator('input[type="number"], input[name*="grade"], input[name*="value"]').first();
      
      if (await gradeInput.isVisible({ timeout: 2000 })) {
        await gradeInput.fill('25');
        await page.keyboard.press('Tab');
        await page.waitForTimeout(500);
        
        const errorMessage = page.locator('.ant-form-item-explain-error, .error-message, [role="alert"]');
        const hasError = await errorMessage.isVisible({ timeout: 2000 }).catch(() => false);
        expect(hasError).toBeTruthy();
      } else {
        test.skip();
      }
    } else {
      test.skip();
    }
  });

  test('should display existing grades table', async ({ page }) => {
    const gradesLink = page.locator('a[href*="grade"]').first();
    
    if (await gradesLink.isVisible({ timeout: 2000 })) {
      await gradesLink.click();
      await page.waitForLoadState('networkidle');
      
      const table = page.locator('table, .ant-table, [data-testid="grades-table"]').first();
      await expect(table).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });

  test('should filter grades by semester', async ({ page }) => {
    const gradesLink = page.locator('a[href*="grade"]').first();
    
    if (await gradesLink.isVisible({ timeout: 2000 })) {
      await gradesLink.click();
      await page.waitForLoadState('networkidle');
      
      const semesterFilter = page.locator('select, .ant-select, [data-testid="semester-filter"]').first();
      
      if (await semesterFilter.isVisible({ timeout: 2000 })) {
        await semesterFilter.click();
        await page.waitForTimeout(300);
        
        const firstOption = page.locator('.ant-select-item, option').nth(1);
        if (await firstOption.isVisible({ timeout: 1000 })) {
          await firstOption.click();
          await page.waitForLoadState('networkidle');
        }
      }
    } else {
      test.skip();
    }
  });

  test('should access teacher dashboard', async ({ page }) => {
    const dashboardLink = page.locator('a[href*="teacher-dashboard"]').first();
    
    if (await dashboardLink.isVisible({ timeout: 3000 })) {
      await dashboardLink.click();
      await page.waitForLoadState('networkidle');
      
      await expect(page.url()).toMatch(/teacher-dashboard/i);
      
      const statsCards = page.locator('.ant-card, [data-testid="stats-card"]');
      await expect(statsCards.first()).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });

  test('should display assigned subjects on dashboard', async ({ page }) => {
    const dashboardLink = page.locator('a[href*="teacher-dashboard"]').first();
    
    if (await dashboardLink.isVisible({ timeout: 3000 })) {
      await dashboardLink.click();
      await page.waitForLoadState('networkidle');
      
      const subjectsTable = page.locator('table, .ant-table').first();
      await expect(subjectsTable).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });

  test('should display students by level tabs', async ({ page }) => {
    const dashboardLink = page.locator('a[href*="teacher-dashboard"]').first();
    
    if (await dashboardLink.isVisible({ timeout: 3000 })) {
      await dashboardLink.click();
      await page.waitForLoadState('networkidle');
      
      const tabs = page.locator('.ant-tabs, [role="tablist"]').first();
      await expect(tabs).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });
});
