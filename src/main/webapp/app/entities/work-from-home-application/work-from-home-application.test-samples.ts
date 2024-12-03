import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IWorkFromHomeApplication, NewWorkFromHomeApplication } from './work-from-home-application.model';

export const sampleWithRequiredData: IWorkFromHomeApplication = {
  id: 28084,
  startDate: dayjs('2022-08-08'),
  endDate: dayjs('2022-08-08'),
  reason: 'Minnesota Nebraska Lev',
  status: Status['NOT_APPROVED'],
};

export const sampleWithPartialData: IWorkFromHomeApplication = {
  id: 27809,
  startDate: dayjs('2022-08-08'),
  endDate: dayjs('2022-08-09'),
  reason: 'Designer back-end Chair',
  status: Status['APPROVED'],
  appliedAt: dayjs('2022-08-08'),
  createdAt: dayjs('2022-08-08T07:34'),
};

export const sampleWithFullData: IWorkFromHomeApplication = {
  id: 9851,
  startDate: dayjs('2022-08-08'),
  endDate: dayjs('2022-08-08'),
  reason: 'Missouri Borders Fresh',
  duration: 8128,
  status: Status['APPROVED'],
  appliedAt: dayjs('2022-08-09'),
  updatedAt: dayjs('2022-08-08T23:07'),
  createdAt: dayjs('2022-08-09T02:28'),
  sanctionedAt: dayjs('2022-08-08T09:12'),
};

export const sampleWithNewData: NewWorkFromHomeApplication = {
  startDate: dayjs('2022-08-09'),
  endDate: dayjs('2022-08-08'),
  reason: 'Forward Account bypassing',
  status: Status['APPROVED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
