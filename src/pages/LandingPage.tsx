import { Link } from 'react-router';

export default function LandingPage() {
  return (
    <main className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-5 py-4">
          <Link to="/" className="text-lg font-bold tracking-tight text-slate-900">
            ZestP2P
          </Link>
          <Link
            to="/login"
            className="rounded-lg bg-[var(--color-brand)] px-4 py-2 text-sm font-semibold text-white transition hover:opacity-90"
          >
            Sign in
          </Link>
        </div>
      </header>

      <section className="relative isolate overflow-hidden border-b border-slate-200 bg-white">
        <div className="absolute inset-y-0 right-0 -z-10 hidden w-1/2 bg-emerald-50 lg:block" />
        <div className="mx-auto grid min-h-[72vh] max-w-6xl items-center gap-12 px-5 py-16 lg:grid-cols-[1fr_0.8fr] lg:py-20">
          <div className="max-w-2xl">
            <p className="mb-5 text-sm font-semibold uppercase tracking-[0.16em] text-emerald-700">
              Peer-to-peer scheduling
            </p>
            <h1 className="text-4xl font-bold leading-tight text-slate-950 md:text-5xl">
              Make time for the work that matters.
            </h1>
            <p className="mt-6 max-w-xl text-lg leading-8 text-slate-600">
              Coordinate trainers, schools, availability, and sessions in one straightforward workspace.
            </p>
            <div className="mt-8 flex flex-wrap items-center gap-3">
              <Link
                to="/login"
                className="rounded-lg bg-[var(--color-brand)] px-6 py-3 font-semibold text-white transition hover:opacity-90"
              >
                Open scheduler
              </Link>
              <span className="text-sm text-slate-500">Scheduling workspace</span>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3" aria-label="Scheduling overview">
            <div className="col-span-2 rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
              <div className="flex items-center justify-between border-b border-slate-100 pb-4">
                <div>
                  <p className="text-xs font-medium uppercase tracking-wide text-slate-500">This week</p>
                  <p className="mt-1 text-lg font-semibold text-slate-900">Session overview</p>
                </div>
                <span className="rounded-full bg-emerald-100 px-3 py-1 text-xs font-semibold text-emerald-800">On track</span>
              </div>
              <div className="mt-4 space-y-3">
                <div className="flex items-center justify-between rounded-lg bg-slate-50 px-4 py-3">
                  <span className="text-sm font-medium text-slate-700">Trainer availability</span>
                  <span className="text-sm font-semibold text-emerald-700">Updated</span>
                </div>
                <div className="flex items-center justify-between rounded-lg bg-slate-50 px-4 py-3">
                  <span className="text-sm font-medium text-slate-700">School schedules</span>
                  <span className="text-sm font-semibold text-slate-700">Coordinated</span>
                </div>
                <div className="flex items-center justify-between rounded-lg bg-slate-50 px-4 py-3">
                  <span className="text-sm font-medium text-slate-700">Upcoming sessions</span>
                  <span className="text-sm font-semibold text-slate-700">Ready</span>
                </div>
              </div>
            </div>
            <div className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
              <p className="text-2xl font-bold text-slate-900">01</p>
              <p className="mt-1 text-sm text-slate-500">Share availability</p>
            </div>
            <div className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
              <p className="text-2xl font-bold text-slate-900">02</p>
              <p className="mt-1 text-sm text-slate-500">Schedule a session</p>
            </div>
          </div>
        </div>
      </section>

      <footer className="mx-auto flex max-w-6xl flex-col gap-2 px-5 py-6 text-sm text-slate-500 sm:flex-row sm:items-center sm:justify-between">
        <span>ZestP2P</span>
        <span>Personal scheduling project</span>
      </footer>
    </main>
  );
}
