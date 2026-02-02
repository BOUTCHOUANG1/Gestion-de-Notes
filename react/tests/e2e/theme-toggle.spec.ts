import { test, expect, type Page } from '@playwright/test';

test.describe('Theme Toggle', () => {
  test('should toggle between dark and light themes', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Wait for theme toggle button to be visible
    const toggleButton = page.locator('button.theme-toggle');
    await expect(toggleButton).toBeVisible({ timeout: 10000 });
    
    // Get initial theme icon
    const initialIcon = await toggleButton.textContent();
    expect(initialIcon).toMatch(/[☀☾]/); // Sun or moon emoji
    
    // Take screenshot in initial theme
    const initialTheme = initialIcon === '☀' ? 'dark' : 'light';
    await page.screenshot({ 
      path: `playwright-report/screenshots/theme-initial-${initialTheme}.png`,
      fullPage: true
    });
    
    // Toggle theme
    await toggleButton.click();
    await page.waitForTimeout(500); // Wait for theme transition
    
    // Verify icon changed
    const newIcon = await toggleButton.textContent();
    expect(newIcon).not.toBe(initialIcon);
    expect(newIcon).toMatch(/[☀☾]/);
    
    // Take screenshot after toggle
    const newTheme = newIcon === '☀' ? 'dark' : 'light';
    await page.screenshot({ 
      path: `playwright-report/screenshots/theme-toggled-${newTheme}.png`,
      fullPage: true
    });
    
    // Toggle back
    await toggleButton.click();
    await page.waitForTimeout(500);
    
    const finalIcon = await toggleButton.textContent();
    expect(finalIcon).toBe(initialIcon);
  });

  test('should persist theme after page reload', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    const toggleButton = page.locator('button.theme-toggle');
    await expect(toggleButton).toBeVisible({ timeout: 10000 });
    
    const initialIcon = await toggleButton.textContent();
    
    // Toggle theme
    await toggleButton.click();
    await page.waitForTimeout(500);
    
    const afterToggleIcon = await toggleButton.textContent();
    expect(afterToggleIcon).not.toBe(initialIcon);
    
    // Reload page
    await page.reload();
    await page.waitForLoadState('networkidle');
    
    // Verify theme persisted
    await expect(toggleButton).toBeVisible({ timeout: 10000 });
    const afterReloadIcon = await toggleButton.textContent();
    expect(afterReloadIcon).toBe(afterToggleIcon);
  });

  test('should cycle through themes correctly', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    const toggleButton = page.locator('button.theme-toggle');
    await expect(toggleButton).toBeVisible({ timeout: 10000 });
    
    // Record initial state
    const icon1 = await toggleButton.textContent();
    
    // First toggle
    await toggleButton.click();
    await page.waitForTimeout(500);
    const icon2 = await toggleButton.textContent();
    expect(icon2).not.toBe(icon1);
    
    // Second toggle (back to original)
    await toggleButton.click();
    await page.waitForTimeout(500);
    const icon3 = await toggleButton.textContent();
    expect(icon3).toBe(icon1);
    
    // Third toggle (cycle again)
    await toggleButton.click();
    await page.waitForTimeout(500);
    const icon4 = await toggleButton.textContent();
    expect(icon4).toBe(icon2);
  });
});
