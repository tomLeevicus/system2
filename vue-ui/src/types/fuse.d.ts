declare module 'fuse.js' {
  interface FuseOptions<T> {
    keys?: string[]
    threshold?: number
    location?: number
    distance?: number
    maxPatternLength?: number
    caseSensitive?: boolean
    tokenize?: boolean
    matchAllTokens?: boolean
    findAllMatches?: boolean
    includeScore?: boolean
    includeMatches?: boolean
    shouldSort?: boolean
    sortFn?: (a: { score: number }, b: { score: number }) => number
    tokenSeparator?: RegExp
    minMatchCharLength?: number
    id?: string
    getFn?: (obj: any, path: string) => any
  }

  class Fuse<T> {
    constructor(list: T[], options?: FuseOptions<T>)
    search(pattern: string): Array<{
      item: T
      refIndex: number
      score?: number
      matches?: any[]
    }>
    setCollection(list: T[]): void
  }

  export default Fuse
} 