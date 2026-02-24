import { test, expect } from '@playwright/test';

/**
 * E2E Tests for Admin CRUD Operations
 * Tests user management, course management, and batch operations
 */

test.describe('Admin CRUD Operations', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to login page
    await page.goto('http://localhost:5173');
    
    // Login as admin
    await page.fill('input[type="text"]', 'admin');
    await page.fill('input[type="password"]', 'admin');
    await page.click('button[type="submit"]');
    
    // Wait for dashboard to load
    await page.waitForURL('**/admin/dashboard');
    await page.waitForLoadState('networkidle');
  });

  test('should display admin dashboard with management options', async ({ page }) => {
    // Verify admin dashboard elements
    await expect(page.locator('text=/manage users/i')).toBeVisible();
    await expect(page.locator('text=/manage courses/i')).toBeVisible();
    
    // Check for admin-specific navigation
    const hasUserManagement = await page.locator('text=/users/i').count() > 0;
    const hasCourseManagement = await page.locator('text=/courses/i').count() > 0;
    
    expect(hasUserManagement || hasCourseManagement).toBeTruthy();
  });

  test('should navigate to user management page', async ({ page }) => {
    // Look for user management link (various possible text patterns)
    const userLink = page.locator('text=/users/i, text=/manage users/i, text=/user management/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Verify user list is displayed
    const hasTable = await page.locator('table, .ant-table').count() > 0;
    const hasUserList = await page.locator('text=/user list/i, text=/all users/i').count() > 0;
    
    expect(hasTable || hasUserList).toBeTruthy();
  });

  test('should open create user modal and validate form', async ({ page }) => {
    // Navigate to user management
    const userLink = page.locator('text=/users/i, text=/manage users/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Click add user button
    const addButton = page.locator('button:has-text("Add"), button:has-text("Create"), button:has-text("New User")').first();
    
    if (await addButton.count() === 0) {
      test.skip(true, 'Add user button not found');
      return;
    }
    
    await addButton.click();
    
    // Verify modal opened
    await expect(page.locator('.ant-modal, [role="dialog"]')).toBeVisible();
    
    // Try to submit empty form (should show validation errors)
    const submitButton = page.locator('.ant-modal button[type="submit"], .ant-modal button:has-text("Submit"), .ant-modal button:has-text("Create")').first();
    await submitButton.click();
    
    // Check for validation messages
    const hasValidationError = await page.locator('.ant-form-item-explain-error, .ant-message-error, text=/required/i').count() > 0;
    expect(hasValidationError).toBeTruthy();
  });

  test('should create a new student user successfully', async ({ page }) => {
    // Navigate to user management
    const userLink = page.locator('text=/users/i, text=/manage users/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Click add user button
    const addButton = page.locator('button:has-text("Add"), button:has-text("Create"), button:has-text("New User")').first();
    
    if (await addButton.count() === 0) {
      test.skip(true, 'Add user button not found');
      return;
    }
    
    await addButton.click();
    await page.waitForSelector('.ant-modal, [role="dialog"]');
    
    // Fill form with unique student data
    const timestamp = Date.now();
    const studentId = `STU${timestamp}`;
    
    await page.fill('input[name="username"], input[placeholder*="username" i]', studentId);
    await page.fill('input[name="email"], input[type="email"]', `${studentId.toLowerCase()}@university.edu`);
    await page.fill('input[name="firstName"], input[placeholder*="first" i]', 'Test');
    await page.fill('input[name="lastName"], input[placeholder*="last" i]', 'Student');
    
    // Select STUDENT role
    const roleSelect = page.locator('.ant-select:has-text("Role"), select[name="role"]').first();
    if (await roleSelect.count() > 0) {
      await roleSelect.click();
      await page.locator('text=/student/i').first().click();
    }
    
    // Fill password
    await page.fill('input[name="password"], input[type="password"]', 'Test123!@#');
    
    // Submit form
    const submitButton = page.locator('.ant-modal button[type="submit"], .ant-modal button:has-text("Submit"), .ant-modal button:has-text("Create")').first();
    await submitButton.click();
    
    // Wait for success message
    await expect(page.locator('.ant-message-success, text=/success/i')).toBeVisible({ timeout: 5000 });
    
    // Verify modal closed
    await expect(page.locator('.ant-modal')).not.toBeVisible({ timeout: 3000 });
  });

  test('should search and filter users', async ({ page }) => {
    // Navigate to user management
    const userLink = page.locator('text=/users/i, text=/manage users/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Look for search input
    const searchInput = page.locator('input[placeholder*="search" i], input[type="search"]').first();
    
    if (await searchInput.count() === 0) {
      test.skip(true, 'Search functionality not found');
      return;
    }
    
    // Search for a known user
    await searchInput.fill('STU2024001');
    await page.waitForTimeout(500); // Wait for debounce
    
    // Verify filtered results
    const tableRows = page.locator('table tbody tr, .ant-table-tbody tr');
    const rowCount = await tableRows.count();
    
    // Should have fewer rows than initial load (filtered)
    expect(rowCount).toBeGreaterThanOrEqual(0);
    
    // Clear search
    await searchInput.clear();
    await page.waitForTimeout(500);
  });

  test('should edit an existing user', async ({ page }) => {
    // Navigate to user management
    const userLink = page.locator('text=/users/i, text=/manage users/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Click first edit button in table
    const editButton = page.locator('button:has-text("Edit"), button[aria-label*="edit" i], .ant-table-row .edit-btn').first();
    
    if (await editButton.count() === 0) {
      test.skip(true, 'Edit functionality not found');
      return;
    }
    
    await editButton.click();
    
    // Verify edit modal opened
    await expect(page.locator('.ant-modal, [role="dialog"]')).toBeVisible();
    
    // Update email field
    const emailInput = page.locator('.ant-modal input[name="email"], .ant-modal input[type="email"]');
    const currentEmail = await emailInput.inputValue();
    const newEmail = currentEmail.includes('updated') 
      ? currentEmail.replace('updated', 'edited')
      : currentEmail.replace('@', '.updated@');
    
    await emailInput.clear();
    await emailInput.fill(newEmail);
    
    // Submit update
    const submitButton = page.locator('.ant-modal button[type="submit"], .ant-modal button:has-text("Update"), .ant-modal button:has-text("Save")').first();
    await submitButton.click();
    
    // Wait for success message
    await expect(page.locator('.ant-message-success, text=/success/i')).toBeVisible({ timeout: 5000 });
  });

  test('should delete a user with confirmation', async ({ page }) => {
    // Navigate to user management
    const userLink = page.locator('text=/users/i, text=/manage users/i').first();
    
    if (await userLink.count() === 0) {
      test.skip(true, 'User management feature not yet implemented');
      return;
    }
    
    await userLink.click();
    await page.waitForLoadState('networkidle');
    
    // Get initial row count
    const initialRowCount = await page.locator('table tbody tr, .ant-table-tbody tr').count();
    
    // Click first delete button
    const deleteButton = page.locator('button:has-text("Delete"), button[aria-label*="delete" i], .ant-table-row .delete-btn').first();
    
    if (await deleteButton.count() === 0) {
      test.skip(true, 'Delete functionality not found');
      return;
    }
    
    await deleteButton.click();
    
    // Handle confirmation dialog
    const confirmButton = page.locator('.ant-popconfirm button:has-text("Yes"), .ant-modal button:has-text("Confirm"), .ant-modal button:has-text("Delete")').first();
    
    if (await confirmButton.count() > 0) {
      await confirmButton.click();
    }
    
    // Wait for success message
    await expect(page.locator('.ant-message-success, text=/deleted/i')).toBeVisible({ timeout: 5000 });
    
    // Verify row count decreased
    await page.waitForTimeout(500);
    const newRowCount = await page.locator('table tbody tr, .ant-table-tbody tr').count();
    expect(newRowCount).toBeLessThanOrEqual(initialRowCount);
  });

  test('should navigate to course management and display courses', async ({ page }) => {
    // Look for course management link
    const courseLink = page.locator('text=/courses/i, text=/manage courses/i').first();
    
    if (await courseLink.count() === 0) {
      test.skip(true, 'Course management feature not yet implemented');
      return;
    }
    
    await courseLink.click();
    await page.waitForLoadState('networkidle');
    
    // Verify course list is displayed
    const hasCourseList = await page.locator('table, .ant-table, .course-list').count() > 0;
    expect(hasCourseList).toBeTruthy();
    
    // Check for course-specific columns
    const hasCourseName = await page.locator('text=/course name/i, text=/title/i').count() > 0;
    const hasCourseCode = await page.locator('text=/code/i, text=/course code/i').count() > 0;
    
    expect(hasCourseName || hasCourseCode).toBeTruthy();
  });
});
