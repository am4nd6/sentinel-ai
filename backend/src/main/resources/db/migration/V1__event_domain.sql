create table if not exists tenant (
  id varchar(80) primary key,
  name varchar(160) not null,
  status varchar(30) not null,
  created_at timestamptz not null default now()
);
create table if not exists actor (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  subject_id varchar(200) not null,
  created_at timestamptz not null default now(),
  unique (tenant_id, subject_id)
);
create table if not exists device (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  device_ref varchar(120) not null,
  created_at timestamptz not null default now(),
  unique (tenant_id, device_ref)
);
create table if not exists security_session (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  session_ref varchar(120) not null,
  started_at timestamptz not null,
  unique (tenant_id, session_ref)
);
create table if not exists ingestion_batch (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  received_at timestamptz not null default now(),
  event_count integer not null default 0
);
create table if not exists security_event (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  event_ref varchar(120) not null,
  event_type varchar(120) not null,
  schema_version varchar(30) not null,
  occurred_at timestamptz not null,
  received_at timestamptz not null default now(),
  account_ref varchar(120),
  device_ref varchar(120),
  source_ref varchar(120),
  payload jsonb not null,
  dedupe_key varchar(200) not null,
  classification varchar(30) not null default 'synthetic',
  unique (tenant_id, dedupe_key)
);
create index if not exists idx_security_event_tenant_time on security_event(tenant_id, occurred_at);
create index if not exists idx_security_event_tenant_type on security_event(tenant_id, event_type);
create index if not exists idx_security_event_correlation on security_event(tenant_id, account_ref, device_ref);
