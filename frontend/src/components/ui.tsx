import { useEffect, useRef, useState, type InputHTMLAttributes, type ReactNode, type SelectHTMLAttributes } from 'react'

export function Input(props: InputHTMLAttributes<HTMLInputElement>) {
  return <input {...props} />
}

export function Select({ children, ...props }: SelectHTMLAttributes<HTMLSelectElement> & { children: ReactNode }) {
  return <select {...props}>{children}</select>
}

export type DropdownOption = { label: string; value: string }

export function Dropdown({ label, options, value, onChange }: { label: string; options: DropdownOption[]; value?: string; onChange?: (value: string) => void }) {
  const [open, setOpen] = useState(false)
  const [internalValue, setInternalValue] = useState(value ?? options[0]?.value)
  const ref = useRef<HTMLDivElement>(null)
  const currentValue = value ?? internalValue
  const selected = options.find(option => option.value === currentValue) ?? options[0]
  useEffect(() => { const close = (event: MouseEvent) => { if (!ref.current?.contains(event.target as Node)) setOpen(false) }; document.addEventListener('mousedown', close); return () => document.removeEventListener('mousedown', close) }, [])
  return <div className={`dropdown ${open ? 'is-open' : ''}`} ref={ref}>
    <button type="button" className="dropdown-trigger" aria-haspopup="listbox" aria-expanded={open} aria-label={label} onClick={() => setOpen(current => !current)}><span>{selected?.label ?? label}</span></button>
    {open && <div className="dropdown-menu" role="listbox" aria-label={label}>{options.map(option => <button type="button" role="option" aria-selected={option.value === selected?.value} className={option.value === selected?.value ? 'selected' : ''} key={option.value} onClick={() => { setInternalValue(option.value); onChange?.(option.value); setOpen(false) }}>{option.label}</button>)}</div>}
  </div>
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
