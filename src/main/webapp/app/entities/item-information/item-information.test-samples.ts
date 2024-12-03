import dayjs from 'dayjs/esm';

import { IItemInformation, NewItemInformation } from './item-information.model';

export const sampleWithRequiredData: IItemInformation = {
  id: 34137,
  code: 'transmit',
  name: 'Metal bleeding-edge Buckinghamshire',
  specification: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2023-01-18T04:16'),
};

export const sampleWithPartialData: IItemInformation = {
  id: 71115,
  code: 'Balanced Account',
  name: 'Mississippi',
  specification: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2023-01-18T02:46'),
};

export const sampleWithFullData: IItemInformation = {
  id: 1768,
  code: 'utilize Account',
  name: 'deliverables',
  specification: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2023-01-17T18:23'),
  updatedAt: dayjs('2023-01-18T03:12'),
};

export const sampleWithNewData: NewItemInformation = {
  code: 'parsing',
  name: 'portal enterprise West',
  specification: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2023-01-18T03:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
