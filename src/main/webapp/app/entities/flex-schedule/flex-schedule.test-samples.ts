import dayjs from 'dayjs/esm';

import { IFlexSchedule, NewFlexSchedule } from './flex-schedule.model';

export const sampleWithRequiredData: IFlexSchedule = {
  id: 13131,
  effectiveDate: dayjs('2022-03-07'),
  inTime: dayjs('2022-03-07T23:37'),
  outTime: dayjs('2022-03-08T07:24'),
};

export const sampleWithPartialData: IFlexSchedule = {
  id: 69441,
  effectiveDate: dayjs('2022-03-07'),
  inTime: dayjs('2022-03-08T09:01'),
  outTime: dayjs('2022-03-07T09:23'),
};

export const sampleWithFullData: IFlexSchedule = {
  id: 84113,
  effectiveDate: dayjs('2022-03-08'),
  inTime: dayjs('2022-03-08T04:38'),
  outTime: dayjs('2022-03-07T23:16'),
};

export const sampleWithNewData: NewFlexSchedule = {
  effectiveDate: dayjs('2022-03-07'),
  inTime: dayjs('2022-03-08T08:18'),
  outTime: dayjs('2022-03-07T10:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
