import dayjs from 'dayjs/esm';

import { ISpecialShiftTiming, NewSpecialShiftTiming } from './special-shift-timing.model';

export const sampleWithRequiredData: ISpecialShiftTiming = {
  id: 62127,
  startDate: dayjs('2022-09-10'),
  endDate: dayjs('2022-09-10'),
  overrideRoaster: false,
  overrideFlexSchedule: true,
};

export const sampleWithPartialData: ISpecialShiftTiming = {
  id: 68791,
  startDate: dayjs('2022-09-11'),
  endDate: dayjs('2022-09-11'),
  overrideRoaster: false,
  overrideFlexSchedule: false,
  remarks: 'multi-byte Isle Buckinghamshire',
  reason: 'e-enable Customer',
};

export const sampleWithFullData: ISpecialShiftTiming = {
  id: 75942,
  startDate: dayjs('2022-09-10'),
  endDate: dayjs('2022-09-10'),
  overrideRoaster: false,
  overrideFlexSchedule: false,
  remarks: 'Account (E.M.U.-6) Steel',
  createdAt: dayjs('2022-09-11T02:07'),
  updatedAt: dayjs('2022-09-11T00:01'),
  reason: 'navigating HTTP',
};

export const sampleWithNewData: NewSpecialShiftTiming = {
  startDate: dayjs('2022-09-11'),
  endDate: dayjs('2022-09-10'),
  overrideRoaster: false,
  overrideFlexSchedule: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
