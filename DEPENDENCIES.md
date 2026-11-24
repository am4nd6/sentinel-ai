# SENTINEL AI — Dependências

Este arquivo lista os requisitos para instalar e executar o projeto.

## Ferramentas

| Ferramenta | Versão | Uso |
|---|---:|---|
| Git | 2.55+ | Versionamento |
| Java/JDK | 21 LTS | Backend |
| Maven | 3.9+ | Build Java |
| Node.js | 24 LTS | Frontend |
| npm | 11+ | Pacotes JavaScript |
| Python | 3.13 | Intelligence service |
| Docker | 29+ | Infraestrutura local |
| Docker Compose | v2+ | PostgreSQL, Redis e Keycloak |

Verificar:

```bash
git --version
java -version
mvn -version
node --version
npm --version
python --version
docker --version
docker compose version
```

## Frontend — `frontend/`

Dependências fixadas em `frontend/package-lock.json`:

- React `19.3.1` e React DOM `19.3.1`.
- TypeScript `7.0.2`.
- Vite `8.3.8` e `@vitejs/plugin-react` `6.3.0`.
- TanStack React Query `5.108.4`.
- `oidc-client-ts` `3.5.0`.
- Vitest `5.1.1`.
- Playwright Test/Core `1.65.1`.

Instalar e validar:

```bash
cd frontend
npm ci
npm run build
npm run test
node node_modules/playwright/cli.js install chromium
```

Executar E2E com o frontend iniciado em outro terminal:

```bash
npm run dev -- --host 127.0.0.1 --port 4173
node node_modules/playwright/cli.js test
```

## Backend — `backend/`

Dependências principais em `backend/pom.xml`:

- Java `21`.
- Spring Boot `3.5.6`.
- Spring Web, Actuator e Validation.
- Spring Security.
- OAuth2 Resource Server e JOSE/JWT.
- Spring Security Test.
- JUnit 5 e Mockito via Spring Boot Starter Test.

```bash
cd backend
mvn -B test package
java -jar target/sentinel-backend-0.1.0-SNAPSHOT.jar
```

Endpoints básicos:

- `GET http://localhost:8080/api/v1/health`
- `GET http://localhost:8080/actuator/health`
- `GET http://localhost:8080/v3/api-docs`

## Intelligence service — `intelligence-service/`

Dependências em `pyproject.toml`:

- Python `>=3.13,<3.14`.
- FastAPI `0.141.1`.
- Pydantic `2.13.5`.
- Uvicorn `0.38.0`.
- pytest `8.4.2` e httpx `0.28.1` para testes.

Windows PowerShell:

```powershell
cd intelligence-service
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -e ".[test]"
$env:PYTHONPATH = "src"
python -m pytest
```

Linux/macOS:

```bash
cd intelligence-service
python -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -e ".[test]"
PYTHONPATH=src python -m pytest
```

## Docker Compose — `infra/compose/`

Imagens usadas:

- PostgreSQL `16-alpine`, porta `5432`.
- Redis `7-alpine`, porta `6379`.
- Keycloak `26.3`, porta `8081`, somente para OIDC local.

```bash
docker compose -f infra/compose/docker-compose.yml up -d
docker compose -f infra/compose/docker-compose.yml ps
docker compose -f infra/compose/docker-compose.yml down
```

OIDC local:

- Issuer: `http://localhost:8081/realms/sentinel`.
- Client: `sentinel-frontend`.
- Realm: definido em `infra/keycloak/realm-sentinel.json`.

As credenciais do realm são sintéticas e exclusivas para desenvolvimento local.

## Scan de segredos

O CI usa `trufflesecurity/trufflehog:3.90.8` e não exige licença Gitleaks.

PowerShell:

```powershell
docker run --rm -v "${PWD}:/repo:ro" trufflesecurity/trufflehog:3.90.8 filesystem /repo --no-update --fail --exclude-paths=/repo/.trufflehog-exclude
```

## CI

`.github/workflows/ci.yml` executa `npm ci`, testes/build frontend, `mvn test package`, pytest Python e scan de segredos.

## Variáveis locais

```text
SENTINEL_ENVIRONMENT=local
SENTINEL_FRONTEND_ORIGIN=http://localhost:5173
SENTINEL_JWT_SECRET=valor somente local
SENTINEL_OIDC_ISSUER=http://localhost:8081/realms/sentinel
VITE_API_URL=http://localhost:8080
VITE_OIDC_AUTHORITY=http://localhost:8081/realms/sentinel
VITE_OIDC_CLIENT_ID=sentinel-frontend
```

Nunca commit credenciais reais. Consulte também `.gitignore`, `docs/security-auth.md` e `docs/tech.md`.
