import { useCallback, useEffect, useState } from 'react';
import { clearStoredSession, isCoordinatorSession, readStoredSession } from '@/lib/auth';

export function useCoordinatorAuth() {
  const [loading, setLoading] = useState(true);
  const [isCoordinator, setIsCoordinator] = useState(false);
  const [userSub, setUserSub] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    try {
      const session = readStoredSession();
      const sub = session?.id ?? null;
      setUserSub(sub);
      setIsCoordinator(isCoordinatorSession(session));
    } catch {
      setUserSub(null);
      setIsCoordinator(false);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  const logout = useCallback(async () => {
    clearStoredSession();
    setUserSub(null);
    setIsCoordinator(false);
  }, []);

  return { loading, isCoordinator, userSub, refresh, logout };
}
