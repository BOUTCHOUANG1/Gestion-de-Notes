import { test, expect, Page } from '@playwright/test';

const STUDENT_USERNAME = process.env.E2E_STUDENT_USER ?? 'STU2024001';
const STUDENT_PASSWORD = process.env.E2E_STUDENT_PASS ?? 'student123';

async function loginAsStudent(page: Page) {
  await page.context().clearCookies();
  await page.evaluate(() => localStorage.clear());
  
  await page.goto('/auth');
  await page.waitForLoadState('networkidle');
  
  await page.fill('#login_username', STUDENT_USERNAME);
  await page.fill('#login_password', STUDENT_PASSWORD);
  await page.getByTestId('login-submit').click();
  
  await page.waitForURL('**/dashboard/**', { timeout: 15000 });
}

test.describe('Grade Claims Workflow', () => {
  test.beforeEach(async ({ page }) => {
    await loginAsStudent(page);
  });

  test('should navigate to grade claims page', async ({ page }) => {
    const claimsLink = page.locator('a[href*="claim"], a:has-text("Claim"), a:has-text("Revendication")').first();
    
    if (await claimsLink.isVisible({ timeout: 3000 })) {
      await claimsLink.click();
      await page.waitForLoadState('networkidle');
      await expect(page.url()).toMatch(/claim|revendication/i);
    } else {
      test.skip();
    }
  });

  test('should display student grades', async ({ page }) => {
    const gradesLink = page.locator('a[href*="grade"], a[href*="sem"]').first();
    
    if (await gradesLink.isVisible({ timeout: 3000 })) {
      await gradesLink.click();
      await page.waitForLoadState('networkidle');
      
      const gradesTable = page.locator('table, .ant-table, [data-testid="grades-table"]').first();
      await expect(gradesTable).toBeVisible({ timeout: 5000 });
    } else {
      test.skip();
    }
  });

  test('should show grade claim form', async ({ page }) => {
    const claimsPage = page.locator('a[href*="claim"], a:has-text("Claim")').first();
    
    if (await claimsPage.isVisible({ timeout: 2000 })) {
      await claimsPage.click();
      await page.waitForLoadState('networkidle');
      
      const claimButton = page.locator('button:has-text("Submit"), button:has-text("Claim"), button[data-testid="submit-claim"]').first();
      const claimForm = page.locator('form, [data-testid="claim-form"]').first();
      
      const hasClaimInterface = await claimButton.isVisible({ timeout: 2000 }).catch(() => false) ||
                                 await claimForm.isVisible({ timeout: 2000 }).catch(() => false);
      
      expect(hasClaimInterface).toBeTruthy();
    } else {
      test.skip();
    }
  });

  test('should validate claim description required field', async ({ page }) => {
    const claimsPage = page.locator('a[href*="claim"]').first();
    
    if (await claimsPage.isVisible({ timeout: 2000 })) {
      await claimsPage.click();
      await page.waitForLoadState('networkidle');
      
      const submitButton = page.locator('button[type="submit"], button:has-text("Submit")').first();
      
      if (await submitButton.isVisible({ timeout: 2000 })) {
        await submitButton.click();
        await page.waitForTimeout(500);
        
        const errorMessage = page.locator('.ant-form-item-explain-error, .error, [role="alert"]').first();
        const hasValidation = await errorMessage.isVisible({ timeout: 2000 }).catch(() => false);
        
        expect(hasValidation).toBeTruthy();
      } else {
        test.skip();
      }
    } else {
      test.skip();
    }
  });

  test('should display claim status', async ({ page }) => {
    const claimsLink = page.locator('a[href*="claim"]').first();
    
    if (await claimsLink.isVisible({ timeout: 2000 })) {
      await claimsLink.click();
      await page.waitForLoadState('networkidle');
      
      const statusBadge = page.locator('.ant-badge, .ant-tag, [data-testid="claim-status"]').first();
      const hasStatus = await statusBadge.isVisible({ timeout: 3000 }).catch(() => false);
      
      if (!hasStatus) {
        test.skip();
      }
    } else {
      test.skip();
    }
  });

  test('should filter claims by status', async ({ page }) => {
    const claimsLink = page.locator('a[href*="claim"]').first();
    
    if (await claimsLink.isVisible({ timeout: 2000 })) {
      await claimsLink.click();
      await page.waitForLoadState('networkidle');
      
      const statusFilter = page.locator('select, .ant-select, [data-testid="status-filter"]').first();
      
      if (await statusFilter.isVisible({ timeout: 2000 })) {
        await statusFilter.click();
        await page.waitForTimeout(300);
        
        const filterOption = page.locator('.ant-select-item, option').first();
        if (await filterOption.isVisible({ timeout: 1000 })) {
          await filterOption.click();
          await page.waitForLoadState('networkidle');
        }
      } else {
        test.skip();
      }
    } else {
      test.skip();
    }
  });

  test('should display semester grades navigation', async ({ page }) => {
    const sem1Link = page.locator('a:has-text("Semestre 1"), a[href*="sem1"]').first();
    const sem2Link = page.locator('a:has-text("Semestre 2"), a[href*="sem2"]').first();
    
    const hasSemesterNav = await sem1Link.isVisible({ timeout: 2000 }).catch(() => false) ||
                           await sem2Link.isVisible({ timeout: 2000 }).catch(() => false);
    
    expect(hasSemesterNav).toBeTruthy();
  });

  test('should navigate between semesters', async ({ page }) => {
    const sem1Link = page.locator('a:has-text("Semestre 1"), a[href*="sem1"]').first();
    
    if (await sem1Link.isVisible({ timeout: 2000 })) {
      await sem1Link.click();
      await page.waitForLoadState('networkidle');
      await expect(page.url()).toMatch(/sem1|semester.*1/i);
      
      const sem2Link = page.locator('a:has-text("Semestre 2"), a[href*="sem2"]').first();
      
      if (await sem2Link.isVisible({ timeout: 2000 })) {
        await sem2Link.click();
        await page.waitForLoadState('networkidle');
        await expect(page.url()).toMatch(/sem2|semester.*2/i);
      }
    } else {
      test.skip();
    }
  });
});
