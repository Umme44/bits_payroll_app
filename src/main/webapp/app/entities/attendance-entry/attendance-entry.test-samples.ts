import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IAttendanceEntry, NewAttendanceEntry } from './attendance-entry.model';

export const sampleWithRequiredData: IAttendanceEntry = {
  id: 46435,
};

export const sampleWithPartialData: IAttendanceEntry = {
  id: 18301,
  inTime: dayjs('2021-02-28T23:38'),
  inNote: 'Kyat',
};

export const sampleWithFullData: IAttendanceEntry = {
  id: 10601,
  date: dayjs('2021-02-28'),
  inTime: dayjs('2021-02-28T14:56'),
  inNote: 'Forge Frozen Chicken',
  outTime: dayjs('2021-03-01T00:08'),
  outNote: 'Home Cambridgeshire',
  status: Status['APPROVED'],
  note: 'Polarised Rand Communications',
};

export const sampleWithNewData: NewAttendanceEntry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
