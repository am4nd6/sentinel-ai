# Governança de dados — Fase 4

- Os eventos do MVP são sintéticos e classificados como `synthetic`.
- O tenant efetivo vem da credencial autenticada; o campo recebido não é autoridade.
- Payloads devem ser minimizados, validados pelo schema e armazenados sem dados pessoais reais.
- `dedupe_key` é único por tenant para impedir reprocessamento duplicado.
- Retenção inicial local: dados de demonstração podem ser removidos ao destruir o volume do Compose.
- Ambiente compartilhado deverá definir retenção, expurgo, exportação redigida e responsável antes de uso.
- Índices sempre começam por tenant quando a consulta é multi-tenant.
- Datas são armazenadas em UTC.
- O gerador determinístico está em `scripts/seed/generate_events.py`, com seed `20261124`.
