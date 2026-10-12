const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL as string | undefined) ?? 'http://localhost:8081/api';

async function request<T>(path: string, init: RequestInit = {}): Promise<{ data?: T; errors?: Array<{ message: string }> }> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...(init.headers ?? {}),
    },
  });

  const text = await response.text();
  const payload = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = payload?.message ?? 'Request failed';
    return { errors: [{ message }] };
  }

  return { data: payload?.data ?? payload ?? undefined };
}

function buildQueryUrl(path: string, params: Record<string, string | undefined> = {}) {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      search.append(key, value);
    }
  });
  const suffix = search.toString();
  return suffix ? `${path}?${suffix}` : path;
}

const baseModels: any = {
  School: {
    list: async ({ filter: _filter, limit: _limit }: { filter?: { type?: { eq?: string } }; limit?: number } = {}) => {
      return request('/schools', {
        method: 'GET',
      }).then((result) => ({
        ...result,
        data: Array.isArray(result.data) ? result.data : ((result.data ?? []) as any),
      }));
    },
    get: async ({ id }: { id: string }) => request(`/schools/${id}`),
    create: async (payload: Record<string, unknown>) => request('/schools', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload: Record<string, unknown>) => request(`/schools/${(payload as any).id ?? ''}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  User: {
    list: async ({ limit: _limit }: { limit?: number } = {}) => request('/users', { method: 'GET' }),
    get: async ({ id }: { id: string }) => request(`/users/${id}`),
    create: async (payload: Record<string, unknown>) => request('/users', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload: Record<string, unknown>) => request(`/users/${(payload as any).id ?? ''}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  Availability: {
    list: async ({ filter, limit: _limit }: { filter?: Record<string, unknown>; limit?: number } = {}) => {
      if (filter?.ownerType && filter.ownerType === 'SCHOOL') {
        const ownerID = filter.ownerID as { eq?: string } | undefined;
        if (ownerID?.eq) {
          return request(`/availability/school/${ownerID.eq}`);
        }
      }
      return request('/availability', { method: 'GET' });
    },
    get: async ({ id }: { id: string }) => request(`/availability/${id}`),
    create: async (payload: Record<string, unknown>) => request('/availability?userId=' + encodeURIComponent(String((payload as any).userId ?? '')), { method: 'POST', body: JSON.stringify(payload) }),
    delete: async ({ id }: { id: string }) => request(`/availability/${id}`, { method: 'DELETE' }),
  },
  Booking: {
    list: async ({ filter }: { filter?: Record<string, unknown> } = {}) => {
      if (filter?.date && filter?.status) {
        return request(buildQueryUrl('/bookings/date', { date: String((filter as any).date.eq ?? ''), status: String((filter as any).status.eq ?? '') }));
      }
      return request('/bookings', { method: 'GET' });
    },
    get: async ({ id }: { id: string }) => request(`/bookings/${id}`),
    create: async (payload: Record<string, unknown>) => request('/bookings', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload: Record<string, unknown>) => request(`/bookings/${(payload as any).id ?? ''}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  LeadTeacherAssignment: {
    list: async ({ filter: _filter }: { filter?: Record<string, unknown> } = {}) => request('/lead-teacher-assignments', { method: 'GET' }),
  },
};

export const dataClient: any = {
  models: baseModels,
  queries: {
    findAvailableTrainers: async (args: { schoolId?: string; date: string }) => {
      const url = buildQueryUrl('/availability/find-trainers', {
        schoolId: args.schoolId,
        date: args.date,
      });
      return request(url, { method: 'GET' });
    },
  },
  mutations: {
    sendSessionNotification: async (payload: Record<string, unknown>) => request('/notifications/session', { method: 'POST', body: JSON.stringify(payload) }),
    cancelSession: async ({ bookingId, reason }: { bookingId: string; reason?: string }) => request(`/bookings/${bookingId}/cancel?userId=${encodeURIComponent((window as any).__CURRENT_USER_ID__ ?? '')}`, { method: 'POST', body: JSON.stringify({ cancellationReason: reason ?? '' }) }),
    createSchoolAvailability: async (payload: Record<string, unknown>) => {
      const userId = (window as any).__CURRENT_USER_ID__ ?? '';
      return request(`/availability?userId=${encodeURIComponent(userId)}`, { method: 'POST', body: JSON.stringify(payload) });
    },
    deleteSchoolAvailability: async ({ availabilityId }: { availabilityId: string }) => request(`/availability/${availabilityId}`, { method: 'DELETE' }),
    createUserByCoordinator: async (payload: Record<string, unknown>) => request('/users', { method: 'POST', body: JSON.stringify(payload) }),
  },
};
