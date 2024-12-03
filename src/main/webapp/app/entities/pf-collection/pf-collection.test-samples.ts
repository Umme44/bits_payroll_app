import dayjs from 'dayjs/esm';

import { PfCollectionType } from 'app/entities/enumerations/pf-collection-type.model';

import { IPfCollection, NewPfCollection } from './pf-collection.model';

export const sampleWithRequiredData: IPfCollection = {
  id: 78701,
};

export const sampleWithPartialData: IPfCollection = {
  id: 65092,
  employerContribution: 16159,
  month: 9137,
  gross: 8771217,
  basic: 2772596,
};

export const sampleWithFullData: IPfCollection = {
  id: 98802,
  employeeContribution: 68994,
  employerContribution: 9892,
  transactionDate: dayjs('2021-04-17'),
  year: 85733,
  month: 98477,
  collectionType: PfCollectionType['CASH'],
  employeeInterest: 6818865,
  employerInterest: 2959636,
  gross: 3217445,
  basic: 4789143,
};

export const sampleWithNewData: NewPfCollection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
