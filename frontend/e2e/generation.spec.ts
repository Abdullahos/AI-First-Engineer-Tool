import { test, expect } from '@playwright/test';

test.describe('Code Generation E2E Tests', () => {

  test('should successfully generate code using Mock AI and display results', async ({ page }) => {
    await page.goto('/');

    // Fill the form with mock data
    await page.fill('#systemDescription', 'A system to generate code based on user requirements.');
    await page.fill('#functionalRequirements', 'User authentication\\nCRUD operations for products');
    await page.fill('#nonFunctionalRequirements', 'High availability\\nLow latency');
    await page.fill('#dataModel', 'User { id: string, name: string }\\nProduct { id: string, name: string, price: number }');
    await page.fill('#apiDesign', 'POST /users\\nGET /products/{id}');
    await page.fill('#acceptanceCriteria', 'User can register\\nProduct can be added');

    // Ensure "Use Real AI Model" is unchecked for mock AI
    await expect(page.locator('#isRealModel')).not.toBeChecked();

    // Submit the form
    await page.click('button[type="submit"]');

    // Expect a loading indicator or status message
    await expect(page.locator('text=PENDING')).toBeVisible();
    await expect(page.locator('text=RUNNING')).toBeVisible();

    // Wait for the job to complete
    await page.waitForSelector('text=COMPLETE', { timeout: 30000 }); // Increased timeout for polling

    // Verify ResultsDisplay is visible
    await expect(page.locator('.mt-8.bg-white.shadow-md.rounded-lg')).toBeVisible();

    // Verify Backend Code tab content
    await page.click('button:has-text("Backend Code")');
    await expect(page.locator('pre code')).toContainText('package com.app.backend;');
    await expect(page.locator('pre code')).toContainText('public class BackendService {');

    // Verify Frontend Code tab content
    await page.click('button:has-text("Frontend Code")');
    await expect(page.locator('pre code')).toContainText('import React from \'react\';');
    await expect(page.locator('pre code')).toContainText('function FrontendComponent() {');

    // Verify Test Code tab content
    await page.click('button:has-text("Test Code")');
    await expect(page.locator('pre code')).toContainText('import org.junit.jupiter.api.Test;');
    await expect(page.locator('pre code')).toContainText('public class MockBackendServiceTest {');

    // Verify Folder Structure tab content
    await page.click('button:has-text("Folder Structure")');
    await expect(page.locator('pre code')).toContainText('backend/');
    await expect(page.locator('pre code')).toContainText('frontend/');
  });

  test('should display validation errors for invalid input', async ({ page }) => {
    await page.goto('/');

    // Fill systemDescription with too many characters (e.g., 2001 chars)
    const longDescription = 'a'.repeat(2001);
    await page.fill('#systemDescription', longDescription);
    await page.fill('#functionalRequirements', 'Valid FR');
    await page.fill('#nonFunctionalRequirements', 'Valid NFR');
    await page.fill('#dataModel', 'Valid DM');
    await page.fill('#apiDesign', 'Valid API');
    await page.fill('#acceptanceCriteria', 'Valid AC');

    // Submit the form
    await page.click('button[type="submit"]');

    // Expect a validation error message from the backend
    // This assumes the frontend will display the error message from the backend's 400 response
    // The exact selector for the error message might need adjustment based on frontend implementation
    await expect(page.locator('text=HTTP error! status: 400')).toBeVisible();
    await expect(page.locator('text=systemDescription size must be between 0 and 2000')).toBeVisible();
  });

});
