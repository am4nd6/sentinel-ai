export type HealthResponse = { status: string; service: string; timestamp?: string }
export type PipelineStatus = { status: string; topic: string; retryPolicy: string; deadLetterTopic: string; backpressure: string; processed: number; duplicates: number; failures: number; deadLetters: number }
export type Incident = { id: string; title: string; severity: string; status: string; assignee?: string; updatedAt?: string }
export type IncidentPage = { items: Incident[]; totalItems: number; totalPages: number; page: number }
export type IncidentSummary = { open: number; investigating: number; resolved: number }
export type IncidentTimeline = { id: string; action: string; actor: string; details?: string; createdAt: string }
export type Investigation = { job_id: string; status: string; mode: 'read-only'; summary: string; hypotheses: Array<{ text: string; confidence: string }>; citations: Array<{ evidence_id: string; source: string; excerpt: string }>; limitations: string[] }
export type PlaybookExecution = { id: string; playbookId: string; action: string; impact: string; state: string; dryRun: boolean; idempotencyKey: string; reason: string; result?: string }
export type GraphResponse = { tenantId: string; nodes: Array<{ id: string; type: string; label: string }>; edges: Array<{ id: string; source: string; target: string; relation: string; evidenceSource: string; confidence: string }>; page: number; size: number; totalNodes: number }
const base = (url?: string) => url ?? import.meta.env.VITE_API_URL ?? 'http://localhost:8080'
async function get<T>(url: string): Promise<T> { const r = await fetch(url, { headers: { Accept: 'application/json' } }); if (!r.ok) throw new Error(`Request failed: ${r.status}`); return r.json() as Promise<T> }
export const getBackendHealth = (url?: string) => get<HealthResponse>(`${base(url)}/api/v1/health`)
export const getPipelineStatus = (url?: string) => get<PipelineStatus>(`${base(url)}/api/v1/pipeline/status`)
export async function getIncidents(url?: string, search = '', status = ''): Promise<IncidentPage> { const p = new URLSearchParams({ page: '0', size: '20' }); if (search) p.set('search', search); if (status) p.set('status', status); return get<IncidentPage>(`${base(url)}/api/v1/incidents?${p}`) }
export const getIncidentSummary = (url?: string) => get<IncidentSummary>(`${base(url)}/api/v1/incidents/summary`)
export const getIncidentTimeline = (id: string, url?: string) => get<IncidentTimeline[]>(`${base(url)}/api/v1/incidents/${id}/timeline`)
export async function createInvestigation(input: { tenant_id: string; incident_id: string; roles: string[]; evidence: Array<{ id: string; text: string; source: string }> }, url?: string): Promise<Investigation> { const r = await fetch(`${base(url)}/v1/investigations`, { method: 'POST', headers: { 'Content-Type': 'application/json', Accept: 'application/json' }, body: JSON.stringify(input) }); if (!r.ok) throw new Error(`Investigation failed: ${r.status}`); return r.json() as Promise<Investigation> }
export async function requestPlaybook(playbookId: string, action: string, url?: string): Promise<PlaybookExecution> { const r = await fetch(`${base(url)}/api/v1/playbooks/${playbookId}/executions`, { method: 'POST', headers: { 'Content-Type': 'application/json', Accept: 'application/json' }, body: JSON.stringify({ action, idempotencyKey: `${playbookId}-${action}-demo`, reason: 'Synthetic analyst demonstration', inputJson: '{}' }) }); if (!r.ok) throw new Error(`Playbook failed: ${r.status}`); return r.json() as Promise<PlaybookExecution> }
export const getGraph = (url?: string) => get<GraphResponse>(`${base(url)}/api/v1/graph?page=0&size=50`)
