import { cache } from './cache'
import { TOKEN_KEY } from '@/constants/cache-keys'

export function getToken() {
  return cache.get(TOKEN_KEY)
}

export function setToken(token: string) {
  return cache.set(TOKEN_KEY, token)
}

export function removeToken() {
  return cache.remove(TOKEN_KEY)
} 