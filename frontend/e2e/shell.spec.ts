import { test, expect } from '@playwright/test'

test('console renders the operational shell', async ({ page }) => {
  await page.goto('/')
  await expect(page.getByText('SENTINEL', { exact: true })).toBeVisible()
  await expect(page.getByText('Volume de eventos')).toBeVisible()
})
