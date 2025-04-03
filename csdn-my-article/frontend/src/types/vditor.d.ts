declare module 'vditor' {
  interface IVditorOptions {
    value?: string
    toolbar?: Array<string>
    mode?: 'sv' | 'wysiwyg' | 'ir'
    preview?: {
      markdown?: {
        toc?: boolean
        mark?: boolean
      }
    }
  }

  export default class Vditor {
    constructor(element: HTMLElement, options: IVditorOptions)
    getValue(): string
    destroy(): void
  }
}