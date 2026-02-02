import { test, expect, ConsoleMessage } from '@playwright/test';

test.describe('Console Error Detection', () => {
  const consoleErrors: ConsoleMessage[] = [];
  const consoleWarnings: ConsoleMessage[] = [];

  test.beforeEach(async ({ page }) => {
    consoleErrors.length = 0;
    consoleWarnings.length = 0;

    page.on('console', (msg) => {
      if (msg.type() === 'error') {
        consoleErrors.push(msg);
      } else if (msg.type() === 'warning') {
        consoleWarnings.push(msg);
      }
    });

    page.on('pageerror', (error) => {
      console.error('Uncaught page error:', error.message);
    });
  });

  test('login page should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');

    expect(consoleErrors.length, 
      `Console errors found: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);

    const criticalWarnings = consoleWarnings.filter(w => 
      !w.text().includes('DevTools') && 
      !w.text().includes('Download the React DevTools')
    );
    
    expect(criticalWarnings.length,
      `Console warnings found: ${criticalWarnings.map(w => w.text()).join('\n')}`
    ).toBe(0);
  });

  test('admin dashboard should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.fill('input[type="text"]', 'admin');
    await page.fill('input[type="password"]', 'admin');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/admin/dashboard');
    await page.waitForLoadState('networkidle');

    expect(consoleErrors.length,
      `Console errors found: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('teacher dashboard should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.fill('input[type="text"]', 'prof.johnson');
    await page.fill('input[type="password"]', 'teacher123');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/teacher/dashboard');
    await page.waitForLoadState('networkidle');

    expect(consoleErrors.length,
      `Console errors found: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('student dashboard should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.fill('input[type="text"]', 'STU2024001');
    await page.fill('input[type="password"]', 'student123');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/student/dashboard');
    await page.waitForLoadState('networkidle');

    expect(consoleErrors.length,
      `Console errors found: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('navigation between pages should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.fill('input[type="text"]', 'STU2024001');
    await page.fill('input[type="password"]', 'student123');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/student/dashboard');
    await page.waitForLoadState('networkidle');

    const gradesLink = page.locator('text=/grades/i, text=/my grades/i').first();
    if (await gradesLink.count() > 0) {
      await gradesLink.click();
      await page.waitForLoadState('networkidle');
    }

    const profileLink = page.locator('text=/profile/i').first();
    if (await profileLink.count() > 0) {
      await profileLink.click();
      await page.waitForLoadState('networkidle');
    }

    expect(consoleErrors.length,
      `Console errors during navigation: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('failed API requests should not produce unhandled console errors', async ({ page }) => {
    await page.route('**/api/**', route => route.abort('failed'));

    await page.goto('http://localhost:5173');
    await page.waitForTimeout(2000);

    const unhandledErrors = consoleErrors.filter(e => 
      !e.text().includes('Failed to fetch') &&
      !e.text().includes('Network request failed') &&
      !e.text().includes('ERR_FAILED')
    );

    expect(unhandledErrors.length,
      `Unhandled console errors found: ${unhandledErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('form validation should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.click('button[type="submit"]');
    await page.waitForTimeout(500);

    const nonValidationErrors = consoleErrors.filter(e => 
      !e.text().includes('validation') &&
      !e.text().includes('required')
    );

    expect(nonValidationErrors.length,
      `Non-validation console errors: ${nonValidationErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });

  test('logout should not produce console errors', async ({ page }) => {
    await page.goto('http://localhost:5173');
    
    await page.fill('input[type="text"]', 'STU2024001');
    await page.fill('input[type="password"]', 'student123');
    await page.click('button[type="submit"]');
    
    await page.waitForURL('**/student/dashboard');
    await page.waitForLoadState('networkidle');

    consoleErrors.length = 0;

    const logoutButton = page.locator('button:has-text("Logout"), button:has-text("Log out"), [aria-label*="logout" i]').first();
    await logoutButton.click();

    await page.waitForURL('**/login', { timeout: 5000 });

    expect(consoleErrors.length,
      `Console errors during logout: ${consoleErrors.map(e => e.text()).join('\n')}`
    ).toBe(0);
  });
});
