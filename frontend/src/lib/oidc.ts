import { UserManager, WebStorageStateStore, type User } from 'oidc-client-ts'

const authority = import.meta.env.VITE_OIDC_AUTHORITY ?? 'http://localhost:8081/realms/sentinel'

export const oidc = new UserManager({
  authority,
  client_id: import.meta.env.VITE_OIDC_CLIENT_ID ?? 'sentinel-frontend',
  redirect_uri: `${window.location.origin}/auth/callback`,
  post_logout_redirect_uri: window.location.origin,
  response_type: 'code',
  scope: 'openid profile email',
  userStore: new WebStorageStateStore({ store: window.sessionStorage }),
})

export const login = () => oidc.signinRedirect()
export const logout = () => oidc.signoutRedirect()
export const completeLogin = (): Promise<User> => oidc.signinRedirectCallback()
