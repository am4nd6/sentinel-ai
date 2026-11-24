export type HealthResponse = { status: string; service: string; timestamp?: string }
export type PipelineStatus = { status: string; topic: string; retryPolicy: string; deadLetterTopic: string; backpressure: string; processed: number; duplicates: number; failures: number; deadLetters: number }

export async function getBackendHealth(baseUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'): Promise<HealthResponse> {
  const response = await fetch(`${baseUrl}/api/v1/health`, { headers: { Accept: 'application/json' } })
  if (!response.ok) throw new Error(`Backend health failed: ${response.status}`)
  return response.json() as Promise<HealthResponse>
}

export async function getPipelineStatus(baseUrl = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'): Promise<PipelineStatus> {
  const response = await fetch(`${baseUrl}/api/v1/pipeline/status`, { headers: { Accept: 'application/json' } })
  if (!response.ok) throw new Error(`Pipeline status failed: ${response.status}`)
  return response.json() as Promise<PipelineStatus>
}
