import { Navigate, Outlet, useLocation } from 'react-router';
import { useEffect, useState } from 'react';
import { isCoordinatorSession, readStoredSession } from '@/lib/auth';
import { useCoordinatorAuth } from '@/hooks/useCoordinatorAuth';
import Spinner from '@/components/shared/Spinner';

export default function RequireCoordinator() {
  const { loading, isCoordinator, logout } = useCoordinatorAuth();
  const location = useLocation();
  const [authChecked, setAuthChecked] = useState(false);
  const [signedIn, setSignedIn] = useState(false);

  useEffect(() => {
    const session = readStoredSession();
    setSignedIn(Boolean(session));
    setAuthChecked(true);
  }, [location.pathname]);

  if (!authChecked || loading) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <Spinner />
      </div>
    );
  }

  if (!signedIn) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  if (!isCoordinator && !isCoordinatorSession(readStoredSession())) {
    return (
      <div className="min-h-screen flex items-center justify-center p-6 bg-slate-50">
        <div className="card max-w-md p-8 text-center">
          <h1 className="text-xl font-bold text-slate-900 mb-2">Coordinator access required</h1>
          <p className="text-slate-600 text-sm mb-6">
            Your account is not marked as a coordinator in this demo session. Sign in with a
            coordinator account to continue.
          </p>
          <div className="flex flex-col gap-2">
            <a
              href="/"
              className="inline-flex justify-center px-4 py-2 rounded-lg bg-[var(--color-brand)] text-white font-semibold text-sm"
            >
              Back to home
            </a>
            <button
              type="button"
              onClick={() => void logout()}
              className="text-sm text-slate-500 hover:text-slate-700"
            >
              Sign out
            </button>
          </div>
        </div>
      </div>
    );
  }

  return <Outlet />;
}
