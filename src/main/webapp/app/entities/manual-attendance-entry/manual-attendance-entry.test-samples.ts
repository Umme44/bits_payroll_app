import dayjs from 'dayjs/esm';

import { IManualAttendanceEntry, NewManualAttendanceEntry } from './manual-attendance-entry.model';

export const sampleWithRequiredData: IManualAttendanceEntry = {
  id: 53197,
};

export const sampleWithPartialData: IManualAttendanceEntry = {
  id: 92543,
  date: dayjs('2021-06-30'),
  inTime: dayjs('2021-06-30T17:35'),
  outTime: dayjs('2021-06-30T07:36'),
  outNote: 'Generic Lock hybrid',
  note: 'portals',
};

export const sampleWithFullData: IManualAttendanceEntry = {
  id: 53837,
  date: dayjs('2021-07-01'),
  inTime: dayjs('2021-07-01T01:10'),
  inNote: 'Chair Open-source of',
  outTime: dayjs('2021-07-01T01:48'),
  outNote: 'Avon bleeding-edge',
  isLineManagerApproved: true,
  isHRApproved: false,
  isRejected: true,
  rejectionComment: 'Bedfordshire 1080p',
  note: 'Accounts input',
};

export const sampleWithNewData: NewManualAttendanceEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
