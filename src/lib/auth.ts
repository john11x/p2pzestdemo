export type AppRole = 'COORDINATOR' | 'LEAD_TEACHER' | 'TRAINER';

export interface AppSession {
  id: string;
  email: string;
  fullName: string;
  role: AppRole;
  groups: string[];
}

const STORAGE_KEY = 'zestp2p.auth.session';

export function readStoredSession(): AppSession | null {
  if (typeof window === 'undefined') {
    return null;
  }

  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }

    const parsed = JSON.parse(raw) as Partial<AppSession>;
    if (!parsed.id || !parsed.email || !parsed.fullName) {
      return null;
    }

    return {
      id: parsed.id,
      email: parsed.email,
      fullName: parsed.fullName,
      role: (parsed.role as AppRole) ?? 'TRAINER',
      groups: Array.isArray(parsed.groups) ? parsed.groups : [],
    };
  } catch {
    return null;
  }
}

export function writeStoredSession(session: AppSession) {
  if (typeof window === 'undefined') {
    return;
  }

  window.localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
}

export function clearStoredSession() {
  if (typeof window === 'undefined') {
    return;
  }

  window.localStorage.removeItem(STORAGE_KEY);
}

export function isCoordinatorSession(session: AppSession | null): boolean {
  return Boolean(
    session && (
      session.role === 'COORDINATOR' ||
      session.groups.includes('Coordinators') ||
      session.groups.includes('coordinators')
    ),
  );
}
