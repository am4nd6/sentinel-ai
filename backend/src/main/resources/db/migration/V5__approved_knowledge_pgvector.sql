create extension if not exists vector;

create table if not exists approved_knowledge_document (
  id uuid primary key,
  tenant_id varchar(80) not null references tenant(id),
  source varchar(240) not null,
  version varchar(80) not null,
  content_hash varchar(128) not null,
  approved_by varchar(160) not null,
  approved_at timestamptz not null default now(),
  unique (tenant_id, source, version)
);

create table if not exists approved_knowledge_chunk (
  id uuid primary key,
  document_id uuid not null references approved_knowledge_document(id),
  tenant_id varchar(80) not null references tenant(id),
  chunk_index integer not null,
  content text not null,
  embedding vector(1536),
  unique (document_id, chunk_index)
);

create index if not exists idx_knowledge_chunk_tenant on approved_knowledge_chunk(tenant_id, document_id);
