import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IFlexScheduleApplication, NewFlexScheduleApplication } from './flex-schedule-application.model';

export const sampleWithRequiredData: IFlexScheduleApplication = {
  id: 10786,
  effectiveFrom: dayjs('2022-08-22'),
  effectiveTo: dayjs('2022-08-23'),
  status: Status['APPROVED'],
  appliedAt: dayjs('2022-08-22'),
  createdAt: dayjs('2022-08-23T07:25'),
};

export const sampleWithPartialData: IFlexScheduleApplication = {
  id: 44433,
  effectiveFrom: dayjs('2022-08-22'),
  effectiveTo: dayjs('2022-08-23'),
  status: Status['PENDING'],
  sanctionedAt: dayjs('2022-08-23T04:39'),
  appliedAt: dayjs('2022-08-23'),
  createdAt: dayjs('2022-08-23T12:13'),
  updatedAt: dayjs('2022-08-23T00:16'),
};

export const sampleWithFullData: IFlexScheduleApplication = {
  id: 94666,
  effectiveFrom: dayjs('2022-08-22'),
  effectiveTo: dayjs('2022-08-23'),
  reason: '../fake-data/blob/hipster.txt',
  status: Status['NOT_APPROVED'],
  sanctionedAt: dayjs('2022-08-23T00:52'),
  appliedAt: dayjs('2022-08-23'),
  createdAt: dayjs('2022-08-23T03:22'),
  updatedAt: dayjs('2022-08-23T07:41'),
};

export const sampleWithNewData: NewFlexScheduleApplication = {
  effectiveFrom: dayjs('2022-08-22'),
  effectiveTo: dayjs('2022-08-23'),
  status: Status['APPROVED'],
  appliedAt: dayjs('2022-08-23'),
  createdAt: dayjs('2022-08-22T19:39'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
