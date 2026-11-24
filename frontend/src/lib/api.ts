export type HealthResponse = { status: string; service: string; timestamp?: string }

export async function getBackendHealth(baseUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'): Promise<HealthResponse> {
  const response = await fetch(`${baseUrl}/api/v1/health`, { headers: { Accept: 'application/json' } })
  if (!response.ok) throw new Error(`Backend health failed: ${response.status}`)
  return response.json() as Promise<HealthResponse>
}
