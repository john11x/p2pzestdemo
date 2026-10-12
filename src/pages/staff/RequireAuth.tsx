import { Navigate, Outlet, useLocation } from 'react-router';
import { useEffect, useState } from 'react';
import { readStoredSession } from '@/lib/auth';
import Spinner from '@/components/shared/Spinner';

export default function RequireAuth() {
  const location = useLocation();
  const [checked, setChecked] = useState(false);
  const [signedIn, setSignedIn] = useState(false);

  useEffect(() => {
    const session = readStoredSession();
    setSignedIn(Boolean(session));
    setChecked(true);
  }, [location.pathname]);

  if (!checked) {
    return (
      <div className="min-h-[50vh] flex items-center justify-center">
        <Spinner />
      </div>
    );
  }

  if (!signedIn) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  return <Outlet />;
}
