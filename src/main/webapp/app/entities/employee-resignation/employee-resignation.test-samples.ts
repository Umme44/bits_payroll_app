import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IEmployeeResignation, NewEmployeeResignation } from './employee-resignation.model';

export const sampleWithRequiredData: IEmployeeResignation = {
  id: 6330,
  lastWorkingDay: dayjs('2021-05-17'),
};

export const sampleWithPartialData: IEmployeeResignation = {
  id: 66667,
  updatedAt: dayjs('2021-05-16'),
  isFestivalBonusHold: false,
  resignationReason: 'Exclusive',
  lastWorkingDay: dayjs('2021-05-16'),
};

export const sampleWithFullData: IEmployeeResignation = {
  id: 80277,
  createdAt: dayjs('2021-05-17'),
  updatedAt: dayjs('2021-05-17'),
  resignationDate: dayjs('2021-05-16'),
  approvalStatus: Status['APPROVED'],
  approvalComment: 'infomediaries',
  isSalaryHold: true,
  isFestivalBonusHold: true,
  resignationReason: 'Multi-layered capacitor',
  lastWorkingDay: dayjs('2021-05-16'),
};

export const sampleWithNewData: NewEmployeeResignation = {
  lastWorkingDay: dayjs('2021-05-17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
