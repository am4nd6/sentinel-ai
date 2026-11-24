create table if not exists event_outbox (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  aggregate_id uuid not null,
  topic varchar(160) not null,
  schema_version varchar(30) not null,
  payload jsonb not null,
  status varchar(30) not null default 'PENDING',
  attempts integer not null default 0,
  available_at timestamptz not null default now(),
  last_error varchar(500),
  created_at timestamptz not null default now(),
  unique (tenant_id, aggregate_id, topic)
);
create table if not exists event_consumer_receipt (
  consumer_name varchar(120) not null,
  event_key varchar(200) not null,
  processed_at timestamptz not null default now(),
  primary key (consumer_name, event_key)
);
create index if not exists idx_outbox_pending on event_outbox(status, available_at);
