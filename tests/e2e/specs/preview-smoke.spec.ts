import { test, expect, request as playwrightRequest } from '@playwright/test';

const fixtureBase = process.env.FIXTURE_BASE_URL || 'http://127.0.0.1:18080';

function b64(v: string): string {
  return Buffer.from(v).toString('base64');
}

async function openPreview(request: any, fileUrl: string) {
  const encoded = encodeURIComponent(b64(fileUrl));
  return request.get(`/onlinePreview?url=${encoded}`);
}

test.beforeAll(async () => {
  const api = await playwrightRequest.newContext();
  const required = [
    'sample.txt',
    'sample.md',
    'sample.json',
    'sample.xml',
    'sample.csv',
    'sample.html',
    'sample.png',
    'sample.docx',
    'sample.xlsx',
    'sample.pptx',
    'sample.zip',
    'sample.tar',
    'sample.tgz',
    'sample.7z',
    'sample.rar',
  ];

  try {
    for (const name of required) {
      const resp = await api.get(`${fixtureBase}/${name}`);
      expect(resp.ok(), `fixture missing or unavailable: ${name}`).toBeTruthy();
    }
  } finally {
    await api.dispose();
  }
});

test('01 home/index reachable', async ({ request }) => {
  const resp = await request.get('/');
  expect(resp.status()).toBeLessThan(500);
});

test('02 txt preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.txt`);
  expect(resp.status()).toBe(200);
});

test('03 markdown preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.md`);
  expect(resp.status()).toBe(200);
});

test('04 json preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.json`);
  expect(resp.status()).toBe(200);
});

test('05 xml preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.xml`);
  expect(resp.status()).toBe(200);
});

test('06 csv preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.csv`);
  expect(resp.status()).toBe(200);
});

test('07 html preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.html`);
  expect(resp.status()).toBe(200);
});

test('08 png preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.png`);
  expect(resp.status()).toBe(200);
});

test('09 docx preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.docx`);
  expect(resp.status()).toBe(200);
});

test('10 xlsx preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.xlsx`);
  expect(resp.status()).toBe(200);
});

test('11 pptx preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.pptx`);
  expect(resp.status()).toBe(200);
});

test('12 zip preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.zip`);
  expect(resp.status()).toBe(200);
});

test('13 tar preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.tar`);
  expect(resp.status()).toBe(200);
});

test('14 tgz preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.tgz`);
  expect(resp.status()).toBe(200);
});

test('15 7z preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.7z`);
  expect(resp.status()).toBe(200);
});

test('16 rar preview', async ({ request }) => {
  const resp = await openPreview(request, `${fixtureBase}/sample.rar`);
  expect(resp.status()).toBe(200);
});

test('17 security: block 10.x host in onlinePreview', async ({ request }) => {
  const resp = await openPreview(request, `http://10.1.2.3/a.pdf`);
  const body = await resp.text();
  expect(body).toContain('不受信任');
});

test('18 security: block 10.x host in getCorsFile', async ({ request }) => {
  const encoded = b64('http://10.1.2.3/a.pdf');
  const resp = await request.get(`/getCorsFile?urlPath=${encoded}`);
  const body = await resp.text();
  expect(body).toContain('不受信任');
});
