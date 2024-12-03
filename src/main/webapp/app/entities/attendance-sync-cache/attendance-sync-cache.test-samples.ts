import dayjs from 'dayjs/esm';

import { IAttendanceSyncCache, NewAttendanceSyncCache } from './attendance-sync-cache.model';

export const sampleWithRequiredData: IAttendanceSyncCache = {
  id: 80980,
};

export const sampleWithPartialData: IAttendanceSyncCache = {
  id: 78073,
  timestamp: dayjs('2021-06-09T20:47'),
  terminal: 23925,
};

export const sampleWithFullData: IAttendanceSyncCache = {
  id: 74024,
  employeePin: 'white Towels',
  timestamp: dayjs('2021-06-09T12:41'),
  terminal: 51000,
};

export const sampleWithNewData: NewAttendanceSyncCache = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
