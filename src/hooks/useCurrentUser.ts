import { useCallback, useEffect, useState } from 'react';
import { clearStoredSession, isCoordinatorSession, readStoredSession } from '@/lib/auth';

export type StaffRole = 'LEAD_TEACHER' | 'TRAINER';

export interface UserProfile {
  id: string;
  fullName: string;
  email: string;
  role: string;
  timezone: string;
  schoolId: string | null;
  phoneNumber: string | null;
}

export function useCurrentUser() {
  const [loading, setLoading] = useState(true);
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [isCoordinator, setIsCoordinator] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const session = readStoredSession();
      if (!session) {
        setProfile(null);
        setIsCoordinator(false);
        setLoading(false);
        return;
      }
      const sub = session?.id ?? null;
      const coordinator = isCoordinatorSession(session);
      setIsCoordinator(coordinator);

      if (!sub) {
        setProfile(null);
        setLoading(false);
        return;
      }

      setProfile({
        id: session.id,
        fullName: session.fullName,
        email: session.email,
        role: session.role,
        timezone: 'UTC',
        schoolId: null,
        phoneNumber: null,
      });
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load profile');
      setProfile(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  const logout = useCallback(async () => {
    clearStoredSession();
    setProfile(null);
    setIsCoordinator(false);
  }, []);

  const isStaff = profile?.role === 'LEAD_TEACHER' || profile?.role === 'TRAINER';

  return {
    loading,
    profile,
    error,
    isCoordinator,
    isStaff: isStaff ?? false,
    staffRole: profile?.role as StaffRole | undefined,
    refresh,
    logout,
  };
}
