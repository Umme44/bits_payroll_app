import dayjs from 'dayjs/esm';

import { IAttendanceSummary, NewAttendanceSummary } from './attendance-summary.model';

export const sampleWithRequiredData: IAttendanceSummary = {
  id: 90494,
};

export const sampleWithPartialData: IAttendanceSummary = {
  id: 64896,
  month: 64536,
  totalWorkingDays: 29594,
  totalLeaveDays: 73084,
  totalAbsentDays: 8480,
  totalFractionDays: 92548,
  attendanceRegularisationEndDate: dayjs('2021-02-16'),
};

export const sampleWithFullData: IAttendanceSummary = {
  id: 83756,
  month: 26319,
  year: 6382,
  totalWorkingDays: 79319,
  totalLeaveDays: 2133,
  totalAbsentDays: 39858,
  totalFractionDays: 83759,
  attendanceRegularisationStartDate: dayjs('2021-02-17'),
  attendanceRegularisationEndDate: dayjs('2021-02-16'),
};

export const sampleWithNewData: NewAttendanceSummary = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
