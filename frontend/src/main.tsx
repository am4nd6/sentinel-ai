import { StrictMode, useState, type ReactNode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useQuery } from '@tanstack/react-query'
import { getPipelineStatus } from './lib/api'
import './styles/tokens.css'
import './styles/global.css'

type View = 'overview' | 'incidents' | 'events' | 'investigation' | 'policies'

const nav: Array<{ id: View; label: string; icon: string }> = [
  { id: 'overview', label: 'Overview', icon: '⌂' },
  { id: 'incidents', label: 'Incidentes', icon: '!' },
  { id: 'events', label: 'Eventos', icon: '↯' },
  { id: 'investigation', label: 'Investigação IA', icon: '✦' },
  { id: 'policies', label: 'Políticas', icon: '◈' },
]

function Button({ children, variant = 'primary', onClick }: { children: ReactNode; variant?: 'primary' | 'secondary' | 'ghost'; onClick?: () => void }) {
  return <button className={`button button-${variant}`} onClick={onClick}>{children}</button>
}

function Badge({ children, tone = 'neutral' }: { children: ReactNode; tone?: 'neutral' | 'danger' | 'warning' | 'success' | 'info' }) {
  return <span className={`badge badge-${tone}`}>{children}</span>
}

function Sidebar({ view, setView }: { view: View; setView: (view: View) => void }) {
  return <aside className="sidebar" aria-label="Navegação principal">
    <div className="brand"><div className="brand-mark">S</div><div><strong>SENTINEL</strong><span>AI CONSOLE</span></div></div>
    <p className="nav-label">OPERAÇÕES</p>
    <nav>{nav.map(item => <button key={item.id} className={`nav-item ${view === item.id ? 'active' : ''}`} aria-current={view === item.id ? 'page' : undefined} onClick={() => setView(item.id)}><span className="nav-icon">{item.icon}</span>{item.label}</button>)}</nav>
    <div className="sidebar-bottom"><div className="environment"><span className="status-dot" /> Ambiente demo <Badge tone="info">SINTÉTICO</Badge></div><button className="nav-item"><span className="nav-icon">⚙</span>Configurações</button></div>
  </aside>
}

function Header({ view }: { view: View }) {
  const title = nav.find(item => item.id === view)?.label ?? 'Overview'
  return <header className="topbar"><div><p className="breadcrumb">SENTINEL AI / CONSOLE</p><h1>{title}</h1></div><div className="topbar-actions"><span className="live-status"><span className="status-dot" /> Todos os sistemas operacionais</span><button className="icon-button" aria-label="Notificações">♢</button><div className="avatar" aria-label="Usuário Ana Souza">AS</div></div></header>
}

function StatCard({ label, value, detail, tone = 'neutral' }: { label: string; value: string; detail: string; tone?: string }) {
  return <article className="stat-card"><div className="stat-label">{label}<span className={`stat-signal ${tone}`} /></div><strong>{value}</strong><span className="stat-detail">{detail}</span></article>
}

function Overview() {
  const pipeline = useQuery({ queryKey: ['pipeline-status'], queryFn: () => getPipelineStatus(), refetchInterval: 10_000 })
  return <>
    <section className="page-intro"><div><p className="eyebrow">MONITORAMENTO OPERACIONAL</p><h2>Bom dia, Ana.</h2><p className="muted">Aqui está o resumo do ambiente sintético nas últimas 24 horas.</p></div><Button>＋ Ingerir evento</Button></section>
    <section className="stats-grid" aria-label="Resumo operacional"><StatCard label="Eventos processados" value={pipeline.data ? pipeline.data.processed.toLocaleString('pt-BR') : '—'} detail={pipeline.isError ? 'Pipeline indisponível' : 'Atualização automática'} tone="blue" /><StatCard label="Duplicidades" value={pipeline.data ? pipeline.data.duplicates.toLocaleString('pt-BR') : '—'} detail="Descartadas com idempotência" tone="amber" /><StatCard label="Falhas / DLQ" value={pipeline.data ? `${pipeline.data.failures} / ${pipeline.data.deadLetters}` : '—'} detail="Retry e dead-letter" tone="red" /><StatCard label="Pipeline" value={pipeline.data?.status ?? '—'} detail={pipeline.data?.backpressure ?? 'Carregando'} tone="green" /></section>
    <div className="content-grid"><section className="panel"><div className="panel-heading"><div><p className="eyebrow">ATIVIDADE</p><h3>Volume de eventos</h3></div><select aria-label="Período"><option>Últimas 24 horas</option><option>Últimos 7 dias</option></select></div><div className="chart" role="img" aria-label="Gráfico ilustrativo do volume de eventos"><div className="chart-line" /><div className="chart-axis"><span>00h</span><span>06h</span><span>12h</span><span>18h</span><span>24h</span></div></div></section><section className="panel"><div className="panel-heading"><div><p className="eyebrow">ATENÇÃO</p><h3>Fila de triagem</h3></div><Button variant="ghost">Ver todos →</Button></div><div className="queue"><div><Badge tone="danger">CRÍTICO</Badge><span>Possível account takeover</span><small>há 8 min</small></div><div><Badge tone="warning">ALTO</Badge><span>Abuso de cupom coordenado</span><small>há 21 min</small></div><div><Badge tone="warning">ALTO</Badge><span>Credential stuffing sintético</span><small>há 42 min</small></div></div></section></div>
  </>
}

