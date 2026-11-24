export type AuthErrorCode = 'UNAUTHENTICATED' | 'FORBIDDEN'

export class AuthError extends Error {
  constructor(public readonly code: AuthErrorCode, message: string) {
    super(message)
  }
}

/** Fetch wrapper: tokens are supplied by the approved OIDC adapter, never inferred from UI state. */
export async function authorizedFetch(input: RequestInfo | URL, init: RequestInit = {}): Promise<Response> {
  const response = await fetch(input, { ...init, credentials: 'same-origin' })
  if (response.status === 401) throw new AuthError('UNAUTHENTICATED', 'Sessão expirada ou inexistente.')
  if (response.status === 403) throw new AuthError('FORBIDDEN', 'Você não possui permissão para esta operação.')
  return response
}

export function logout(): void {
  window.location.assign('/logout')
}
