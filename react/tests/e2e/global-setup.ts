import fs from 'node:fs';

import { request, type FullConfig } from '@playwright/test';

export default async function globalSetup(_config: FullConfig) {
  const baseURL = process.env.PLAYWRIGHT_BASE_URL || 'http://localhost:5173';
  const apiBase = process.env.VITE_API_BASE_URL || 'http://localhost:3030/api';
  const loginUrl = apiBase.replace(/\/+$/, '') + '/auth/login';

  const username = process.env.E2E_ADMIN_USER ?? 'admin';
  const password = process.env.E2E_ADMIN_PASS ?? 'admin';

  const ctx = await request.newContext();
  const res = await ctx.post(loginUrl, { data: { username, password } });

  if (!res.ok()) {
    const body = await res.text();
    throw new Error(`E2E globalSetup login failed (${res.status()}): ${body}`);
  }

  const data = (await res.json()) as { token?: string };
  if (!data?.token) {
    throw new Error('E2E globalSetup: login response missing token');
  }

  const storageState = {
    origins: [
      {
        origin: baseURL,
        localStorage: [{ name: 'token', value: data.token }],
      },
    ],
  };

  fs.writeFileSync(
    'tests/e2e/.authState.json',
    JSON.stringify(storageState, null, 2),
    'utf-8'
  );
}
