const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL as string | undefined) ?? 'http://localhost:8081/api';

interface ApiResult<T> {
  data?: T;
  errors?: Array<{ message: string }>;
}

interface ListOptions {
  filter?: Record<string, unknown>;
  limit?: number;
}

interface SchoolRecord {
  id: string;
  name: string;
  type?: string;
  country?: string;
  city?: string;
  timezone?: string;
}

interface UserRecord {
  id: string;
  fullName: string;
  email: string;
  role: string;
  timezone?: string;
  schoolId?: string | null;
  phoneNumber?: string | null;
}

interface AvailabilityRecord {
  id: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  timezone?: string;
  isRecurring?: boolean;
  startDate?: string | null;
  endDate?: string | null;
}

interface BookingRecord {
  id: string;
  date: string;
  startTime: string;
  endTime: string;
  status: string;
  trainerId: string;
  leadTeacherId: string;
  memberSchoolId: string;
  partnerSchoolId: string;
}

interface ApiModel<T> {
  list: (options?: ListOptions) => Promise<ApiResult<T[]>>;
  get: (options: { id: string }) => Promise<ApiResult<T>>;
  create: (payload: Record<string, unknown>) => Promise<ApiResult<T>>;
  update: (payload: Record<string, unknown>) => Promise<ApiResult<T>>;
}

type DataModels = {
  School: ApiModel<SchoolRecord>;
  User: ApiModel<UserRecord>;
  Availability: Omit<ApiModel<AvailabilityRecord>, 'update'> & {
    delete: (options: { id: string }) => Promise<ApiResult<AvailabilityRecord>>;
  };
  Booking: ApiModel<BookingRecord>;
  LeadTeacherAssignment: Pick<ApiModel<Record<string, unknown>>, 'list'>;
};

function toRecord(value: unknown): Record<string, unknown> | undefined {
  return typeof value === 'object' && value !== null && !Array.isArray(value)
    ? (value as Record<string, unknown>)
    : undefined;
}

function getEqualityValue(value: unknown): string | undefined {
  const equality = toRecord(value)?.eq;
  return typeof equality === 'string' ? equality : undefined;
}

function getPayloadId(payload: Record<string, unknown>): string {
  return typeof payload.id === 'string' ? payload.id : '';
}

function getCurrentUserId(): string {
  return (window as Window & { __CURRENT_USER_ID__?: string }).__CURRENT_USER_ID__ ?? '';
}

async function request<T>(path: string, init: RequestInit = {}): Promise<ApiResult<T>> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...(init.headers ?? {}),
    },
  });

  const text = await response.text();
  const payload: unknown = text ? JSON.parse(text) : null;
  const payloadRecord = toRecord(payload);

  if (!response.ok) {
    const message = typeof payloadRecord?.message === 'string' ? payloadRecord.message : 'Request failed';
    return { errors: [{ message }] };
  }

  return { data: (payloadRecord?.data ?? payload ?? undefined) as T | undefined };
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

const baseModels: DataModels = {
  School: {
    list: async () => {
      const result = await request<SchoolRecord[]>('/schools', { method: 'GET' });
      return { ...result, data: Array.isArray(result.data) ? result.data : [] };
    },
    get: async ({ id }) => request<SchoolRecord>(`/schools/${id}`),
    create: async (payload) => request<SchoolRecord>('/schools', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload) => request<SchoolRecord>(`/schools/${getPayloadId(payload)}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  User: {
    list: async () => request<UserRecord[]>('/users', { method: 'GET' }),
    get: async ({ id }) => request<UserRecord>(`/users/${id}`),
    create: async (payload) => request<UserRecord>('/users', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload) => request<UserRecord>(`/users/${getPayloadId(payload)}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  Availability: {
    list: async ({ filter }: ListOptions = {}) => {
      const ownerType = typeof filter?.ownerType === 'string'
        ? filter.ownerType
        : getEqualityValue(filter?.ownerType);
      const ownerId = getEqualityValue(filter?.ownerID);
      if (ownerType === 'SCHOOL' && ownerId) {
        return request<AvailabilityRecord[]>(`/availability/school/${ownerId}`);
      }
      return request<AvailabilityRecord[]>('/availability', { method: 'GET' });
    },
    get: async ({ id }) => request<AvailabilityRecord>(`/availability/${id}`),
    create: async (payload) => request<AvailabilityRecord>('/availability?userId=' + encodeURIComponent(String(payload.userId ?? '')), { method: 'POST', body: JSON.stringify(payload) }),
    delete: async ({ id }) => request<AvailabilityRecord>(`/availability/${id}`, { method: 'DELETE' }),
  },
  Booking: {
    list: async ({ filter }: ListOptions = {}) => {
      const date = getEqualityValue(filter?.date);
      const status = getEqualityValue(filter?.status);
      if (date && status) {
        return request<BookingRecord[]>(buildQueryUrl('/bookings/date', { date, status }));
      }
      return request<BookingRecord[]>('/bookings', { method: 'GET' });
    },
    get: async ({ id }) => request<BookingRecord>(`/bookings/${id}`),
    create: async (payload) => request<BookingRecord>('/bookings', { method: 'POST', body: JSON.stringify(payload) }),
    update: async (payload) => request<BookingRecord>(`/bookings/${getPayloadId(payload)}`, { method: 'PUT', body: JSON.stringify(payload) }),
  },
  LeadTeacherAssignment: {
    list: async () => request<Record<string, unknown>[]>('/lead-teacher-assignments', { method: 'GET' }),
  },
};

export const dataClient = {
  models: baseModels,
  queries: {
    findAvailableTrainers: async (args: { schoolId?: string; date: string }): Promise<ApiResult<unknown>> => {
      const url = buildQueryUrl('/availability/find-trainers', {
        schoolId: args.schoolId,
        date: args.date,
      });
      return request(url, { method: 'GET' });
    },
  },
  mutations: {
    sendSessionNotification: async (payload: Record<string, unknown>) => request('/notifications/session', { method: 'POST', body: JSON.stringify(payload) }),
    cancelSession: async ({ bookingId, reason }: { bookingId: string; reason?: string }) => request<unknown>(`/bookings/${bookingId}/cancel?userId=${encodeURIComponent(getCurrentUserId())}`, { method: 'POST', body: JSON.stringify({ cancellationReason: reason ?? '' }) }),
    createSchoolAvailability: async (payload: Record<string, unknown>) => {
      const userId = getCurrentUserId();
      return request<unknown>(`/availability?userId=${encodeURIComponent(userId)}`, { method: 'POST', body: JSON.stringify(payload) });
    },
    deleteSchoolAvailability: async ({ availabilityId }: { availabilityId: string }) => request<unknown>(`/availability/${availabilityId}`, { method: 'DELETE' }),
    createUserByCoordinator: async (payload: Record<string, unknown>) => request<{ success?: boolean; message?: string }>('/users', { method: 'POST', body: JSON.stringify(payload) }),
  },
};
