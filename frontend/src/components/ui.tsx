import type { InputHTMLAttributes, ReactNode, SelectHTMLAttributes } from 'react'

export function Input(props: InputHTMLAttributes<HTMLInputElement>) {
  return <input {...props} />
}

export function Select({ children, ...props }: SelectHTMLAttributes<HTMLSelectElement> & { children: ReactNode }) {
  return <select {...props}>{children}</select>
}

export function Skeleton({ width = '100%' }: { width?: string }) {
  return <span aria-hidden="true" style={{ display: 'block', width, height: 14, borderRadius: 4, background: '#26364d' }} />
}

export function Toast({ children }: { children: ReactNode }) {
  return <div role="status" className="callout">{children}</div>
}

export function Dialog({ title, children, onClose }: { title: string; children: ReactNode; onClose: () => void }) {
  return <div role="dialog" aria-modal="true" aria-label={title} className="panel"><div className="panel-heading"><h3>{title}</h3><button className="button button-ghost" onClick={onClose}>Fechar</button></div>{children}</div>
}
