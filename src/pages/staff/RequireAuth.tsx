import { Navigate, Outlet, useLocation } from 'react-router';
import { readStoredSession } from '@/lib/auth';

export default function RequireAuth() {
  const location = useLocation();
  const session = readStoredSession();

  if (!session) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  return <Outlet />;
}