function Incidents() { return <section className="panel page-panel"><div className="panel-heading"><div><p className="eyebrow">CASOS INVESTIGÁVEIS</p><h2>Incidentes</h2></div><Button>＋ Novo filtro</Button></div><div className="filters"><input placeholder="Buscar por título ou ID" aria-label="Buscar incidentes" /><select aria-label="Filtrar por severidade"><option>Todas as severidades</option><option>Crítico</option><option>Alto</option></select></div><table><thead><tr><th>INCIDENTE</th><th>SEVERIDADE</th><th>STATUS</th><th>RESPONSÁVEL</th><th>ATUALIZADO</th></tr></thead><tbody><tr><td><strong>INC-0024</strong><br /><span>Possível account takeover</span></td><td><Badge tone="danger">CRÍTICO</Badge></td><td><Badge tone="warning">INVESTIGANDO</Badge></td><td>Ana Souza</td><td>8 min</td></tr><tr><td><strong>INC-0023</strong><br /><span>Abuso de cupom coordenado</span></td><td><Badge tone="warning">ALTO</Badge></td><td><Badge tone="info">TRIAGED</Badge></td><td>Marcos Lima</td><td>21 min</td></tr><tr><td><strong>INC-0022</strong><br /><span>Sequência anômala de autenticação</span></td><td><Badge>MEDIUM</Badge></td><td><Badge tone="success">RESOLVED</Badge></td><td>—</td><td>1 h</td></tr></tbody></table></section> }
function Events() { return <section className="panel page-panel"><div className="panel-heading"><div><p className="eyebrow">ENTRADA NORMALIZADA</p><h2>Explorador de eventos</h2></div><Button>＋ Ingerir evento</Button></div><div className="empty-state"><div className="empty-icon">↯</div><h3>Selecione um filtro para explorar</h3><p className="muted">Os eventos são sintéticos, versionados e isolados por tenant.</p><Button variant="secondary">Configurar filtro</Button></div></section> }
function Investigation() { return <section className="panel page-panel"><div className="panel-heading"><div><p className="eyebrow">ASSISTÊNCIA CONTROLADA</p><h2>Investigação IA</h2></div><Badge tone="info">READ-ONLY</Badge></div><div className="callout"><strong>O agente não executa ações.</strong><span>Resultados são hipóteses com citações verificadas e podem ser inconclusivos.</span></div><div className="empty-state"><div className="empty-icon">✦</div><h3>Nenhuma investigação em andamento</h3><p className="muted">Inicie uma investigação a partir de um incidente autorizado.</p></div></section> }
function Policies() { return <section className="panel page-panel"><div className="panel-heading"><div><p className="eyebrow">GOVERNANÇA</p><h2>Políticas e ações</h2></div><Badge tone="success">DRY-RUN ATIVO</Badge></div><div className="policy-list"><div><strong>CREATE_INTERNAL_TICKET</strong><span>Ação de baixo impacto · sem aprovação</span><Badge tone="success">ATIVA</Badge></div><div><strong>FLAG_ACCOUNT_FOR_REVIEW_SIMULATION</strong><span>Ação sensível · aprovação humana</span><Badge tone="warning">REVISÃO</Badge></div><div><strong>DISABLE_DEMO_COUPON_SIMULATION</strong><span>Ação sensível · aprovação humana</span><Badge tone="warning">REVISÃO</Badge></div></div></section> }

function App() { const [view, setView] = useState<View>('overview'); return <div className="app-shell"><Sidebar view={view} setView={setView} /><main className="main"><Header view={view} /><div className="page">{view === 'overview' && <Overview />}{view === 'incidents' && <Incidents />}{view === 'events' && <Events />}{view === 'investigation' && <Investigation />}{view === 'policies' && <Policies />}</div></main></div> }

const queryClient = new QueryClient({ defaultOptions: { queries: { retry: 1, staleTime: 30_000 } } })
createRoot(document.getElementById('root')!).render(<StrictMode><QueryClientProvider client={queryClient}><App /></QueryClientProvider></StrictMode>)
