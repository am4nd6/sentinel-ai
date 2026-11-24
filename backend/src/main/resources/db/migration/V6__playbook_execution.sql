create table if not exists playbook_execution (
  id uuid primary key, tenant_id varchar(80) not null references tenant(id), playbook_id varchar(100) not null,
  incident_id uuid, action varchar(80) not null, impact varchar(20) not null, state varchar(40) not null,
  dry_run boolean not null default true, idempotency_key varchar(160) not null, requester varchar(160) not null,
  reason varchar(500) not null, input_json varchar(4000) not null, result varchar(1000),
  created_at timestamptz not null default now(), updated_at timestamptz not null default now(),
  unique(tenant_id, idempotency_key)
);
create table if not exists playbook_approval (
  id uuid primary key, execution_id uuid not null references playbook_execution(id), tenant_id varchar(80) not null references tenant(id),
  reviewer varchar(160) not null, decision varchar(20) not null, reason varchar(500) not null, created_at timestamptz not null default now(),
  unique(execution_id, reviewer)
);
create index if not exists idx_playbook_execution_tenant_state on playbook_execution(tenant_id, state, created_at);
