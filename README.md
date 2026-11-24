# SENTINEL AI

Plataforma demonstrativa de segurança, inteligência antifraude e resposta assistida. O projeto transforma eventos operacionais sintéticos em sinais de risco rastreáveis, incidentes investigáveis e ações seguras regidas por políticas.

> **Importante:** este projeto ainda não é um produto de produção. O MVP usa somente dados sintéticos e não executa bloqueios ou ações destrutivas reais.

## Problema que resolve

Operações de segurança e antifraude recebem frequentemente muitos alertas isolados, com pouco contexto e sem uma trilha clara de decisão. Isso aumenta a fadiga dos analistas, torna a investigação manual mais lenta e dificulta entender por que um alerta foi gerado ou qual ação é segura.

O SENTINEL AI organiza eventos heterogêneos em sinais de risco explicáveis, incidentes investigáveis e recomendações rastreáveis. Ele combina regras determinísticas, evidências, investigação assistida por IA e políticas independentes, sem delegar ações sensíveis ao LLM.

## Objetivos

- Reduzir fadiga de alertas por meio de correlação e contexto.
- Produzir sinais explicáveis com regras determinísticas e modelos avaliáveis.
- Organizar incidentes, evidências e auditoria.
- Usar LLMs somente como apoio à investigação e síntese.
- Manter políticas e execução fora do controle do LLM.
- Demonstrar isolamento por tenant, aprovação humana e efeitos simulados.

## Limites do MVP

- Não usa contas, pessoas ou sistemas de terceiros.
- Não realiza bloqueios reais, pagamentos ou ações destrutivas.
- Não usa dados pessoais reais.
- Não permite que o LLM execute comandos, playbooks ou alterações administrativas.


## Arquitetura

```text
Gerador sintético
       │
       ▼
Frontend React ──► API Java/Spring Boot ──► PostgreSQL
                                            │
                                            ├─► Outbox / broker
                                            ├─► Regras e correlação
                                            ├─► Incidentes e evidências
                                            └─► Políticas e auditoria
                                                       │
                                                       ▼
                                      Python/FastAPI read-only
                                                       │
                                                       ▼
                                             LLM/RAG restrito
```

### Responsabilidades

- **Backend Java:** autoridade para autenticação, autorização, tenants, persistência, incidentes, políticas, auditoria e execução simulada.
- **Frontend React:** console operacional, componentes, estados de interface e consultas autorizadas.
- **Intelligence service Python:** inferência, avaliação offline, investigação e RAG restrito; sem autoridade de negócio.
- **PostgreSQL:** fonte de verdade dos dados críticos.
- **Redis:** cache, quotas e coordenação pontual; nunca fonte única de verdade.
- **Kafka/Redpanda:** será introduzido somente na Fase 5.

## Como executar o frontend

Requisitos:

- Node.js compatível com a versão registrada em `docs/tech.md`.
- npm.

```bash
cd frontend
npm install
npm run dev
```

Para validar a entrega:

```bash
npm run build
npm run test
```

O frontend atual usa dados estáticos e não depende de backend ou banco de dados.

## Estrutura do repositório

```text
sentinel-ai/
├── contexto.md                 # contexto mestre e especificação
├── phases.md                   # plano e status das fases
├── README.md
├── frontend/                   # React + TypeScript + Vite
├── datasets/                   # eventos e schemas sintéticos
├── docs/
│   ├── adr/                    # decisões arquiteturais
│   ├── phases/                 # evidências por fase
│   ├── architecture.md
│   ├── api-contracts.md
│   ├── threat-model.md
│   ├── tech.md
│   └── ui-reference.md
└── .gitignore
```

## Segurança e governança

- Validar tudo no backend; não confiar em tenant, papel ou severidade enviados pelo navegador.
- Aplicar menor privilégio e negar por padrão.
- Tratar logs, eventos e documentos RAG como dados não confiáveis.
- Validar schema, citações, escopo e limites da saída do LLM.
- Usar `Idempotency-Key` para comandos.
- Manter auditoria correlacionada por IDs.
- Falhar de forma segura em timeout, erro de política ou contexto incompleto.

## Dataset sintético

Os exemplos em `datasets/synthetic/` cobrem:

- Login normal.
- Account takeover.
- Credential stuffing.
- Abuso coordenado de cupons.
- Replay e duplicidade.
- Prompt injection em log.
- Isolamento entre tenants.

O schema está em `datasets/schemas/event.schema.json` e o ground truth em `datasets/synthetic/ground-truth.json`.


## Princípios do projeto

1. Segurança e isolamento antes de conveniência.
2. LLM não é detector nem decisor final.
3. Ações sensíveis exigem aprovação humana.
4. Dados sintéticos e reprodutibilidade no MVP.
5. Nenhuma métrica é alegada sem medição reproduzível.
6. Nenhuma funcionalidade é considerada concluída sem código, testes, documentação e evidência real.

## Licença

A licença pública ainda precisa ser definida na Fase 0/ governança do repositório. Não assumir autorização para reutilização comercial ou distribuição até essa decisão ser registrada.
