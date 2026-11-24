create table if not exists incident (
  id uuid primary key, tenant_id varchar(80) not null references tenant(id),
  title varchar(240) not null, severity varchar(30) not null, status varchar(30) not null,
  assignee varchar(160), created_at timestamptz not null default now(), updated_at timestamptz not null default now()
);
create table if not exists incident_evidence (
  id uuid primary key, incident_id uuid not null references incident(id), tenant_id varchar(80) not null references tenant(id),
  event_ref varchar(160) not null, content_hash varchar(128) not null, created_at timestamptz not null default now(), unique(incident_id,event_ref)
);
create table if not exists incident_timeline (
  id uuid primary key, incident_id uuid not null references incident(id), tenant_id varchar(80) not null references tenant(id),
  action varchar(80) not null, actor varchar(160) not null, details varchar(500), created_at timestamptz not null default now()
);
create index if not exists idx_incident_tenant_status on incident(tenant_id,status,updated_at);
