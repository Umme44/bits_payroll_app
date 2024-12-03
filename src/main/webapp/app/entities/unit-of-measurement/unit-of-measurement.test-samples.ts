import dayjs from 'dayjs/esm';

import { IUnitOfMeasurement, NewUnitOfMeasurement } from './unit-of-measurement.model';

export const sampleWithRequiredData: IUnitOfMeasurement = {
  id: 67274,
  name: 'payment',
  createdAt: dayjs('2023-01-18T01:14'),
};

export const sampleWithPartialData: IUnitOfMeasurement = {
  id: 70820,
  name: 'frictionless',
  remarks: 'blue Wooden',
  createdAt: dayjs('2023-01-17T06:13'),
};

export const sampleWithFullData: IUnitOfMeasurement = {
  id: 59099,
  name: 'rich',
  remarks: 'markets payment',
  createdAt: dayjs('2023-01-17T07:13'),
  updatedAt: dayjs('2023-01-18T00:55'),
};

export const sampleWithNewData: NewUnitOfMeasurement = {
  name: 'Shoes',
  createdAt: dayjs('2023-01-17T23:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
