import { useState } from 'react';
import type { FormEvent } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router';
import { writeStoredSession } from '@/lib/auth';
import type { AppSession } from '@/lib/auth';

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState('staff@example.com');
  const [fullName, setFullName] = useState('Demo Staff Member');
  const [role, setRole] = useState<'COORDINATOR' | 'LEAD_TEACHER' | 'TRAINER'>('TRAINER');
  const [error, setError] = useState<string | null>(null);

  const from = (location.state as { from?: string } | null)?.from;

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();

    if (!email.trim()) {
      setError('Email is required.');
      return;
    }

    const session: AppSession = {
      id: `demo-${email.trim().toLowerCase().replace(/[^a-z0-9]+/g, '-')}`,
      email: email.trim(),
      fullName: fullName.trim() || email.trim(),
      role,
      groups: role === 'COORDINATOR' ? ['Coordinators'] : [],
    };

    writeStoredSession(session);
    const nextLocation = from?.startsWith('/app') || role === 'COORDINATOR' ? '/app' : '/dashboard';
    navigate(nextLocation, { replace: true });
  };

  const isSignedIn = Boolean(location.state && typeof location.state === 'object' && 'from' in location.state);
  if (isSignedIn && from) {
    return <Navigate to={from} replace />;
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 bg-slate-100">
      <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg border border-slate-200">
        <div className="mb-6 text-center">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-[var(--color-brand)]">
            ZestP2P
          </p>
          <h1 className="mt-2 text-2xl font-bold text-slate-900">Sign in</h1>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <label className="block text-sm font-medium text-slate-700">
            Full name
            <input
              value={fullName}
              onChange={(event) => setFullName(event.target.value)}
              className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-[var(--color-brand)]"
            />
          </label>

          <label className="block text-sm font-medium text-slate-700">
            Email
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-[var(--color-brand)]"
            />
          </label>

          <label className="block text-sm font-medium text-slate-700">
            Role
            <select
              value={role}
              onChange={(event) => setRole(event.target.value as 'COORDINATOR' | 'LEAD_TEACHER' | 'TRAINER')}
              className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-[var(--color-brand)]"
            >
              <option value="COORDINATOR">Coordinator</option>
              <option value="LEAD_TEACHER">Lead Teacher</option>
              <option value="TRAINER">Trainer</option>
            </select>
          </label>

          {error && <p className="text-sm text-red-600">{error}</p>}

          <button
            type="submit"
            className="w-full rounded-lg bg-[var(--color-brand)] px-4 py-2.5 font-semibold text-white transition hover:opacity-90"
          >
            Continue
          </button>
        </form>

        <div className="mt-5 text-center">
          <Link to="/" className="text-sm text-slate-500 hover:text-slate-700">
            Back to home
          </Link>
        </div>
      </div>
    </div>
  );
}
