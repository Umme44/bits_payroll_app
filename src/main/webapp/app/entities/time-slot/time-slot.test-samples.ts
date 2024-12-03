import dayjs from 'dayjs/esm';

import { ITimeSlot, NewTimeSlot } from './time-slot.model';

export const sampleWithRequiredData: ITimeSlot = {
  id: 5308,
  title: 'Idaho Shoes Incredible',
  inTime: dayjs('2022-05-11T21:11'),
  outTime: dayjs('2022-05-12T00:58'),
};

export const sampleWithPartialData: ITimeSlot = {
  id: 62948,
  title: 'virtual Program',
  inTime: dayjs('2022-05-12T12:08'),
  outTime: dayjs('2022-05-12T07:06'),
  isApplicableByEmployee: false,
  isDefaultShift: true,
};

export const sampleWithFullData: ITimeSlot = {
  id: 6031,
  title: 'reciprocal',
  inTime: dayjs('2022-05-12T00:44'),
  outTime: dayjs('2022-05-11T23:00'),
  isApplicableByEmployee: true,
  isDefaultShift: true,
  code: 'red Principal',
  weekEnds: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTimeSlot = {
  title: 'generation driver overriding',
  inTime: dayjs('2022-05-11T22:53'),
  outTime: dayjs('2022-05-12T11:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
