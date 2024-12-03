import dayjs from 'dayjs/esm';

export interface IAttendanceSyncCache {
  id: number;
  employeePin?: string | null;
  timestamp?: dayjs.Dayjs | null;
  terminal?: number | null;
}

export type NewAttendanceSyncCache = Omit<IAttendanceSyncCache, 'id'> & { id: null };
