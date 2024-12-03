import dayjs from 'dayjs/esm';

import { EventType } from 'app/entities/enumerations/event-type.model';

import { IEmploymentHistory, NewEmploymentHistory } from './employment-history.model';

export const sampleWithRequiredData: IEmploymentHistory = {
  id: 48364,
};

export const sampleWithPartialData: IEmploymentHistory = {
  id: 61700,
  previousMainGrossSalary: 69523,
  currentMainGrossSalary: 66890,
  changedWorkingHour: 'network Hat Alabama',
  isModifiable: false,
};

export const sampleWithFullData: IEmploymentHistory = {
  id: 68859,
  referenceId: 'Loan',
  pin: 'SDD Delaware redefine',
  eventType: EventType['RESIGNATION'],
  effectiveDate: dayjs('2021-02-15'),
  previousMainGrossSalary: 71971,
  currentMainGrossSalary: 1502,
  previousWorkingHour: 'matrix',
  changedWorkingHour: 'Outdoors Paradigm',
  isModifiable: false,
};

export const sampleWithNewData: NewEmploymentHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
