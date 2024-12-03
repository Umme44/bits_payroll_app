import dayjs from 'dayjs/esm';

import { IBand, NewBand } from './band.model';

export const sampleWithRequiredData: IBand = {
  id: 51648,
  bandName: 'Directives Investment payment',
  minSalary: 59853,
  maxSalary: 58543,
};

export const sampleWithPartialData: IBand = {
  id: 80289,
  bandName: 'Keyboard Bermuda salmon',
  minSalary: 44578,
  maxSalary: 34048,
  createdAt: dayjs('2021-02-14'),
};

export const sampleWithFullData: IBand = {
  id: 63106,
  bandName: 'content Analyst',
  minSalary: 23157,
  maxSalary: 18938,
  welfareFund: 72535,
  mobileCelling: 76743,
  createdAt: dayjs('2021-02-14'),
  updatedAt: dayjs('2021-02-14'),
};

export const sampleWithNewData: NewBand = {
  bandName: 'Beauty Tala visualize',
  minSalary: 82047,
  maxSalary: 38290,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
